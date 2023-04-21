    protected boolean copyFile(final File from, final File to) {
        try {
            FileUtils.copyFile(from, to);
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
        JIThumbnailService.getInstance().copyFile(from, to);
        return true;
    }
