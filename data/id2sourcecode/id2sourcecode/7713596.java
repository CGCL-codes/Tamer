    public static void downloadUrlToFile(URL url, File result) throws IOException {
        IOException exception = null;
        InputStream is = null;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        byte[] buf = new byte[1024];
        try {
            is = url.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            fos = new FileOutputStream(result);
            int bytesRead;
            bytesRead = dis.read(buf);
            while (bytesRead > 0) {
                fos.write(buf, 0, bytesRead);
                bytesRead = dis.read(buf);
            }
        } catch (IOException ioe) {
            exception = ioe;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException ioe) {
            }
            if (exception != null) throw exception;
        }
    }
