    public static void getFromUrl(URL url, File target) throws Exception {
        LogUtil.entering(log, "Getting serverlist from " + url.toString() + ".");
        if (target.exists()) {
            if (!target.delete()) throw new Exception("File " + target.toString() + " exists, can't delete.");
        }
        target.createNewFile();
        URLConnection urlCon = url.openConnection();
        InputStream inStream = new BufferedInputStream(urlCon.getInputStream());
        FileOutputStream outStream = new FileOutputStream(target);
        byte[] buffer = new byte[2048];
        long totalSize = 0;
        try {
            while (true) {
                int nbytes = inStream.read(buffer);
                if (nbytes <= 0) break;
                totalSize += nbytes;
                outStream.write(buffer, 0, nbytes);
            }
        } catch (IOException ioe) {
            log.warning("Error: " + ioe.getMessage());
        }
        inStream.close();
        outStream.close();
        log.finest("Wrote " + totalSize + "bytes to " + target.toString());
    }
