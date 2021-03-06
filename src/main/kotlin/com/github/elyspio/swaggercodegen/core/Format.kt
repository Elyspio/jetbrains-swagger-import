package com.github.elyspio.swaggercodegen.core

enum class Format(val label: String, val codegen: String? = null) {
    JavaRetrofit2("Java (retrofit2)", "java"),
    TypeScriptAxios("TypeScript (axios)", "typescript-axios"),
    TypeScriptFetch("TypeScript (fetch)", "typescript-fetch"),
    TypeScriptRestTest("TypeScript (test)")
}