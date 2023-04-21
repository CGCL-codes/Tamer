    private static void checkIoWaitWrite(VM_ThreadIOWaitData waitData) throws SocketException, SocketTimeoutException {
        if (waitData.timedOut()) throw new SocketTimeoutException("socket operation timed out");
        if ((waitData.writeFds[0] & VM_ThreadIOConstants.FD_INVALID_BIT) != 0) throw new SocketException("invalid socket file descriptor");
    }
