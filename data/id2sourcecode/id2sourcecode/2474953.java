    public void deleteFile(String fileName) throws SftpException, IOException {
        String deleteFlag = getQueryMap().get("deletefile");
        if (deleteFlag != null && Boolean.parseBoolean(deleteFlag)) {
            String file = url.getPath() + File.separatorChar + fileName;
            log().debug("deleting file " + file + " from " + url.getHost());
            getChannel().rm(file);
        }
    }
