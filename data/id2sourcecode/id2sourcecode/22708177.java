    public static InputStream openRemoteFile(URL urlParam) throws KExceptionClass {
        InputStream result = null;
        try {
            result = urlParam.openStream();
        } catch (IOException error) {
            String message = new String();
            message = "Cant open resource [";
            message += urlParam.toString();
            message += "][";
            message += error.toString();
            message += "]";
            throw new KExceptionClass(message, error);
        }
        ;
        return (result);
    }
