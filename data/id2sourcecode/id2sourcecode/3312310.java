    public static void copyFile(InputStream in, File out) throws IOException {
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = in.read(buf)) != -1) fos.write(buf, 0, i);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (in != null) in.close();
            if (fos != null) fos.close();
        }
    }
