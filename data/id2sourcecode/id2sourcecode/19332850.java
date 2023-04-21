    public String sendPOSTRequest(String URLstring) {
        String str = "";
        try {
            String data = URLEncoder.encode("data[User][username]", "UTF-8") + "=" + URLEncoder.encode("value1", "UTF-8");
            data += "&" + URLEncoder.encode("data[User][password]", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");
            URL url = new URL(URLstring);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                str += line;
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
        }
        return str;
    }
