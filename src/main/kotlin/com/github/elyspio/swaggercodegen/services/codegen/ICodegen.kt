package com.github.elyspio.swaggercodegen.services.codegen

import com.github.elyspio.swaggercodegen.core.Format
import com.github.elyspio.swaggercodegen.ui.SwaggerDialog

interface ICodegen {
    fun use(): List<String>
    fun post()

    companion object {
        fun of(info: SwaggerDialog.SwaggerInfo): ICodegen? {
            return when (info.format) {
                Format.JavaRetrofit2 -> JavaCodegen(info)
                Format.TypeScriptAxios -> TypescriptCodegen(info)
                Format.TypeScriptFetch -> TypescriptCodegen(info)
            }
        }
    }

}