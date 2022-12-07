package utils

import day3.Day3
import java.io.File

class Resource {
    companion object {
        fun getFile(name: String): File? {
            val resource = Day3::class.java.classLoader.getResource(name) ?: return null

            return File(resource.toURI())
        }
    }
}

