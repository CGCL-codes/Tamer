    public void onPacketReceived(IOBuffer readBuf, IOBuffer writeBuf) {
        logger.debug("DEBUG ENTER");
        switch(state) {
            case READ_AUTH:
                state = State.WRITE_RESULT;
                PacketAuth auth = new PacketAuth();
                auth.putBytes(readBuf.getBytes(0, readBuf.limit()));
                String user = "";
                if (auth.user.length() > 1) {
                    user = auth.user.substring(0, auth.user.length() - 1);
                }
                try {
                    if (MysqlServerDef.index2Charset.containsKey((int) auth.charsetNum)) {
                        handle.setCharset(MysqlServerDef.index2Charset.get((int) auth.charsetNum));
                    }
                    if (auth.dbName.length() > 0) {
                        handle.setDb(auth.dbName);
                    }
                    if (checkAuth(user, auth.pass)) {
                        writePacket(writeBuf, new PacketOk());
                        break;
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                PacketError err = new PacketError();
                err.errno = 1045;
                err.message = "Access denied for user " + auth.user;
                writePacket(writeBuf, err);
                state = State.CLOSE;
                break;
            case READ_COMMOND:
                state = State.WRITE_RESULT;
                long start = System.currentTimeMillis();
                PacketCommand cmd = new PacketCommand();
                cmd.putBytes(readBuf.getBytes(0, readBuf.limit()));
                List<Packet> resultList = null;
                try {
                    resultList = handle.executeCommand(cmd);
                    if (resultList == null || resultList.size() == 0) {
                        session.setNextState(MyBridgeSession.STATE_CLOSE);
                        return;
                    }
                    resultIter = resultList.iterator();
                } catch (Exception e) {
                    e.printStackTrace();
                    session.setNextState(MyBridgeSession.STATE_CLOSE);
                    return;
                }
                writePacketList(writeBuf, resultList);
                long cost = System.currentTimeMillis() - start;
                logger.info("finished commond " + cost);
                break;
            case CLOSE:
            default:
                session.setNextState(MyBridgeSession.STATE_CLOSE);
        }
    }
