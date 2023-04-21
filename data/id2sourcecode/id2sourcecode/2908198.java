    public String getResponse(String botId, String userId, String message) {
        String data, xmlResponse = "";
        try {
            data = URLEncoder.encode("botid", "UTF-8") + "=" + URLEncoder.encode(botId, "UTF-8");
            data += "&" + URLEncoder.encode("input", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
            data += "&" + URLEncoder.encode("custid", "UTF-8") + "=" + URLEncoder.encode(userId, "UTF-8");
            URL url = new URL("http://www.pandorabots.com/pandora/talk-xml");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            xmlResponse = rd.readLine();
            wr.close();
            rd.close();
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }
        return getMessage(xmlResponse);
    }
