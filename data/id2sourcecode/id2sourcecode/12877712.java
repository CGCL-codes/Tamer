    protected void recursiveTransfer(File target, WritableByteChannel channel, ByteBuffer buffer, CharsetEncoder encoder) throws IOException {
        String name = target.getName();
        long pos, rem;
        if (target.isDirectory()) {
            buffer.clear();
            buffer.putInt(TYPE_DIR);
            buffer.putInt(0);
            encoder.reset();
            encoder.encode(CharBuffer.wrap(name), buffer, true);
            encoder.flush(buffer);
            int mark = buffer.position();
            int len = buffer.position() - 8;
            buffer.position(4);
            buffer.putInt(len);
            buffer.position(mark);
            buffer.flip();
            while (buffer.hasRemaining()) channel.write(buffer);
            for (File f : target.listFiles()) {
                recursiveTransfer(f, channel, buffer, encoder);
            }
            buffer.clear();
            buffer.putInt(TYPE_NULL);
            buffer.putInt(0);
            buffer.flip();
            while (buffer.hasRemaining()) channel.write(buffer);
        } else {
            FileInputStream fileIn = new FileInputStream(target);
            FileChannel fileChannel = fileIn.getChannel();
            long size = fileChannel.size();
            buffer.clear();
            buffer.putInt(TYPE_FILE);
            buffer.putInt(0);
            encoder.reset();
            encoder.encode(CharBuffer.wrap(name), buffer, true);
            encoder.flush(buffer);
            int mark = buffer.position();
            int len = buffer.position() - 8;
            buffer.position(4);
            buffer.putInt(len);
            buffer.position(mark);
            buffer.putLong(size);
            buffer.flip();
            while (buffer.hasRemaining()) channel.write(buffer);
            pos = 0;
            rem = size;
            if (!FALLBACK_2GB || rem < SIZE_2GB) {
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
