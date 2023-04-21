    public Id3v2Tag getID3v2() {
        if (v2tag == null && iD3v2parsed == false) {
            iD3v2parsed = true;
            FileChannel theStream;
            try {
                theStream = new FileInputStream(theOriginal).getChannel();
                MP3beginning = theStream.size();
                try {
                    v2tag = new Id3v2Tag(theStream);
                } catch (InstantiationException e) {
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return v2tag;
    }
