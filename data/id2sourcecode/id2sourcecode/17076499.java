    public static int copyURL2File(URL url, File outFile) throws IOException {
        URLConnection conn = url.openConnection();
        conn.connect();
        int totalNumBytes = 0;
        InputStream in = new BufferedInputStream(conn.getInputStream());
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
            try {
                totalNumBytes = copyStream2Stream(in, out, conn.getContentLength());
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
        return totalNumBytes;
    }
