    public static long getChannelAddress(Channel channel) {
        if (channel instanceof FileDescriptorHandler) {
            return getFDAddress(((FileDescriptorHandler) channel).getFD());
        } else if (channel instanceof FileChannelImpl) {
            return ((FileChannelImpl) channel).getHandle();
        }
        return 0;
    }
