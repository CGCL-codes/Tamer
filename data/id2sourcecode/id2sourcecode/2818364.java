    private byte[] getMP3() {
        try {
            FileInputStream temp = new FileInputStream(getFilePath());
            FileChannel tChan = temp.getChannel();
            ByteBuffer byBu = ByteBuffer.allocate((int) ((int) tChan.size() - MP3beginning));
            tChan.read(byBu, MP3beginning);
            return byBu.array();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[] {};
    }
