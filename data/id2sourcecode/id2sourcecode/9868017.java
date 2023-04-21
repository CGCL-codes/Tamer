    public synchronized int append(int limit) throws IOException {
        int result = 0;
        while (!dc.doClose() && result < limit) {
            intermedianBuffer.position(0);
            intermedianBuffer.limit(Math.min(limit - result, intermedianBufferSize));
            int bytes = dc.getChannel().read(intermedianBuffer);
            if (bytes > 0) {
                result += bytes;
                append(intermedianBuffer, bytes);
            } else if (bytes == -1) {
                dc.addReceivedBytesNum(result);
                log.fine("detect end of stream on channel.read(). close connection " + dc.getConnectionNumber());
                dc.close();
                break;
            } else {
                break;
            }
        }
        return result;
    }
