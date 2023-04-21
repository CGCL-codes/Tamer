    private static boolean put(URL url, String content) throws IOException, ProtocolException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("PUT");
        OutputStreamWriter outReq = new OutputStreamWriter(con.getOutputStream());
        outReq.write(content);
        outReq.flush();
        outReq.close();
        System.out.println(con.getResponseCode());
        System.out.println(con.getResponseMessage());
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStreamReader is = new InputStreamReader(con.getInputStream());
            String response = readIs(is);
            is.close();
            System.out.println(response);
            return true;
        } else {
            System.out.println(con.getResponseCode());
            System.out.println(con.getResponseMessage());
            return false;
        }
    }
