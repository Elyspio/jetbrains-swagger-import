package com.github.elyspio.swaggercodegen.helper.swagger.maven

import kotlinx.serialization.Serializable

@Serializable
class Version : Comparable<Version> {


    companion object {
        val Default = Version("0.0.0")
    }


    constructor(version: String) {
        if (version.filter { it == '.' }.length != 2) {
            throw Exception("Malformed version : $version")
        }
        val (major, minor, revision) = version.split(".").map { it.toInt() }
        this.major = major
        this.minor = minor
        this.revision = revision
    }

    private var major: Int
    private var revision: Int
    private var minor: Int

    override fun compareTo(other: Version): Int {
        if (other.major < this.major) return -1
        if (other.major > this.major) return 1
        if (other.minor < this.minor) return -1
        if (other.minor > this.minor) return 1
        if (other.revision < this.revision) return -1
        if (other.revision > this.revision) return 1
        return 0
    }


    override fun toString(): String {
        return "$major.$minor.$revision"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Version) return false

        if (major != other.major) return false
        if (revision != other.revision) return false
        if (minor != other.minor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = major
        result = 31 * result + revision
        result = 31 * result + minor
        return result
    }


}


