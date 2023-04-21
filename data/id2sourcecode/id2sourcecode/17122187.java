    public void test_openConnection_windows_path_character() throws Exception {
        int port = Support_Jetty.startDefaultHttpServer();
        HttpURLConnection con = null;
        try {
            URL url = new URL("http://0.0.0.0:" + port + "/servlet?ResourceName=C:\\temp\\test.txt");
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("GET");
            InputStream is = con.getInputStream();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
