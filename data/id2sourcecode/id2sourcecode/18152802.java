    private static void callServlet() throws IOException {
        URL url = new URL("http", "localhost", 8080, "/samples.simpleServlet/HelloServlet");
        System.out.println("Connecting to " + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();
        do {
            System.out.println(response);
            response = reader.readLine();
        } while (response != null);
    }
