    public static BioAPI_BIR readBir(String filename) throws IOException {
        BioAPI_BIR bir;
        FileInputStream in = null;
        FileChannel channel = null;
        try {
            in = new FileInputStream(filename);
            channel = in.getChannel();
            long channelSize = channel.size();
            ByteBuffer buffer = ByteBuffer.allocateDirect(16);
            channel.read(buffer);
            bir = new BioAPI_BIR();
            BioAPI.setBirHeader(bir, buffer);
            buffer = ByteBuffer.allocateDirect((int) (channelSize - channel.position()));
            channel.read(buffer);
            BioAPI.setBirData(bir, buffer);
            if (channel.position() != channelSize) {
                buffer = buffer.compact();
                BioAPI.setSignatureData(bir, buffer);
            }
            channel.close();
            channel = null;
            in.close();
            in = null;
        } catch (IOException e1) {
            if (channel != null) {
                try {
                    channel.close();
                    channel = null;
                } catch (IOException e2) {
                    channel = null;
                }
            }
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e2) {
                    in = null;
                }
            }
            throw e1;
        }
        return bir;
    }
