    public void run() {
        for (; ; ) {
            try {
                Socket s = ss.accept();
                clientsAccepted++;
                System.out.println("client connection accepted!");
                System.out.println("total clients accepted = " + clientsAccepted);
                System.out.println("assigning ids: " + id + " - " + (id + idRange));
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeLong(id);
                id += idRange;
                dos.writeLong(id);
                System.out.println("creating new server writer thread...");
                ServerWriterThread swt = new ServerWriterThread(s);
                try {
                    socketSem.acquire();
                    sockets.put(s, swt);
                    socketSem.release();
                } catch (InterruptedException e) {
                }
                new Thread(swt).start();
                System.out.println("creating new server receiver thread...");
                new Thread(new ServerReceiverThread(s, server)).start();
                System.out.println("ready!");
                System.out.println("--------------------------------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
