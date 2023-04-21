    protected boolean recursiveTransfer(String path, boolean topDir) throws IOException {
        ReadableByteChannel channel = streamIn.getChannel();
        ByteBuffer buffer = streamIn.getBuffer();
        CharsetDecoder decoder = streamIn.getDecoder();
        boolean reached = false;
        buffer.clear();
        buffer.limit(8);
        while (buffer.hasRemaining()) channel.read(buffer);
        buffer.flip();
        int type = buffer.getInt();
        int len = buffer.getInt();
        if (type == TYPE_NULL) {
            reached = true;
        } else {
            buffer.clear();
            buffer.limit(len);
            while (buffer.hasRemaining()) channel.read(buffer);
            buffer.flip();
            String name = decoder.decode(buffer).toString();
            if (type == TYPE_DIR) {
                String dirPath = path + File.separator + name;
                if (topDir) this.path = dirPath;
                File target = new File(dirPath);
                if (!target.exists()) target.mkdirs();
                while (!recursiveTransfer(dirPath, false)) ;
            } else if (type == TYPE_FILE) {
                buffer.clear();
                buffer.limit(8);
                while (buffer.hasRemaining()) channel.read(buffer);
                buffer.flip();
                long size = buffer.getLong();
                FileOutputStream fileOut = new FileOutputStream(path + File.separator + name);
                FileChannel fileChannel = fileOut.getChannel();
                long pos = 0, rem = size;
                showProgress(name, pos, size);
                while (rem > 0) {
                    long transferred = fileChannel.transferFrom(channel, pos, rem);
                    if (transferred <= 0) {
                        logging.warning(LOG_NAME, "Unexpected termination of transfer");
                        break;
                    }
                    pos += transferred;
                    rem -= transferred;
                    showProgress(name, pos, size);
                }
                fileChannel.close();
            }
        }
        return reached;
    }
