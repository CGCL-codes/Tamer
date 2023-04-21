    public void sendMsg(String msg) throws ClientNotConnectedException {
        if (!isConnected()) throw new ClientNotConnectedException();
        try {
            int select = writeSelector.select();
            while (select == 0) {
                select = writeSelector.select();
                Debug.print("sendMsg::Socket not ready for write", 3);
            }
            if (select < 0) {
                Debug.print("sendMsg::Error! (sendMsg). write select < 0.", 3);
            }
            if (select > 0) {
                Debug.print("sendMsg::Socket is ready for write", 3);
                Set readyKeys = writeSelector.selectedKeys();
                Iterator readyItor = readyKeys.iterator();
                while (readyItor.hasNext()) {
                    SelectionKey key = (SelectionKey) readyItor.next();
                    readyItor.remove();
                    SocketChannel keyChannel = (SocketChannel) key.channel();
                    if (key.isWritable()) {
                        outBuff = null;
                        outBuff = ByteBuffer.wrap(new String(msg).getBytes());
                        int numBytesWritten = keyChannel.write(outBuff);
                        outBuff.clear();
                        Debug.print("sendMsg::Writing finished", 3);
                    }
                }
            }
        } catch (IOException e) {
            Debug.print("sendMsg::Error! (sendMsg). " + e, 3);
        }
    }
