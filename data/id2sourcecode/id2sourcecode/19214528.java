    public String doPost(String data) {
        String request;
        try {
            request = URLEncoder.encode("xml", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        URL url;
        try {
            url = new URL(Midget.APPENDAGE + "/view/clientEndpoint.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        URLConnection conn;
        OutputStreamWriter wr;
        try {
            conn = url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(request);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = new String();
            while ((line = rd.readLine()) != null) {
                result += line + System.getProperty("line.separator");
                if (verbose) Logger.log("Network", "Reading: " + line);
            }
            wr.close();
            rd.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
