    public byte[] getMD5(FileType file) {
        byte[] hash = null;
        InputStream input = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            File appFile = new File(this.getFilePath(file));
            if (appFile.exists() && appFile.canRead()) {
                int read = 0;
                byte[] buffer = new byte[4096];
                input = new FileInputStream(appFile);
                while ((read = input.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
                }
                hash = digest.digest();
            }
        } catch (Exception ex) {
            logger.warn(ex);
        } finally {
            try {
                if (input != null) input.close();
            } catch (Exception ex) {
                logger.warn(ex);
            }
        }
        return hash;
    }
