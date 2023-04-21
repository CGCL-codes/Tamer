    public String readOnce(String filepath) throws IOException {
        CharBuffer charBuffer = null;
        @Cleanup FileChannel file_channel = new FileInputStream(new File(filepath)).getChannel();
        int ch_size = (int) file_channel.size();
        MappedByteBuffer mapbyte_buffer = file_channel.map(FileChannel.MapMode.READ_ONLY, 0, ch_size);
        charBuffer = decoder.decode(mapbyte_buffer);
        return charBuffer.toString();
    }
