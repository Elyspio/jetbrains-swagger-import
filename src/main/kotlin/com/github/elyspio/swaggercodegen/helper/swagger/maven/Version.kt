package com.github.elyspio.swaggercodegen.helper.swagger.maven

import kotlinx.serialization.Serializable

@Serializable
class Version : Comparable<Version> {
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
        return compareValuesBy(this, other, Version::major, Version::minor, Version::revision)
    }

    override fun toString(): String {
        return "$major.$minor.$revision"
    }
}


