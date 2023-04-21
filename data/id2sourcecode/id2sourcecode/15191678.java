    protected void updateMorph(TickEvent e) {
        super.updateMorph(e);
        int len = Math.max(startScore.size(), endScore.size());
        for (int i = 0; i < len; i++) {
            initMorphParts(i);
            if (from.getIdChannel() != to.getIdChannel()) {
                System.out.println("must have same ID channel in updateMorph");
                PO.p("from chan = " + from.getChannel() + " id = " + from.getIdChannel());
                PO.p("to chan = " + to.getChannel() + " id = " + to.getIdChannel());
            }
            initContextMorphParams(this.lscore.getLPart(i * 2), this.lscore.getLPart(i * 2 + 1));
            morphResult = obtainMorphResult(i, from, to, this.mtracker.getPos(), morphParams, e);
            if (morphResult == null) {
                PO.p("morphResult null");
            }
            if (from.getChannel() != to.getChannel() && this.morphInst.getSelectedItem().equals("together")) {
                Part mrc1 = morphResult[0].copy();
                Part mrc2 = morphResult[1].copy();
                for (int j = 0; j < mrc2.size(); j++) {
                    morphResult[0].addPhrase(mrc2.getPhrase(j));
                }
                for (int j = 0; j < mrc1.size(); j++) {
                    morphResult[1].addPhrase(mrc1.getPhrase(j));
                }
            }
            morphVolume(morphResult[0], morphResult[1], from.getVolume(), to.getVolume());
            this.lscore.getLPart(2 * i).setPart(morphResult[0].copy());
            this.lscore.getLPart(2 * i + 1).setPart(morphResult[1].copy());
        }
    }
