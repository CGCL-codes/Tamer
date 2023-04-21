    public void handle(Event event) {
        if (event instanceof BroadcastSendableEvent) {
            if (event.getDir() == Direction.DOWN) handleDownBroadcastSendableEvent((BroadcastSendableEvent) event); else handleUpBroadcastSendableEvent((BroadcastSendableEvent) event);
        } else if (event instanceof P2PInitEvent) {
            this.myID = ((P2PInitEvent) event).getLocalPeer();
            try {
                StringTokenizer st = new StringTokenizer(this.myID.toString(), "::");
                st.nextToken();
                this.file = new PrintStream(st.nextToken().replace("/", "") + "_" + st.nextToken() + ".txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                this.file = System.out;
            }
            this.channel = event.getChannel();
        }
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }
