    @Override
    public boolean saveFile(String filter, InputStream in) {
        if (createLocalFile(filter)) {
            try {
                byte[] buffer = new byte[1024];
                int readBytes = 0;
                FileOutputStream fos = new FileOutputStream(file);
                while ((readBytes = in.read(buffer)) >= 0) {
                    fos.write(buffer, 0, readBytes);
                }
                in.close();
                fos.close();
                return true;
            } catch (Exception e) {
                logger.log(Level.INFO, "saveFile", e);
            }
        }
        return false;
    }
