    @Override
    public void run() {
        try {
            clientManager.connect(host, Integer.parseInt(port), tcp);
            clientManager.login(username, password);
            PostmanIRC postmanIRC = new PostmanIRC(host);
            postmanIRC.connect();
            postman = new Postman(clientManager, postmanIRC);
            postman.startThread();
        } catch (SocketException e) {
            System.err.println("Socket Exception");
            Runtime.getRuntime().halt(1);
            return;
        } catch (ariannexpTimeoutException e) {
            System.err.println("Cannot connect to Midhedava server. Server is down?");
            Runtime.getRuntime().halt(1);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace(System.err);
            Runtime.getRuntime().halt(1);
        }
        boolean cond = true;
        while (cond) {
            clientManager.loop(0);
            if ((lastPerceptionTimestamp > 0) && (lastPerceptionTimestamp + 10 * 1000 < System.currentTimeMillis())) {
                System.err.println("Timeout");
                Runtime.getRuntime().halt(1);
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
            }
        }
        long start = System.currentTimeMillis();
        while (clientManager.logout() == false) {
            if (start + 5000 < System.currentTimeMillis()) {
                Runtime.getRuntime().halt(2);
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
