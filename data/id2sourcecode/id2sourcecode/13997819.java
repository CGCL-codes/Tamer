    int doWrites() {
        if (writeRequests.isEmpty()) {
            return 0;
        }
        WriteRequest req = null;
        if (nextWrite > System.currentTimeMillis()) return 0;
        if (System.currentTimeMillis() - lastWrite < 3000) {
            if (bursts == maxBurst) {
                nextWrite = System.currentTimeMillis() + 8000;
                bursts = 0;
                return 0;
            }
            bursts++;
        } else {
            bursts = 0;
            lastWrite = System.currentTimeMillis();
        }
        req = writeRequests.remove(0);
        String data;
        if (req.getType() == WriteRequest.Type.CHANNEL_MSG) {
            if (req.getMessage().length() > 100) {
                writeRequests.add(0, new WriteRequest(req.getMessage().substring(100), req.getChannel(), req.getSession()));
                data = "PRIVMSG " + req.getChannel().getName() + " :" + req.getMessage().substring(0, 100) + "\r\n";
            } else {
                data = "PRIVMSG " + req.getChannel().getName() + " :" + req.getMessage() + "\r\n";
            }
        } else if (req.getType() == WriteRequest.Type.PRIVATE_MSG) {
            if (req.getMessage().length() > 255) {
                writeRequests.add(0, new WriteRequest(req.getMessage().substring(100), req.getSession(), req.getNick()));
                data = "PRIVMSG " + req.getNick() + " :" + req.getMessage().substring(0, 100) + "\r\n";
            } else {
                data = "PRIVMSG " + req.getNick() + " :" + req.getMessage() + "\r\n";
            }
        } else {
            data = req.getMessage();
            if (!data.endsWith("\r\n")) {
                data += "\r\n";
            }
        }
        byte[] dataArray = data.getBytes();
        ByteBuffer buff = ByteBuffer.allocate(dataArray.length);
        buff.put(dataArray);
        buff.flip();
        int amount = 0;
        try {
            amount = socChannel.write(buff);
        } catch (IOException e) {
            e.printStackTrace();
            session.disconnected(e);
        }
        if (session.getState() == State.DISCONNECTED) {
            return amount;
        }
        fireWriteEvent(req);
        System.out.println("Wrote " + amount + " " + req.getType() + " " + bursts);
        return amount;
    }
