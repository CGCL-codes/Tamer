    public static void copy(InputStream is, OutputStream os) {
        try {
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
        }
    }
