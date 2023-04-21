    protected void openFile() throws IOException {
        channel = new FileInputStream(file).getChannel();
        if (null != encoding) {
            buffer = Charset.forName(encoding).newDecoder().decode(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
        } else {
            buffer = Charset.defaultCharset().newDecoder().decode(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
        }
    }
