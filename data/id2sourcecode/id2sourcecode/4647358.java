    public Map<FileId, byte[]> extractFiles(Set<FileId> fileNames) {
        Map<FileId, byte[]> files = new HashMap<FileId, byte[]>();
        for (FileId fileId : fileNames) {
            try {
                String filePath = getFilePath(fileId);
                URL url = getUrl(filePath);
                files.put(fileId, readStream(url.openStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return files;
    }
