    private void xferSegment(FileChannel source, Segment segment) throws IOException {
        long len = _channel.transferFrom(source, segment.getPosition(), segment.getLength());
        if (len != segment.getLength() && len != source.size()) {
            System.err.println("Did not transfer as many bytes(" + len + ") " + "as expected(" + segment.getLength() + ")!  Trying it the slow way.");
            ByteBuffer buffer = ByteBuffer.allocateDirect(segment.getLength());
            source.read(buffer);
            buffer.flip();
            _channel.write(buffer, segment.getPosition());
            _channel.force(true);
            segment.initBuffers(true);
        }
    }
