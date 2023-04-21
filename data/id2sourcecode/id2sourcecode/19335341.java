    public void writeCommand(String cmd) throws IOException {
        if (debug) {
            System.out.println("About to write command: " + cmd + " to " + baseUrl);
        }
        URL url = new URL(baseUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
        OutputStreamWriter solrWriter = new OutputStreamWriter(conn.getOutputStream());
        solrWriter.write(cmd, 0, cmd.length());
        solrWriter.flush();
        solrWriter.close();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = null;
        while ((line = rd.readLine()) != null) {
            if (debug) {
                System.out.println(line);
            }
            if (outWriter != null) {
                outWriter.write(line);
                outWriter.write('\n');
            }
        }
        rd.close();
    }
