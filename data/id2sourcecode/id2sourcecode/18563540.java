    public long transfer(final FileChannel dst, long position, long count) throws IOException {
        if (dst == null) {
            return 0;
        }
        if (this.completed) {
            return -1;
        }
        int lenRemaining = (int) (this.contentLength - this.len);
        long bytesRead;
        if (this.buffer.hasData()) {
            int maxLen = Math.min(lenRemaining, this.buffer.length());
            dst.position(position);
            bytesRead = this.buffer.read(dst, maxLen);
        } else {
            if (count > lenRemaining) {
                count = lenRemaining;
            }
            if (this.channel.isOpen()) {
                if (dst.size() < position) throw new IOException("FileChannel.size() [" + dst.size() + "] < position [" + position + "].  Please grow the file before writing.");
                bytesRead = dst.transferFrom(this.channel, position, count);
            } else {
                bytesRead = -1;
            }
            if (bytesRead > 0) {
                this.metrics.incrementBytesTransferred(bytesRead);
            }
        }
        if (bytesRead == -1) {
            this.completed = true;
            return -1;
        }
        this.len += bytesRead;
        if (this.len >= this.contentLength) {
            this.completed = true;
        }
        return bytesRead;
    }
