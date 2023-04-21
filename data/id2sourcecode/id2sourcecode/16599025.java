    protected static FileSystemEntry createEntryFromFile(File file, String path, String filename, String ftpSuffix) throws IOException {
        String md5Name = FtpHosting.hashFileForFtp(file);
        String url = FtpHosting.getUrl(ftpSuffix + "/" + md5Name);
        FileUtils.copyFile(file, new File(FtpHosting.getLocalPath(ftpSuffix + "/" + md5Name)));
        FileSystemEntry entry = new FileSystemEntry();
        entry.setType(Type.REMOTE);
        entry.setFilename(filename);
        if (path != null) {
            entry.getPaths().add(path);
        }
        entry.setContentLength(file.length());
        entry.setUrl(url);
        return entry;
    }
