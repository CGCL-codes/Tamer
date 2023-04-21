    public MP3File(File currentFile) {
        theOriginal = currentFile;
        try {
            FileChannel theStream = new FileInputStream(theOriginal).getChannel();
            MP3beginning = theStream.size();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
