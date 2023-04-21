    public void accept(int acceptorID) throws IOException {
        try {
            File file = new File("fakeRequests.txt");
            if (!file.exists()) file = new File("modules/jetty/src/test/resources/fakeRequests.txt");
            if (!file.exists()) file = new File("src/test/resources/fakeRequests.txt");
            if (!file.exists()) file = new File("/tmp/fakeRequests.txt");
            if (!file.exists()) {
                System.err.println("No such file " + file);
                System.exit(1);
            }
            Thread.sleep(random.nextInt(50 * rate));
            ByteChannel channel = new FileInputStream(file).getChannel();
            RandomEndPoint gep = new RandomEndPoint(this, channel);
            try {
                while (gep.isOpen()) {
                    Thread.sleep(random.nextInt(10 * rate));
                    synchronized (gep) {
                        if (!gep.dispatched) {
                            gep.dispatched = true;
                            getThreadPool().dispatch(gep);
                        }
                    }
                }
            } finally {
                connectionClosed(gep._connection);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
