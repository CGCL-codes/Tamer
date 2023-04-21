    public long transfer(InputStream inStream, FileOutputStream outStream, boolean keepOutStreamOpen, long size) throws RuntimeIoException {
        ReadableByteChannel inChannel = Channels.newChannel(inStream);
        FileChannel outChannel = outStream.getChannel();
        RuntimeIoException t = null;
        try {
            return outChannel.transferFrom(inChannel, 0, size);
        } catch (Exception e) {
            t = new RuntimeIoException(e, IoMode.COPY);
            throw t;
        } finally {
            boolean doThrow = (t == null);
            try {
                inChannel.close();
            } catch (Exception e) {
                RuntimeIoException ex = new RuntimeIoException(e, IoMode.CLOSE);
                if (t != null) {
                    t.addSuppressed(ex);
                } else {
                    t = ex;
                }
            }
            if (!keepOutStreamOpen) {
                try {
                    outChannel.close();
                } catch (Exception e) {
                    RuntimeIoException ex = new RuntimeIoException(e, IoMode.CLOSE);
                    if (t != null) {
                        t.addSuppressed(ex);
                    } else {
                        t = ex;
                    }
                }
            }
            if ((t != null) && doThrow) {
                throw t;
            }
        }
    }
