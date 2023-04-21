    public Annotation get(final File imageFile) {
        synchronized (writeLock) {
            if (unreadables.contains(imageFile.getAbsolutePath()) || unwriteables.contains(imageFile.getAbsolutePath())) {
                return new BlacklistedAnnotation(imageFile);
            }
            return null;
        }
    }
