    @Override
    public void run() {
        Logger.getLogger("fslogger").info("File Reader started...");
        GetRequest request;
        while (true) {
            try {
                request = (GetRequest) requests.take();
                System.out.println("ok");
                File file = new File(request.getFile());
                int fileNumber = request.getFileId();
                MappedByteBuffer bb = new FileInputStream(file).getChannel().map(MapMode.READ_ONLY, 0, file.length());
                int l = 0;
                DataReply dr;
                while (bb.hasRemaining()) {
                    if (bb.remaining() < 512) l = bb.remaining(); else l = 512;
                    byte[] data = new byte[l];
                    bb.get(data, 0, l);
                    dr = new DataReply(data, fileNumber);
                    request.getClientSession().addReply(dr);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
