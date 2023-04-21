    private void morphInst(Part[] tmorph) {
        if (tmorph[0].getChannel() != tmorph[1].getChannel() && this.morphInst.getSelectedItem().equals("together")) {
            Part mrc1 = tmorph[0].copy();
            Part mrc2 = tmorph[1].copy();
            for (int j = 0; j < mrc2.size(); j++) {
                Phrase sam = An.getSame(tmorph[0], mrc2.getPhrase(j));
                if (sam == null) {
                    tmorph[0].addPhrase(mrc2.getPhrase(j));
                }
            }
            for (int j = 0; j < mrc1.size(); j++) {
                Phrase sam = An.getSame(tmorph[1], mrc1.getPhrase(j));
                if (sam == null) {
                    tmorph[1].addPhrase(mrc1.getPhrase(j));
                }
            }
        }
    }
