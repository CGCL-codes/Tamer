    public void copyTo(CollectionResource newParent, String newName) {
        File dest = new File(newParent.getName(), newName);
        try {
            FileUtils.copyFile(file, dest);
        } catch (IOException ex) {
            throw new RuntimeException("Failed doing copy to: " + dest.getAbsolutePath(), ex);
        }
    }
