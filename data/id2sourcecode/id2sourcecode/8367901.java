    public void connect(String network, int port) {
        try {
            this.sock = new Socket(network, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            IRCEvent result;
            EventWaiter waiter = new EventWaiter() {

                public boolean check(IRCEvent e) {
                    return e.isType(EventType.REPLY) && (e.replyCode == ReplyCode.RPL_MYINFO || e.replyCode == ReplyCode.ERR_NICKNAMEINUSE);
                }
            };
            waiters.add(waiter);
            this.inputThread = new IncomingThread(incoming, reader);
            this.outputThread = new OutgoingThread(writer);
            this.parser = new ParserThread();
            outputThread.start();
            inputThread.start();
            parser.start();
            outputThread.sendImmediately(IRCCommand.nick(this.nick).toString());
            outputThread.sendImmediately(IRCCommand.user("person", "Gracenotes' bot", 0).toString());
            try {
                result = waiter.await(60 * 1000);
            } catch (Exception ex) {
                return;
            }
            if (result.replyCode == ReplyCode.ERR_NICKNAMEINUSE) throw new RuntimeException("Nick in use");
            connected = true;
        } catch (IOException ex) {
        }
    }
