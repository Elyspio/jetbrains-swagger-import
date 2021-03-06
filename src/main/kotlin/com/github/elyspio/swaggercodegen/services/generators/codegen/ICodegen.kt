package com.github.elyspio.swaggercodegen.services.generators.codegen

import com.github.elyspio.swaggercodegen.core.Format
import com.github.elyspio.swaggercodegen.services.generators.test.typescript.TypescriptTestUnit
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData

interface ICodegen {
    fun use(): List<String>
    fun post()

    companion object {
        fun of(info: SwaggerFormData): ICodegen? {
            return when (info.format) {
                Format.JavaRetrofit2 -> JavaCodegen(info)
                Format.TypeScriptAxios -> TypescriptCodegen(info)
                Format.TypeScriptFetch -> TypescriptCodegen(info)
                Format.TypeScriptRestTest -> TypescriptTestUnit(info)
            }
        }
    }

}