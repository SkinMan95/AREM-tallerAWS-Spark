package edu.escuelaing.tallergatewaylambda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import static spark.Spark.*;

/**
 *
 * @author Alejandro Anzola email: alejandro.anzola@mail.escuelaing.edu.co
 */
public class SparkApplication {

    public static final String BASE_AWS_URL = "https://mk5h44s7ye.execute-api.us-west-2.amazonaws.com/prod";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        staticFileLocation("/public");
        get("/getSquare", (request, response) -> {
            // response.type("application/json");
            String number = request.queryParams("value");
            System.out.println("Got number: " + number);
            return response(BASE_AWS_URL + "?value=" + number);
        });
    }

    public static String response(String url) throws Exception {
        StringBuilder res = new StringBuilder();
        URL address = new URL(url);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(address.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                res.append(inputLine);
                //System.out.println(inputLine);
            }
        } catch (IOException x) {
            System.err.println(x);
        }

        return res.toString();
    }

}
