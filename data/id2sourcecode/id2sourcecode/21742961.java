    public String execDelete(String strurl, String id) throws Exception {
        String data = "verb=DeleteRecord&identifier=" + URLEncoder.encode(id, "UTF-8");
        URL url = new URL(strurl);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        StringBuffer sb = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        wr.close();
        rd.close();
        return sb.toString();
    }
