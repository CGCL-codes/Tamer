    public void finishSessionInit(ServerCnxn cnxn, boolean valid) {
        try {
            if (valid) {
                serverCnxnFactory.registerConnection(cnxn);
            }
        } catch (Exception e) {
            LOG.warn("Failed to register with JMX", e);
        }
        try {
            ConnectResponse rsp = new ConnectResponse(0, valid ? cnxn.getSessionTimeout() : 0, valid ? cnxn.getSessionId() : 0, valid ? generatePasswd(cnxn.getSessionId()) : new byte[16]);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BinaryOutputArchive bos = BinaryOutputArchive.getArchive(baos);
            bos.writeInt(-1, "len");
            rsp.serialize(bos, "connect");
            if (!cnxn.isOldClient) {
                bos.writeBool(this instanceof ReadOnlyZooKeeperServer, "readOnly");
            }
            baos.close();
            ByteBuffer bb = ByteBuffer.wrap(baos.toByteArray());
            bb.putInt(bb.remaining() - 4).rewind();
            cnxn.sendBuffer(bb);
            if (!valid) {
                LOG.info("Invalid session 0x" + Long.toHexString(cnxn.getSessionId()) + " for client " + cnxn.getRemoteSocketAddress() + ", probably expired");
                cnxn.sendBuffer(ServerCnxnFactory.closeConn);
            } else {
                LOG.info("Established session 0x" + Long.toHexString(cnxn.getSessionId()) + " with negotiated timeout " + cnxn.getSessionTimeout() + " for client " + cnxn.getRemoteSocketAddress());
            }
            cnxn.enableRecv();
        } catch (Exception e) {
            LOG.warn("Exception while establishing session, closing", e);
            cnxn.close();
        }
    }
