    private void copyFile(InputStream in, OutputStream out) throws FileManagerException {
        try {
            byte[] buffer = new byte[4096];
            int bytes_read;
            while ((bytes_read = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytes_read);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileManagerException("Copy file failed");
        }
    }
