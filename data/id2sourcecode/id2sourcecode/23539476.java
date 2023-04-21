    private boolean validateFDs(FileDescriptor[] readFDs, FileDescriptor[] writeFDs, int countRead, int countWrite) {
        for (int i = 0; i < countRead; ++i) {
            if (!readFDs[i].valid()) {
                return false;
            }
        }
        for (int i = 0; i < countWrite; ++i) {
            if (!writeFDs[i].valid()) {
                return false;
            }
        }
        return true;
    }
