    public void initParts(LScore startScore, LScore endScore, LScore mscore, int i, int morphLength) {
        this.lpmorph[0] = mscore.getLPart(i * 2);
        this.lpmorph[1] = mscore.getLPart(i * 2 + 1);
        if (startScore.size() > i) {
            lfrom = startScore.getLPart(i);
            from = lfrom.getPart();
        }
        if (endScore.size() > i) {
            lto = endScore.getLPart(i);
            to = lto.getPart();
        }
        if (endScore.size() <= i) {
            to = new Part();
            to.setChannel(from.getChannel());
            to.setInstrument(from.getInstrument());
            to.setIdChannel(from.getIdChannel());
            to.setVolume(0);
            lto = lfrom;
        } else if (startScore.size() <= i) {
            from = new Part();
            from.setChannel(to.getChannel());
            from.setInstrument(to.getInstrument());
            from.setIdChannel(to.getIdChannel());
            from.setVolume(0);
            lfrom = lto;
        }
        this.fromTo[0] = lfrom;
        this.fromTo[1] = lto;
        morphStruc().initParts(lfrom, lto, lpmorph, morphLength);
    }
