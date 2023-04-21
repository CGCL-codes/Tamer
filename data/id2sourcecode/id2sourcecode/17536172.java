    public long transfer(FileInputStream inStream, OutputStream outStream, boolean keepOutStreamOpen) throws RuntimeIoException {
        FileChannel inChannel = inStream.getChannel();
        WritableByteChannel outChannel = Channels.newChannel(outStream);
        RuntimeIoException t = null;
        try {
            return inChannel.transferTo(0, inChannel.size(), outChannel);
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
