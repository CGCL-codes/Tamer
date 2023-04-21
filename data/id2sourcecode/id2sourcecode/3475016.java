    protected boolean moveFile(final File from, final File to) {
        boolean bool = false;
        if (confirmDelete(from)) {
            JIThumbnailService.getInstance().copyFile(from, to);
            JIThumbnailService.getInstance().removeFile(from);
            if (to.canWrite() && from.renameTo(to)) {
                bool = true;
            } else {
                try {
                    FileUtils.copyFile(from, to);
                } catch (final IOException e) {
                    e.printStackTrace();
                    return false;
                }
                bool = from.delete();
            }
        }
        return bool;
    }
