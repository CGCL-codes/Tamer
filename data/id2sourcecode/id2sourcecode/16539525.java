    public static File renameFile(final File file, final String rename) throws IOException {
        final String oldName = file.getName();
        if (oldName.equals(rename)) {
            return file;
        }
        final String parent = file.getParent();
        final File renamedFile = new File(parent + File.separator + rename);
        FileUtils.copyFile(file, renamedFile);
        file.delete();
        return renamedFile;
    }
