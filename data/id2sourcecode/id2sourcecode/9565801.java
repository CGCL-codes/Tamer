    BatchLog(BatchWriter writer, Object header, File dir, int fileSize) throws IOException {
        this.writer = writer;
        this.header = header;
        logFile = File.createTempFile("TX_RECOVERY_LOG", ".log", dir);
        os = new RandomAccessFile(logFile, "rw");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(header);
        byte[] bytes = baos.toByteArray();
        os.setLength(fileSize);
        os.writeInt(bytes.length);
        os.write(bytes);
        channel = os.getChannel();
        channel.force(true);
        topFp = channel.position();
        cleanUpLogFile();
    }
