    public static String transferURL(URL url, File dest) throws DAException {
        URLConnection conn = null;
        String contentType = null;
        FileOutputStream fileOut = null;
        InputStream input = null;
        try {
            conn = url.openConnection();
            contentType = conn.getContentType();
            contentType = (contentType != null) ? contentType : ContentType.CONTENT_TYPE_UNKNOWN;
            input = conn.getInputStream();
            fileOut = createOutputStream(dest);
            transferStream(input, fileOut);
        } catch (Exception e) {
            logger.log(DAExceptionCodes.ERROR_READING, Logger.WARN, IOUtils.class, "transferURL", null, e);
            DAException ex = new DAException(DAExceptionCodes.ERROR_READING, new String[] { url.toString() });
            throw ex;
        } finally {
            closeIOObject(input);
            closeIOObject(fileOut);
        }
        return contentType;
    }
