    public int getMessageSize(int index) throws IOException, ArrayIndexOutOfBoundsException {
        long position = getMessagePosition(index);
        long size;
        if (index < messagePositions.length - 1) size = messagePositions[index + 1] - position; else size = getChannel().size() - position;
        return (int) size;
    }
