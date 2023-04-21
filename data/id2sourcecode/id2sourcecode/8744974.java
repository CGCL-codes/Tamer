    public static byte[] digest(URL resource) throws ATLASIOException {
        try {
            byte[] buffer = new byte[8192];
            InputStream is = new BufferedInputStream(resource.openStream(), 8192);
            int lenght = 0;
            while ((lenght = is.read(buffer)) != -1) MESSAGE_DIGEST.update(buffer, 0, lenght);
            is.close();
            return MESSAGE_DIGEST.digest();
        } catch (Exception e) {
            throw new ATLASIOException("DigestUtil couldn't digest resource at: " + resource.toExternalForm() + ". The following error was encountered: " + e.getLocalizedMessage(), e);
        }
    }
