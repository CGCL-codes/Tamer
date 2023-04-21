    public GeodeticPropertyFile(URL url) throws IOException {
        super();
        a = 0;
        try {
            java.io.InputStream inputstream = url.openStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "UTF8");
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            int i = 0;
            b = new StringBuffer(4096);
            while ((i = bufferedreader.read()) != -1) {
                b.append((char) i);
            }
            bufferedreader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
