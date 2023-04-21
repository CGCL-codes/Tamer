    public void decodeFrame() throws Exception {
        int sb, gr, ch, nb;
        int nch = header.getChannels();
        int bound = (header.getMode() == 1) ? ((header.getModeExtension() + 1) * 4) : 32;
        int slots = header.getMainDataSlots();
        bs.append(slots);
        int maindata_begin = bs.getBytePos();
        for (sb = 0; sb < bound; sb++) for (ch = 0; ch < nch; ++ch) {
            nb = bs.getBits9(4);
            if (nb == 15) throw new Exception("decodeFrame()->nb=15");
            allocation[ch][sb] = (byte) ((nb != 0) ? (nb + 1) : 0);
        }
        for (sb = bound; sb < 32; sb++) {
            nb = bs.getBits9(4);
            if (nb == 15) throw new Exception("decodeFrame()->nb=15");
            allocation[0][sb] = (byte) ((nb != 0) ? (nb + 1) : 0);
        }
        for (sb = 0; sb < 32; sb++) for (ch = 0; ch < nch; ch++) if (allocation[ch][sb] != 0) scalefactor[ch][sb] = (byte) bs.getBits9(6);
        for (gr = 0; gr < 12; gr++) {
            for (sb = 0; sb < bound; sb++) for (ch = 0; ch < nch; ch++) {
                nb = allocation[ch][sb];
                if (nb == 0) syin[ch][sb] = 0; else syin[ch][sb] = requantization(ch, sb, nb);
            }
            for (sb = bound; sb < 32; ++sb) if ((nb = allocation[0][sb]) != 0) for (ch = 0; ch < nch; ++ch) syin[ch][sb] = requantization(ch, sb, nb); else for (ch = 0; ch < nch; ++ch) syin[ch][sb] = 0;
            for (ch = 0; ch < nch; ch++) filter.synthesisSubBand(syin[ch], ch);
        }
        int discard = slots + maindata_begin - bs.getBytePos();
        bs.skipBytes(discard);
    }
