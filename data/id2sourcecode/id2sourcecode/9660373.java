    private void copy(URL xslUrl, File file) throws IOException {
        InputStream in = null;
        ReadableByteChannel inchannel = null;
        FileOutputStream out = null;
        FileChannel outchannel = null;
        try {
            in = xslUrl.openStream();
            inchannel = Channels.newChannel(in);
            out = new FileOutputStream(file);
            outchannel = out.getChannel();
            java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(8092);
            int read;
            do {
                byteBuffer.clear();
                read = inchannel.read(byteBuffer);
                byteBuffer.flip();
                if (byteBuffer.remaining() > 0) {
                    outchannel.write(byteBuffer);
                }
            } while (read != -1);
        } finally {
            if (in != null) in.close();
            if (inchannel != null) inchannel.close();
            if (out != null) out.close();
            if (outchannel != null) outchannel.close();
        }
    }
