    private static void downloadFile(URL fromDir, File toDir, String fileName) throws Throwable {
        URL source;
        URLConnection conn;
        File result;
        BufferedInputStream in;
        BufferedOutputStream out;
        int bSize;
        byte buffer[];
        int ccount;
        bSize = 256;
        buffer = new byte[bSize];
        source = new URL(fromDir, fileName);
        result = new File(toDir, fileName);
        if (result.exists()) result.delete();
        conn = source.openConnection();
        in = new BufferedInputStream(conn.getInputStream());
        out = new BufferedOutputStream(new FileOutputStream(result));
        while ((ccount = in.read(buffer, 0, bSize)) != -1) out.write(buffer, 0, ccount);
        in.close();
        out.close();
    }
