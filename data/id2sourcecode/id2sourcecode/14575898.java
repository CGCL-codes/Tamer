    private void copy(File source, File dest) {
        try {
            dest.delete();
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            log.error("Unable to copy file: \"" + source.getAbsolutePath() + "\" to \"" + dest.getAbsolutePath() + "\".");
            log.debug(e);
            throw new RuntimeException(e);
        }
    }
