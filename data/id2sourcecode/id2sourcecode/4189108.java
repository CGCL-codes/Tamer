    protected boolean moveFile(final File from, final File to) {
        boolean bool = false;
        if (confirmDelete(from)) {
            JIThumbnailService.getInstance().copyFile(from, to);
            JIThumbnailService.getInstance().removeFile(from);
            if (from.renameTo(to)) {
                log.debug("from.renameTo(to) == true");
                bool = true;
                JIUtility.deleteFile(from);
            } else {
                try {
                    FileUtils.copyFile(from, to);
                } catch (final IOException e) {
                    e.printStackTrace();
                    return false;
                }
                bool = true;
                log.debug("Files.copy(from, to) == true");
                JIUtility.deleteFile(from);
                log.debug(from.getPath() + " exists = " + from.exists());
            }
        }
        return bool;
    }
