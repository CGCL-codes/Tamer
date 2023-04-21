    public ExtractorThread(InfoStatusInterface parent, File f, boolean owrite) {
        isi = parent;
        root = new BasicAlbum(f);
        overwrite = owrite;
        keepSearching = true;
    }
