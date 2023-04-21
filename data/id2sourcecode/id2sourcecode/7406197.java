    public static byte[] readFile(String filename) throws IOException {
        File file = new File(filename);
        java.io.FileInputStream f = new java.io.FileInputStream(file);
        java.nio.channels.FileChannel ch = f.getChannel();
        byte[] result = null;
        try {
            byte[] barray = new byte[1024 * 8];
            java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(barray);
            int nRead;
            int length = (int) file.length();
            result = new byte[length];
            int currentPos = 0;
            int totalRead = 0;
            while ((nRead = ch.read(bb)) != -1) {
                totalRead += nRead;
                bb.rewind();
                bb.get(result, currentPos, nRead);
                currentPos += nRead;
                bb.rewind();
            }
        } finally {
            try {
                ch.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                f.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
