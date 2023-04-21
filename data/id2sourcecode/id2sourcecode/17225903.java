    public void sendCommit() throws Exception {
        URL url = new URL(mIndexer);
        URLConnection urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "text/xml");
        urlConn.setRequestProperty("charset", "utf-8");
        PrintStream outStream = new PrintStream(urlConn.getOutputStream());
        outStream.println("<commit/>");
        outStream.flush();
        outStream.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String str;
        while (null != (str = input.readLine())) {
            System.out.println(str);
        }
        input.close();
    }
