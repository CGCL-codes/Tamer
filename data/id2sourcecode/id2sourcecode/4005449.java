    public void copyAttributes(Part i) {
        i.setInstrument(this.getInstrument());
        i.setChannel(this.getChannel());
        i.setIdChannel(this.getIdChannel());
        i.setTitle(this.getTitle());
        i.setTempo(this.tempo);
        i.setVolume(this.volume);
        i.setPoints(this.points);
        i.setTime(this.time);
        i.setTimeIndex(this.timeIndex);
        i.setMyScore(this.getMyScore());
    }
