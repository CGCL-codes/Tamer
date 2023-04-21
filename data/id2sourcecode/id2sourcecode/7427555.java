    public InputStream getData() throws Exception {
        URL url;
        HttpURLConnection conn;
        InputStream is = null;
        try {
            if (sessionId == null) {
                throw new Exception("Not connected");
            }
            url = new URL("http://cri-srv-ade.insa-toulouse.fr:8080/ade/custom/modules/plannings/info.jsp");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Cookie", sessionId + "; " + displayCookie);
            is = conn.getInputStream();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return is;
    }
