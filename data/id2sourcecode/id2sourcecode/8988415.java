    private void writeFile(String res) throws IOException {
        InputStream is = getClass().getResourceAsStream(res);
        if (is == null) {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(res);
        }
        if (is == null) throw new InvalidInputDataException("Can't find resource " + res, "resource " + res);
        ReadableByteChannel input = Channels.newChannel(is);
        String fPathEnd = res;
        if (!fPathEnd.startsWith(File.separator)) fPathEnd = File.separator + fPathEnd;
        File f = new File(resultDir.getPath() + fPathEnd);
        f.getParentFile().mkdirs();
        FileChannel output = new FileOutputStream(f).getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        Charset outCharset = Charset.forName((String) settings.get("resultEncoding"));
        int bytesRead;
        while ((bytesRead = input.read(buffer)) >= 0) {
            buffer.flip();
            CharBuffer c = Charset.forName("UTF-8").decode(buffer);
            ByteBuffer buffer1 = outCharset.encode(c);
            output.write(buffer1);
            buffer.clear();
        }
        input.close();
        output.close();
    }
