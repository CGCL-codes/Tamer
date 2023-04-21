    public void seek(long seekBytes) throws FileNotFoundException, IOException, InterruptedException {
        fileInput = new FileInputStream(file);
        fileChannel = fileInput.getChannel();
        try {
            fileChannel.position(seekBytes);
        } catch (ClosedByInterruptException e) {
            throw new InterruptedException();
        }
        bufferedInput = new BufferedInputStream(fileInput, size);
        dataInput = new DataInputStream(bufferedInput);
        offset = seekBytes;
        markOffset = -1;
        available = 0;
    }
