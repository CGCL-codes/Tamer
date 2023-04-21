    public void transferTo(WritableByteChannel channel, ByteBuffer buffer, CharsetEncoder encoder) throws IOException {
        if (isDir) {
            recursiveTransfer(new File(path), channel, buffer, encoder);
        } else {
            FileInputStream fileIn = new FileInputStream(path);
            FileChannel fileChannel = fileIn.getChannel();
            long pos = 0, rem = fileChannel.size();
            if (!FALLBACK_2GB || rem <= SIZE_2GB) {
                while (rem > 0) {
                    long transferred = fileChannel.transferTo(pos, rem, channel);
                    if (transferred <= 0) {
                        logging.warning(LOG_NAME, "Unexpected termination of transfer");
                        break;
                    }
                    pos += transferred;
                    rem -= transferred;
                }
            } else {
                ByteBuffer buf = ByteBuffer.allocate(8192);
                while (fileChannel.read(buf) > 0) {
                    buf.flip();
                    while (buf.hasRemaining()) channel.write(buf);
                    buf.clear();
                }
            }
            fileChannel.close();
        }
    }
