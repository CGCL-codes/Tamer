    public void logout() {
        try {
            String data = URLEncoder.encode("PHPSESSID", "UTF-8") + "=" + URLEncoder.encode(this.get_session(), "UTF-8");
            URL url = new URL(URL_LOLA + FILE_LOGOUT);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            System.out.println("Logged out");
        } catch (Exception e) {
        }
    }
