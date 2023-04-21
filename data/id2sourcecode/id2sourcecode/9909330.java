    private String sendData(String stURL, String strToPost) throws IOException {
        String resp = "";
        URL url = new URL(stURL);
        HttpURLConnection conHttp = (HttpURLConnection) url.openConnection();
        String encoding = "ISO-8859-1";
        conHttp.setDoOutput(true);
        conHttp.setRequestMethod("POST");
        conHttp.setRequestProperty("Content-Type", "text/xml");
        conHttp.setRequestProperty("Authorization", "Basic " + encoding);
        conHttp.setUseCaches(true);
        OutputStream outputStream = conHttp.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        String xmlContent = strToPost;
        writer.println(xmlContent);
        writer.flush();
        outputStream.close();
        int responseCode = conHttp.getResponseCode();
        if (responseCode >= 203 && responseCode <= 505) {
            logger.warn("Got response code of " + responseCode);
        } else {
            InputStream inputStream = conHttp.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String xmlResult = "";
            while ((xmlResult = br.readLine()) != null) {
                resp += xmlResult + "\n";
            }
        }
        return resp;
    }
