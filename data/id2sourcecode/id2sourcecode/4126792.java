    private static void reloadServerCache() {
        try {
            URL url = new URL("http://localhost:8080/servlet/trackerdogs.Reload");
            InputStream is = url.openStream();
            System.out.println("Web server reload");
        } catch (IOException ex) {
            System.out.println("Error: Web Server offline");
        }
    }
