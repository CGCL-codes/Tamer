    private String httpPOST(URL url, String data) throws IOException {
        String result = null;
        if ("http".equals(url.getProtocol())) {
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            log.println("Data sent.");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            if ((line = rd.readLine()) != null) result = line;
            wr.close();
            rd.close();
        }
        return result;
    }
