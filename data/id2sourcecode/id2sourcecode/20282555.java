    public void downloadObject(String bucketName, String objectKey, File target, ProgressListener listener) {
        try {
            FileUtils.copyFile(objectFile(bucketName, objectKey), target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
