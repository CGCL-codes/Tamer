    public void receive(SocketSession session) {
        SocketChannel channel = session.getChannel();
        try {
            int l = session.readInt();
            log.info("FileName length: " + l);
            String fileName = new String(session.read(l));
            log.info("FileName: " + fileName);
            long size = session.readLong();
            log.info("File size: " + size);
            File f = new File(fileName);
            FileSizeTracker fst = new FileSizeTracker(f, size, receiver);
            fst.start();
            long bytesWritten = session.receiveFileAndSaveTo(size, f);
            log.info("Bytes written: " + bytesWritten);
            fst.run = false;
            log.info("FileSysRemote receive complete");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
