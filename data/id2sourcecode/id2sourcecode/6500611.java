    public static void write(InputStream is, File f) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(f);
            int read = 0;
            byte[] buffer = new byte[8096];
            while ((read = is.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
        } catch (IOException e) {
            throw new UnexpectedException(e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
            try {
                if (os != null) os.close();
            } catch (Exception e) {
            }
        }
    }
