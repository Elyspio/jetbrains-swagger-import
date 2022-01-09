package com.github.elyspio.swaggercodegen.core

enum class Format(val label: String, val codegen: String? = null) {
    JavaRetrofit2("Java (retrofit2)", "java"),
    Kotlin("Kotlin (retrofit2)", "kotlin"),
    TypeScriptAxios("TypeScript (axios)", "typescript-axios"),
    TypeScriptFetch("TypeScript (fetch)", "typescript-fetch"),
    TypeScriptInversify("TypeScript (inversify)", "typescript-inversify"),
    TypeScriptRestTest("TypeScript (test)"),
    CSharp("CSharp", "csharp-netcore")

}

