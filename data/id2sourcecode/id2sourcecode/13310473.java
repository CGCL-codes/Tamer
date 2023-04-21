    public static void writeBir(BioAPI_BIR bir, String filename) throws IOException {
        FileOutputStream out = null;
        FileChannel channel = null;
        try {
            out = new FileOutputStream(filename);
            channel = out.getChannel();
            channel.write(BioAPI.getBirHeaderByteBuffer(bir));
            channel.write(BioAPI.getBirDataByteBuffer(bir));
            channel.write(BioAPI.getBirSignatureByteBuffer(bir));
            channel.close();
            channel = null;
            out.close();
            out = null;
        } catch (IOException e) {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e1) {
                }
                channel = null;
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                }
                out = null;
            }
            IOException b = new IOException("Error writing BIR to " + filename);
            b.initCause(e);
            throw b;
        }
    }
