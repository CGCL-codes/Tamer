    public void purge(int[] messageNumbers) throws IOException {
        if (null == messageNumbers) return;
        if (0 == messageNumbers.length) return;
        getMessagePositions();
        if (null == messagePositions) return;
        if (0 == messagePositions.length) return;
        final int total = messagePositions.length;
        final int count = messageNumbers.length;
        int size;
        long next, append;
        boolean perform;
        ByteBuffer messageBuffer;
        byte[] byBuffer = null;
        log.debug("MboxFile.purge(" + String.valueOf(count) + " of " + String.valueOf(total) + ")");
        FileChannel channel = getChannel();
        append = 0;
        for (int index = 0; index < total; index++) {
            perform = false;
            for (int d = 0; d < count && !perform; d++) if (messageNumbers[d] == index) perform = true;
            if (index < total - 1) {
                next = messagePositions[index + 1];
                size = (int) (messagePositions[index + 1] - next);
            } else {
                next = -1l;
                size = 0;
            }
            if (perform) {
                messageBuffer = channel.map(FileChannel.MapMode.READ_WRITE, next, size);
                if (byBuffer == null) byBuffer = new byte[size]; else if (byBuffer.length < size) byBuffer = new byte[size];
                messageBuffer.get(byBuffer, 0, size);
                channel.position(append);
                channel.write(ByteBuffer.wrap(byBuffer));
                append += size;
            } else {
                append += size;
            }
        }
        log.debug("FileChannel.truncate(" + String.valueOf(append) + ")");
        channel.truncate(append);
        messagePositions = null;
    }
