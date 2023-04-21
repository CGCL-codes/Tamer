    public LPart copyEmpty() {
        Part p = new Part(this.part.getTitle(), this.part.getInstrument(), this.part.getChannel());
        LPart l = new LPart();
        l.setPart(p);
        l.setTransformChain(this.getTransformChain().copy());
        l.getQuantise().setValue(this.getQuantise().getValue());
        l.getShuffle().setValue(this.getShuffle().getValue());
        l.getMuteModel().setSelected(this.mute.isSelected());
        l.getSoloModel().setSelected(this.solo.isSelected());
        l.setTonalManager(this.getTonalManager().copy());
        l.getTonalManager().addLPart(l);
        l.setDEPA(this.isDEPA());
        return l;
    }
