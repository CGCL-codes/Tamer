    public void run() {
        if (ok) {
            try {
                socket = new Socket("test.spaghettilearning.com", 9999);
                streamToServer = new DataOutputStream(socket.getOutputStream());
                playbackInputStream = new DataInputStream(socket.getInputStream());
                boolean opened = true;
                if (this.writeline) streamToServer.writeByte(0x00);
                if (this.readline) streamToServer.writeByte(0x01);
                streamToServer.writeByte((byte) username.length());
                streamToServer.write(username.getBytes());
                streamToServer.flush();
            } catch (Exception ex) {
                System.out.println("daemon: hardware problem : " + ex);
            }
            if (this.writeline) {
                transmitter = new Transmitter(this);
                transmitter.start();
                System.out.println("Transmitter started.");
            }
            if (this.readline) {
                reciever = new Reciever(this);
                reciever.start();
                System.out.println("Reciever started.");
            }
            while (running) {
                try {
                    sleep(100);
                } catch (Exception e) {
                }
            }
            this.reciever.running = false;
            this.transmitter.running = false;
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
    }
