package com.eden.orchid.testhelpers

import com.eden.common.json.JSONElement
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.ClasspathResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.SuppressedWarnings
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.util.HashMap

open class OrchidIntegrationTest(
    vararg standardAdditionalModules: OrchidModule
) : BaseOrchidTest {

    private var serve = false

    private lateinit var flags: MutableMap<String, Any>
    private lateinit var config: MutableMap<String, Any>
    private lateinit var resources: MutableList<(OrchidContext) -> OrchidResource>

    private val standardAdditionalModules: Set<OrchidModule> = setOf(*standardAdditionalModules)

    @BeforeEach
    fun integrationTestSetUp() {
        flags = mutableMapOf()
        config = mutableMapOf()
        resources = mutableListOf()
        serve = false
    }

    @AfterEach
    fun integrationTestTearDown() {
        flags = mutableMapOf()
        config = mutableMapOf()
        resources = mutableListOf()
        serve = false
    }

    fun flag(flag: String, value: Any) {
        flags[flag] = value
    }

    @Suppress(SuppressedWarnings.UNCHECKED_KOTLIN)
    fun configObject(flag: String, json: String) {
        if (config.containsKey(flag)) {
            val o = config[flag]
            if (o is Map<*, *>) {
                config[flag] = EdenUtils.merge(o as Map<String, *>, JSONObject(json).toMap())
            } else {
                config[flag] = JSONObject(json).toMap()
            }
        } else {
            config[flag] = JSONObject(json).toMap()
        }
    }

    fun configArray(flag: String, json: String) {
        config[flag] = JSONArray(json).toList()
    }

    fun resource(path: String, content: String, json: String) {
        resource(path, content, JSONObject(json).toMap())
    }

    @JvmOverloads
    fun resource(path: String, content: String = "", data: Map<String, Any> = HashMap()) {
        resources.add { context ->
            StringResource(
                OrchidReference(context, path),
                content,
                JSONElement(JSONObject(data + mapOf("fromIntegrationTest" to true)))
            )
        }
    }

    fun classpathResource(path: String) {
        resources.add { context ->
            ClasspathResource(
                OrchidReference(context, path),
                javaClass.getResource(if(path.startsWith("/")) path else "/$path")
            )
        }
    }

    fun serveOn(port: Int) {
        enableLogging()

        flag("task", "serve")
        flag("port", port)
        this.serve = true
    }

// Execute test runner
//----------------------------------------------------------------------------------------------------------------------

    protected fun execute(vararg modules: OrchidModule): TestResults {
        return TestOrchid().runTest(
            flags,
            config,
            resources,
            listOf(*modules, *standardAdditionalModules.toTypedArray()),
            serve
        )
    }

}
