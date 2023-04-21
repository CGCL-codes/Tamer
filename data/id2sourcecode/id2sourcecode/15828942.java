    public void run() {
        int nameTrnId;
        NameServicePacket response;
        while (thread == Thread.currentThread()) {
            in.setLength(RCV_BUF_SIZE);
            try {
                socket.setSoTimeout(closeTimeout);
                socket.receive(in);
            } catch (IOException ioe) {
                tryClose();
                break;
            }
            if (DebugFile.trace) DebugFile.writeln("NetBIOS: new data read from socket");
            nameTrnId = NameServicePacket.readNameTrnId(rcv_buf, 0);
            response = (NameServicePacket) responseTable.get(new Integer(nameTrnId));
            if (response == null || response.received) {
                continue;
            }
            synchronized (response) {
                response.readWireFormat(rcv_buf, 0);
                response.received = true;
                response.notify();
            }
        }
    }
