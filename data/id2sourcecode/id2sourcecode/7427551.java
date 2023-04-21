    public void doInit() throws Exception {
        if (sessionId == null) {
            throw new Exception("Not connected");
        }
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL("http://cri-srv-ade.insa-toulouse.fr:8080/ade/standard/projects.jsp");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Cookie", sessionId);
            i = conn.getInputStream();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
