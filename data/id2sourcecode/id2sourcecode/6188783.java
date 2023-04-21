    public static void copy(InputStream source, File dest) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest);
            byte[] buff = new byte[1024];
            int read = -1;
            while ((read = source.read(buff)) > -1) {
                out.write(buff, 0, read);
            }
        } finally {
            if (null != source) source.close();
            if (null != out) out.close();
        }
    }
