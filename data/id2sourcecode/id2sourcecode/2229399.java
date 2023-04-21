    public static SimpleFileOp_Delete getDeleteOperation(AbstractFile l_file) throws IOException {
        SimpleFileOp_Delete[] subOperations = null;
        if (l_file.isDirectory()) {
            AbstractFile[] files = l_file.listFiles();
            subOperations = new SimpleFileOp_Delete[files.length];
            for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
                if (false == files[fileIndex].isDirectory()) {
                    subOperations[fileIndex] = new SimpleFileOp_Delete(files[fileIndex]);
                } else {
                    subOperations[fileIndex] = getDeleteOperation(files[fileIndex]);
                }
            }
        }
        SimpleFileOp_Delete result = new SimpleFileOp_Delete(l_file);
        result.childOperations = subOperations;
        return result;
    }
