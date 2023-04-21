    private static void loadStream(InputStream in, BufferedInputStream bufInputStream, OutputStream outputStream) throws DriverException {
        NetOps.Message msg = new NetOps.Message();
        int bytes_read = 0;
        try {
            while (bytes_read != -1) {
                bytes_read = in.read(msg.body, 5, SEDNA_BULK_LOAD_PORTION);
                if (bytes_read != -1) {
                    msg.instruction = se_BulkLoadPortion;
                    msg.length = bytes_read + 5;
                    msg.body[0] = 0;
                    NetOps.writeInt(bytes_read, msg.body, 1);
                    NetOps.writeMsg(msg, outputStream);
                }
            }
        } catch (DriverException de) {
            msg.instruction = se_BulkLoadError;
            msg.length = 0;
            NetOps.writeMsg(msg, outputStream);
            NetOps.readMsg(msg, bufInputStream);
            throw de;
        } catch (IOException ioe) {
            msg.instruction = se_BulkLoadError;
            msg.length = 0;
            NetOps.writeMsg(msg, outputStream);
            NetOps.readMsg(msg, bufInputStream);
            throw new DriverException(ErrorCodes.SE3007, ioe.toString());
        }
        msg.instruction = se_BulkLoadEnd;
        msg.length = 0;
        NetOps.writeMsg(msg, outputStream);
    }
