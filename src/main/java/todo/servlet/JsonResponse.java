package todo.servlet;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Value
@Builder(toBuilder = true)
public class JsonResponse {

    HttpServletResponse response;

    public void setData(Object data) throws IOException {
        response.setContentType("text/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter writer = new PrintWriter(response.getOutputStream());

        Gson gson = new Gson();
        writer.println(gson.toJson(data));
        writer.flush();
    }
}
