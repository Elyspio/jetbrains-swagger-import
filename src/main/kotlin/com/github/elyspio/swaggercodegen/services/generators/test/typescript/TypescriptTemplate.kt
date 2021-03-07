package com.github.elyspio.swaggercodegen.services.generators.test.typescript

import com.github.elyspio.swaggercodegen.helper.swagger.Operation
import com.github.elyspio.swaggercodegen.helper.swagger.Response
import com.github.elyspio.swaggercodegen.helper.swagger.SwaggerDefinition

object TypescriptTemplate {


    fun fill(def: SwaggerDefinition): String {

        var routes = ""


        for (it in def.paths) {
            var str = ""
            for (ope in it.value) {
                str += "\n" + fillRoute(it.key, ope)
            }

            routes += "\n\n" + str
        }

        return """
const port = 7001
describe("Rest", () => {
        
    beforeAll(async () => {
        const platform = await PlatformExpress.bootstrap(Server, {httpPort: port, port});
        await platform.listen();
    });
            
    $routes
            
});
""".trimIndent()
    }


    private fun fillRoute(url: String, operation: Operation): String {

        var str = ""
        for (it in operation.responses) {
            str += "\n" + fillResponse(operation.tag, operation.operationId, it)
        }

        return """
        describe("${operation.method.toUpperCase()} $url", () => {
                $str
        });     
        """.trimIndent()
    }

    private fun fillResponse(controller: String, operationId: String, response: Response): String {
        val code = response.httpCode
        return """
             it("${response.description}", async () => {
//                    const data: any = {};
//                    const ret = await new Apis.${removeSpaces(controller)}Api(undefined, "http://localhost:" + port).${removeSpaces(operationId)}(data);
//                    expect(ret.status).toEqual($code);
             });
        """.trimIndent()
    }


}


fun removeSpaces(str: String): String {
    return str
        .replace(" ", "")
        .replace("_", "");
}