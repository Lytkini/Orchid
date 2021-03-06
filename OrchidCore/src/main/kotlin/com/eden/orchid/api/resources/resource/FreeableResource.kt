package com.eden.orchid.api.resources.resource

import com.eden.orchid.api.theme.pages.OrchidReference

/**
 * An abstract concept of a 'freeable' resource, that is one that is able to look its contents back up, and so can be
 * safely 'freed' when requested to save memory. Such an example would be a file, because having a reference to the
 * file makes it possible to get rid of the actual contents when needed because the file can be read back into memory
 * when the data is needed.
 */
@Deprecated(message = "This class is no longer necessary. Just extend OrchidResource directly, which now also handles freeing resources.")
abstract class FreeableResource(reference: OrchidReference) : OrchidResource(reference)
