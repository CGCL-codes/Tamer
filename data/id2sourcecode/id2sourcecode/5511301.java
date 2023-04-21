    protected void connect(String ipaddr, int port) throws UnknownHostException, IOException {
        System.out.println("Creating Socket to " + ipaddr + "...");
        sock = new Socket(ipaddr, port);
        System.out.println("Socket created");
        reader = new ReaderThread();
        reader.start();
        System.out.println("Moos reader started");
        MoosMessage mm = new MoosMessage();
        mm.messageId = 0;
        mm.messageType = MoosMessage.DATA;
        mm.dataType = MoosMessage.STRING;
        mm.s = clientName;
        System.out.println("Moos connetc: adding first packet");
        MoosPacket packet = new MoosPacket();
        packet.addMessage(mm);
        System.out.println("Moos connetc: sending first packet");
        DataOutputStream douts = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
        packet.write(douts);
        douts.flush();
        System.out.println("Moos connetc: starting Writer thread");
        writer = new WriterThread();
        writer.start();
    }
