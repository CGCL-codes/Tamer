    protected void transferTo(FileChannel channel) throws IOException {
        ReadableByteChannel sockChannel = streamIn.getChannel();
        long pos = 0, rem = size;
        showProgress(name, pos, size);
        while (rem > 0) {
            long transferred = channel.transferFrom(sockChannel, pos, rem);
            if (transferred <= 0) {
                logging.warning(LOG_NAME, "Unexpected termination of transfer");
                break;
            }
            pos += transferred;
            rem -= transferred;
            showProgress(name, pos, size);
        }
    }
