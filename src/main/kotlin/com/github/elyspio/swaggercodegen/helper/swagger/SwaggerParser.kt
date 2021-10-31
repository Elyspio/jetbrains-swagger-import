package com.github.elyspio.swaggercodegen.helper.swagger

import com.github.elyspio.swaggercodegen.ui.SwaggerFormData
import org.json.JSONObject
import java.net.URL


object SwaggerParser {

    const val ALL_CONTROLLERS = "All"


    fun extract(conf: SwaggerFormData): SwaggerDefinition {
        val raw = URL(conf.url).readText()
        val json = JSONObject(raw)
        val selectedController = conf.additionalParams.typeScriptTestUnit!!.controllers
        val getOnly = setOf(selectedController)

        val ret = SwaggerDefinition(json.getString("openapi"), mutableMapOf(), mutableListOf())



        json.getJSONArray("tags").forEach {
            ret.tags.add(TagElement((it as JSONObject).getString("name")))
        }

        val paths = json.getJSONObject("paths")
        paths.keySet().forEach { url ->
            val methods = paths.getJSONObject(url)
            methods.keySet().forEach { method ->
                val methodObj = methods.getJSONObject(method)
                if (selectedController.contains(ALL_CONTROLLERS) || methodObj.getJSONArray("tags").intersect(getOnly).isNotEmpty()) {
                    if (!ret.paths.contains(url)) {
                        ret.paths[url] = mutableListOf()
                    }

                    val reponses = methodObj.getJSONObject("responses")
                    val tags = methodObj.getJSONArray("tags")
                    val operation = Operation(methodObj.getString("operationId"), mutableListOf(), method, tags.getString(0))
                    reponses.keySet().forEach { httpCode ->
                        val responseObj = reponses.getJSONObject(httpCode)
                        var schemaRef = responseObj.optJSONObject("schema")?.getString("\$ref")
                        schemaRef = schemaRef?.substring(schemaRef.lastIndexOf('/'))
                        operation.responses.add(
                            Response(
                                responseObj.optString("description", ""),
                                schemaRef,
                                httpCode
                            )
                        )
                    }
                    ret.paths[url]!!.add(operation)
                }
            }
        }

        return ret
    }


    fun getTags(url: String): List<String> {
        val raw = URL(url).readText()
        val json = JSONObject(raw)
        val tags = json.getJSONArray("tags")

        val ret = mutableListOf<String>()

        for (i in 0 until tags.length()) {
            ret.add(tags.getJSONObject(i).getString("name"))
        }

        return ret
    }

}