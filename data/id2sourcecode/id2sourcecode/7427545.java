    public void doLogin() {
        String body = "login=" + login + "&password=" + pass + "&x=14&y=49";
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL("http://cri-srv-ade.insa-toulouse.fr:8080/ade/standard/index.jsp");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.getInputStream();
            String cookie = conn.getHeaderField("Set-Cookie");
            sessionId = cookie.split(";")[0];
            url = new URL("http://cri-srv-ade.insa-toulouse.fr:8080/ade/standard/gui/interface.jsp");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setAllowUserInteraction(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Cookie", sessionId);
            OutputStream rawOutStream = conn.getOutputStream();
            PrintWriter pw = new PrintWriter(rawOutStream);
            pw.print(body);
            pw.flush();
            pw.close();
            i = conn.getInputStream();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
