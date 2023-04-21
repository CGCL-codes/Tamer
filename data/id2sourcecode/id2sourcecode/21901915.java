    private static void fsync(PyObject fd, boolean metadata) {
        RawIOBase rawIO = FileDescriptors.get(fd);
        rawIO.checkClosed();
        Channel channel = rawIO.getChannel();
        if (!(channel instanceof FileChannel)) {
            throw Py.OSError(Errno.EINVAL);
        }
        try {
            ((FileChannel) channel).force(metadata);
        } catch (ClosedChannelException cce) {
            throw Py.ValueError("I/O operation on closed file");
        } catch (IOException ioe) {
            throw Py.OSError(ioe);
        }
    }
