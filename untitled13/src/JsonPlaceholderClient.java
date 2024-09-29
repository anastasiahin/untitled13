import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class JsonPlaceholderClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/users";

    // Метод для створення нового користувача
    public static String createUser(String jsonInputString) throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return readResponse(connection);
    }

    // Метод для оновлення інформації про користувача
    public static String updateUser(int userId, String jsonInputString) throws IOException {
        URL url = new URL(BASE_URL + "/" + userId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return readResponse(connection);
    }

    // Метод для видалення користувача
    public static int deleteUser(int userId) throws IOException {
        URL url = new URL(BASE_URL + "/" + userId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        return connection.getResponseCode();
    }

    // Метод для отримання списку всіх користувачів
    public static String getAllUsers() throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        return readResponse(connection);
    }

    // Метод для зчитування відповіді від сервера
    private static String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }

    public static void main(String[] args) throws IOException {
        // Приклад створення користувача
        String newUser = "{ \"name\": \"John Doe\", \"username\": \"johndoe\", \"email\": \"johndoe@example.com\" }";
        System.out.println("Створення користувача:");
        String createdUser = createUser(newUser);
        System.out.println(createdUser);

        // Приклад оновлення користувача
        String updatedUser = "{ \"name\": \"John Doe Updated\", \"username\": \"johndoeupdated\", \"email\": \"johndoeupdated@example.com\" }";
        System.out.println("\nОновлення користувача:");
        String updatedResponse = updateUser(1, updatedUser); // Оновлення користувача з id 1
        System.out.println(updatedResponse);

        // Приклад видалення користувача
        System.out.println("\nВидалення користувача:");
        int deleteResponseCode = deleteUser(1); // Видалення користувача з id 1
        System.out.println("Статус відповіді: " + deleteResponseCode);

        // Приклад отримання всіх користувачів
        System.out.println("\nСписок всіх користувачів:");
        String users = getAllUsers();
        System.out.println(users);
    }
}
