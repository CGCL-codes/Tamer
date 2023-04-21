    public final long appendMessage(MboxFile source, int index) throws IOException {
        long srcpos = source.getMessagePosition(index);
        int srcsize;
        if (index < source.messagePositions.length - 1) {
            srcsize = (int) (source.messagePositions[index + 1] - srcpos);
        } else {
            srcsize = (int) (source.getChannel().size() - srcpos);
        }
        return appendMessage(source, srcpos, srcsize);
    }
