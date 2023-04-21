    public static final File create(Resource resource, boolean overwrite) throws IOException {
        File file = resource.getFile();
        if (file.exists()) {
            Assert.isTrue(overwrite, "file already exists: enable overwriting");
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                Assert.isTrue(parent.mkdirs(), "failed to create file directory tree");
            }
        }
        return file;
    }
