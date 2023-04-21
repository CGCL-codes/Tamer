    public void writeTo(TreeLogger logger, OutputStream out) throws UnableToCompleteException {
        try {
            InputStream in = getContents(logger);
            Util.copyNoClose(in, out);
            Utility.close(in);
        } catch (IOException e) {
            logger.log(TreeLogger.ERROR, "Unable to read or write stream", e);
            throw new UnableToCompleteException();
        }
    }
