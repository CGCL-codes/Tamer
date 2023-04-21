    public void record(NoteEvent e) {
        PO.p("going through the parts");
        for (int i = 0; i < this.lpartNum; i++) {
            PO.p("i = " + i + " chan = " + e.getChannel());
            if (lpartArr[i].getPart().getChannel() == e.getChannel()) {
                PO.p("in channel");
                e.getNotePhr().setStartTime(e.getNotePhr().getStartTime() % this.tc.getScopeParam().getValue());
                PO.p(e.getNotePhr().toString());
                lpartArr[i].getPart().add(e.getNotePhr());
            }
        }
    }
