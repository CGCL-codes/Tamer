    public Id3v1Tag getID3v1() {
        if (v1tag == null && iD3v1parsed == false) {
            FileChannel chan;
            try {
                chan = new FileInputStream(theOriginal).getChannel();
                if (chan.size() < 128) return null;
                chan.position(chan.size() - 128);
                ByteBuffer mybyte = ByteBuffer.allocate(128);
                chan.read(mybyte);
                mybyte.flip();
                try {
                    v1tag = new Id3v1Tag(mybyte);
                } catch (InstantiationException e) {
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return v1tag;
    }
