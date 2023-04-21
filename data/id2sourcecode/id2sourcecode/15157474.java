    public void send(InputStream data) throws IOException {
        DataOutputStream dataOutput = new DataOutputStream(this.transport.getOutputStream());
        DataInputStream dataInput = new DataInputStream(this.transport.getInputStream());
        BufferedInputStream bufferedData = new BufferedInputStream(data);
        byte[] b = new byte[BLOCK_SIZE];
        int readedLength;
        while ((readedLength = bufferedData.read(b, 0, BLOCK_SIZE)) != -1) {
            semaphore.startTransaction();
            synchronized (semaphore.getTransactionKey()) {
                dataOutput.writeInt(readedLength);
                dataOutput.write(b, 0, readedLength);
                dataOutput.flush();
                dataInput.readInt();
            }
            semaphore.endTransaction();
        }
        semaphore.startTransaction();
        synchronized (semaphore.getTransactionKey()) {
            dataOutput.writeInt(-1);
            dataOutput.flush();
        }
        semaphore.lastTransaction();
    }
