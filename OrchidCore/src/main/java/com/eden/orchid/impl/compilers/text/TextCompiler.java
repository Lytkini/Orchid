package com.eden.orchid.impl.compilers.text;

import com.eden.orchid.api.compilers.OrchidCompiler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TextCompiler extends OrchidCompiler {

    @Inject
    public TextCompiler() {
        super(800);
    }

    @Override
    public String compile(String extension, String source, Object... data) {
        return source;
    }

    @Override
    public String getOutputExtension() {
        return "html";
    }

    @Override
    public String[] getSourceExtensions() {
        return new String[]{"txt", "text"};
    }
}
