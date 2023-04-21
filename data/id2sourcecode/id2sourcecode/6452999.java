    private static void downloadFile(URL url, File destFile) throws IOException {
        log.info("downloading file: " + url);
        OutputStream out = null;
        InputStream in = null;
        try {
            in = url.openStream();
            out = new FileOutputStream(destFile);
            byte[] buff = new byte[1024];
            for (int len = in.read(buff); len != -1; len = in.read(buff)) {
                out.write(buff, 0, len);
            }
            log.info("saved file: " + destFile);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
