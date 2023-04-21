    private boolean validateFDs(FileDescriptor[] readFDs, FileDescriptor[] writeFDs) {
        for (FileDescriptor fd : readFDs) {
            if (!fd.valid()) {
                return false;
            }
        }
        for (FileDescriptor fd : writeFDs) {
            if (!fd.valid()) {
                return false;
            }
        }
        return true;
    }
