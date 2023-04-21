    public InputStream getFile(String fileName) throws SftpException, IOException {
        return getChannel().get(url.getPath() + File.separatorChar + fileName);
    }
