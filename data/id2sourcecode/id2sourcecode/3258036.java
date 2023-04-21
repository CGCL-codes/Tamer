    public void setPart(Part p) {
        pnpanel.setPart(p);
        this.part = p;
        inst.setValue(p.getInstrument());
        chan.setValue(p.getChannel());
    }
