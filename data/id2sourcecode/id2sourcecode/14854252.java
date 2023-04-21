    public ExtractorThread(InfoStatusInterface parent, Album alb, boolean owrite) {
        isi = parent;
        root = alb;
        overwrite = owrite;
        keepSearching = true;
    }
