    public static void DownloadFromUrl(String url, String path) {
        File outputFile = new File("CodeKeeperDB.xml");
        java.io.BufferedInputStream in = null;
        try {
            if (outputFile.exists()) {
                outputFile.delete();
            }
            in = new java.io.BufferedInputStream(new URL(url + path).openStream());
            FileOutputStream fos = new FileOutputStream(outputFile.toString());
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            while (in.read(data, 0, 1024) >= 0) {
                bout.write(data);
            }
            bout.close();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(CodeKeeperView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
