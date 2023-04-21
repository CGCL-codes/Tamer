    public void listen() {
        try {
            int port = 25;
            ServerSocket srv = new ServerSocket(port);
            int i = 0;
            log.debug("starting to listen on port" + port);
            while (i++ < 2) {
                Socket socket = srv.accept();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                System.out.println("got a req");
                bw.write("220 POPAnything SMTP Server is ready n kicking\r\n");
                bw.flush();
                SMTPService ss = new SMTPService();
                ss.setSocket(socket);
                ss.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
