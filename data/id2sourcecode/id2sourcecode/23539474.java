    public boolean select(FileDescriptor[] readFDs, FileDescriptor[] writeFDs, int numReadable, int numWritable, long timeout, int[] flags) throws SocketException {
        if (numReadable < 0 || numWritable < 0) {
            throw new IllegalArgumentException();
        }
        int total = numReadable + numWritable;
        if (total == 0) {
            return true;
        }
        assert validateFDs(readFDs, writeFDs, numReadable, numWritable) : "Invalid file descriptor arrays";
        int result = selectImpl(readFDs, writeFDs, numReadable, numWritable, flags, timeout);
        if (result >= 0) {
            return true;
        }
        if (result == ERRORCODE_SOCKET_TIMEOUT || result == ERRORCODE_SOCKET_INTERRUPTED) {
            return false;
        }
        throw new SocketException();
    }
