package com.github.elyspio.swaggercodegen.services.generators.codegen

import com.github.elyspio.swaggercodegen.core.Format
import com.github.elyspio.swaggercodegen.services.generators.codegen.jvm.JavaCodegen
import com.github.elyspio.swaggercodegen.services.generators.codegen.jvm.KotlinCodegen
import com.github.elyspio.swaggercodegen.services.generators.test.typescript.TypescriptTestUnit
import com.github.elyspio.swaggercodegen.ui.SwaggerFormData

interface ICodegen {
    fun use(): List<String>
    fun post()

    companion object {
        fun of(info: SwaggerFormData): ICodegen {
            return when (info.format) {
                Format.JavaRetrofit2 -> JavaCodegen(info)
                Format.Kotlin -> KotlinCodegen(info)
                Format.KotlinCoroutine -> KotlinCodegen(info)
                Format.TypeScriptAxios -> TypescriptCodegen(info)
                Format.TypeScriptFetch -> TypescriptCodegen(info)
                Format.TypeScriptInversify -> TypescriptCodegen(info)
                Format.TypeScriptRestTest -> TypescriptTestUnit(info)
                Format.CSharp -> CSharpCodegen(info)
            }
        }
    }

}