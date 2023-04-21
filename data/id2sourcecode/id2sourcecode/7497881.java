    public static int parentInsertUpdate(Seg seg, int topId, int level, byte[] nkeyStr, int nkLen, int nId) {
        int[] pkt = new int[pktSize];
        {
            Ent ent = findEnt(getEnt(seg, topId, accnone), 1 + (level), -1, nkeyStr, nkLen);
            Ent xent = null;
            boolean screwCase_P = false;
            byte[] blkidstr = new byte[4];
            byte[] blk = null;
            if (a2b(ent)) {
                long2str(blkidstr, 0, nId);
                if (ents_EntUpdateAccess_P(ent, accread, accwrite)) {
                    ent = chainFind(ent, accwrite, nkeyStr, nkLen, pkt);
                    blk = ent_Blk(ent);
                } else {
                    releaseEnt(ent, accread);
                    ent = null;
                }
                if (a2b(ent) && atSplitKeyPos_P(blk, pkt_MatchPos(pkt))) {
                    screwCase_P = (true);
                    xent = nextNonemptyEnt(ent_Seg(ent), blk_NxtId(blk));
                    if (!(a2b(xent))) dprintf(">>>>ERROR<<<< No next key found for index insert " + (seg_Id(ent_Seg(ent))) + ":" + (blk_Id(blk)) + "\n");
                }
                if (!(deferInsertUpdates_P) && a2b(ent) && (!(screwCase_P) || a2b(xent)) && a2b(chainPut(ent, nkeyStr, nkLen, blkidstr, 4, pkt, xent, wcbSar))) return 1; else {
                    dprintf("WARNING: " + ("parentInsertUpdate") + ": couldn't update parent n-id=" + (nId) + " nk-len=" + (nkLen) + "\n");
                    deferredInserts = 1 + (deferredInserts);
                    if (a2b(ent)) releaseEnt(ent, accwrite);
                    return 0;
                }
            } else return 0;
        }
    }
