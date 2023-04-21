    public static ByteBuffer getBuffer(int type, String toData) throws IOException {
        ByteBuffer buffer = null;
        if (type == FILE) {
            FileInputStream input = new FileInputStream(toData);
            FileChannel channel = input.getChannel();
            int fileLength = (int) channel.size();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
        } else if (type == STRING) {
            String xml = "<div>\n<div><p><img src='img1' /> <a		\n	 href='a1'>kjg <a			href='a2'>\t</a></a></p></div></div>";
            buffer = ByteBuffer.wrap(xml.getBytes());
        }
        return buffer;
    }
