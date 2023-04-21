    @Override
    public InputStream getInputStream() throws IOException {
        String filePath = getPath();
        try {
            return getChannel().get(filePath);
        } catch (SftpException e) {
            throw new IOException("Can't retrieve " + filePath + " from " + url.getHost() + " because " + e.getMessage());
        }
    }
