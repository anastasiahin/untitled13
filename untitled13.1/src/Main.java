import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserCommentsFetcher {

    private static final String USER_POSTS_URL = "https://jsonplaceholder.typicode.com/users/1/posts";
    private static final String COMMENTS_URL_TEMPLATE = "https://jsonplaceholder.typicode.com/posts/%d/comments";

    public static void main(String[] args) {
        try {
            // Отримати останній пост користувача
            int lastPostId = getLastPostId();

            // Отримати коментарі до останнього поста
            JSONArray comments = getComments(lastPostId);

            // Записати коментарі у файл
            writeCommentsToFile(1, lastPostId, comments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getLastPostId() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(USER_POSTS_URL).openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Знайти пост з найбільшим id
        JSONArray posts = new JSONArray(response.toString());
        int lastPostId = 0;

        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            if (post.getInt("id") > lastPostId) {
                lastPostId = post.getInt("id");
            }
        }

        return lastPostId;
    }

    private static JSONArray getComments(int postId) throws IOException {
        String urlString = String.format(COMMENTS_URL_TEMPLATE, postId);
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JSONArray(response.toString());
    }

    private static void writeCommentsToFile(int userId, int postId, JSONArray comments) {
        String fileName = String.format("user-%d-post-%d-comments.json", userId, postId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(comments.toString(2)); // Записати JSON з відступами
            System.out.println("Коментарі записані у файл: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
