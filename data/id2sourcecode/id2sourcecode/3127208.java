    public static final String convertStreamToFile(final String filePath, final InputStream inputStream) {
        String ret = "";
        try {
            File f = new File(filePath);
            OutputStream out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            inputStream.close();
            ret = f.getAbsolutePath();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return ret;
    }
