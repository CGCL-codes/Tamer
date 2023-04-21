    public void run() {
        try {
            ss = new ServerSocket(port);
            System.out.println("Listening on " + ss + " and ready to write files");
            while (true) {
                Socket s = ss.accept();
                System.out.println("Connection from " + s);
                InputStream in = s.getInputStream();
                new Thread(new FileWorkerThread(in)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
