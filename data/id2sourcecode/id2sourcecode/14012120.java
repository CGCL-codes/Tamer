    private static FileChannel getChannel(FileInputStream in) {
        if (!channelsAvailable) return null;
        try {
            return in.getChannel();
        } catch (UnsatisfiedLinkError x) {
            channelsAvailable = false;
            return null;
        }
    }
