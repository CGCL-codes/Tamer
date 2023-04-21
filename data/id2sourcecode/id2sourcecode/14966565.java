    private void partIntoSeq(Part p, Sequence seq) {
        pchan = p.getChannel() - 1;
        if (pchan < 0) {
            try {
                Exception e = new Exception("part channel mus be between > 0 ");
                e.fillInStackTrace();
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ccenvIntoSeq(pchan, p.getCCEnvs(), seq);
        if (p.getSize() == 0) {
            return;
        }
        if (instruments[pchan] != p.getInstrument()) {
            if (bankStylePrgChg) {
                sendBankPrgChng(p.getInstrument(), pchan, refnumCount - 1);
            } else {
                sendPrgChng(p.getInstrument(), pchan);
            }
            instruments[pchan] = p.getInstrument();
        }
        if (volumes[pchan] != p.getVolume()) {
            sendVol(pchan, p.getVolume());
            volumes[pchan] = p.getVolume();
        }
        for (int i = 0; i < p.size(); i++) {
            phraseIntoSeq(p.getPhrase(i), pchan, seq);
        }
    }
