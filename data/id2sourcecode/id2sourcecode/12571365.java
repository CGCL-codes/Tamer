    public String fetchPost(URL url, String data) throws IOException {
        StringBuilder page = new StringBuilder();
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            page.append(line);
        }
        wr.close();
        rd.close();
        return page.toString();
    }
