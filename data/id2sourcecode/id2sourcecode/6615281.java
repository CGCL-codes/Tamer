    public CharSequence getMessage(final int index) throws IOException {
        long position = getMessagePosition(index);
        long size;
        if (index < messagePositions.length - 1) {
            size = messagePositions[index + 1] - position;
        } else {
            size = getChannel().size() - position;
        }
        return decoder.decode(getChannel().map(FileChannel.MapMode.READ_ONLY, position, size));
    }
