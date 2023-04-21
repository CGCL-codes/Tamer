    @Override
    public void run() {
        try {
            clientManager.connect(host, Integer.parseInt(port), true);
            clientManager.login(username, password);
        } catch (SocketException e) {
            return;
        } catch (ariannexpTimeoutException e) {
            System.out.println("textClient can't connect to Midhedava server. Server is down?");
            return;
        }
        boolean cond = true;
        while (cond) {
            clientManager.loop(0);
            try {
                sleep(100);
            } catch (InterruptedException e) {
            }
            ;
        }
        while (clientManager.logout() == false) {
            ;
        }
    }
