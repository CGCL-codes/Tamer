    private void read() throws Exception {
        URL url;
        URLConnection connect;
        BufferedReader in;
        url = getURL("SCHEMA");
        connect = url.openConnection();
        in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) _lines.addElement(line);
        in.close();
    }
