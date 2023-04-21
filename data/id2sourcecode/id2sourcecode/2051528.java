    static File downloadFile(Proxy proxy, URL url, File path, String fileName) throws IOException {
        URLConnection conn = null;
        if (null == proxy) {
            conn = url.openConnection();
        } else {
            conn = url.openConnection(proxy);
        }
        conn.connect();
        File destFile = new File(path, fileName);
        if (destFile.exists()) {
            return destFile;
        }
        FileOutputStream fos = new FileOutputStream(destFile);
        byte[] buffer = new byte[2048];
        try {
            while (true) {
                int len = conn.getInputStream().read(buffer);
                if (len < 0) {
                    break;
                } else {
                    fos.write(buffer, 0, len);
                }
            }
            fos.close();
        } catch (IOException e) {
            destFile.delete();
            throw e;
        }
        return destFile;
    }
