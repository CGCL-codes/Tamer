    @Override
    public void run() {
        Logger.getLogger("fslogger").info("File Reader started... Fixed Thread pool of " + nbThreads + " threads");
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final GetRequest request = requests.take();
                executor.submit(new Runnable() {

                    @Override
                    public void run() {
                        String s = FileServer.getInstance().getRoot() + request.getFile();
                        File file = new File(s);
                        FileChannel fc = null;
                        MappedByteBuffer bb = null;
                        try {
                            fc = new FileInputStream(file).getChannel();
                            bb = fc.map(MapMode.READ_ONLY, request.getStart(), request.getLength());
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        int l = 0;
                        DataReply dr;
                        int fileNumber = request.getFileId();
                        request.getClientSession().addTask(fileNumber, file.length(), request.getStart(), request.getFile());
                        boolean last = false;
                        while (bb.hasRemaining() && request.getClientSession().isNotKilled(fileNumber)) {
                            if (bb.remaining() < 1380) {
                                l = bb.remaining();
                                last = true;
                            } else l = 1380;
                            byte[] data = new byte[l];
                            bb.get(data, 0, l);
                            dr = new DataReply(data, fileNumber, last);
                            try {
                                request.getClientSession().addReply(dr);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                        try {
                            fc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
