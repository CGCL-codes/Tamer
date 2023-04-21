    public static void readWriteCharUtil(URL sxURL, Writer writer) throws IOException {
        BufferedReader bfReader = null;
        int byteCnt = 0;
        char[] buffer = new char[4096];
        if (bDebug) System.out.println("RW Base Directory - " + sxURL);
        if (bDebug) System.out.println("RW Loading - " + sxURL);
        try {
            bfReader = new BufferedReader(new InputStreamReader(openStream(sxURL)));
            while ((byteCnt = bfReader.read(buffer)) != -1) {
                if (writer != null && byteCnt > 0) {
                    writer.write(buffer, 0, byteCnt);
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (bfReader != null) {
                    bfReader.close();
                }
            } catch (IOException ioe) {
            }
        }
    }
