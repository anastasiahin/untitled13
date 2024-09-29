import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenTasksFetcher {

    public static void main(String[] args) {
        int userId = 1;
        fetchOpenTasks(userId);
    }

    public static void fetchOpenTasks(int userId) {
        try {
            String url = "https://jsonplaceholder.typicode.com/users/" + userId + "/todos";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Парсинг JSON
            JSONArray tasks = new JSONArray(response.toString());

            System.out.println("Відкриті задачі для користувача з ідентифікатором " + userId + ":");
            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                if (!task.getBoolean("completed")) {
                    System.out.println("- " + task.getString("title"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
