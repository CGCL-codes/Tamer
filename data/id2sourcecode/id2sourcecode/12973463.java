    public static void main(String[] args) throws IOException, MarshallException {
        File file = new File(args[0]);
        EpgBinaryMarshaller binary = new EpgBinaryMarshaller();
        EpgXmlMarshaller xml = new EpgXmlMarshaller();
        FileChannel channel = new FileInputStream(file).getChannel();
        ByteBuffer buf = ByteBuffer.allocate((int) channel.size());
        channel.read(buf);
        Epg epg = binary.unmarshall(buf.array());
        System.out.println(new String(xml.marshall(epg)));
    }
