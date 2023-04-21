    public static boolean parentDeleteUpdate_P(Seg seg, int topId, int level, int oldId, byte[] keyStr, int kLen) {
        int[] pkt = new int[pktSize];
        int ans = -1;
        byte[] ansStr = new byte[4];
        {
            Ent ent = findEnt(getEnt(seg, topId, accnone), 1 + (level), -1, keyStr, kLen);
            if (!(a2b(ent))) ; else if (ents_EntUpdateAccess_P(ent, accread, accwrite)) {
                ent = chainFind(ent, accwrite, keyStr, kLen, pkt);
            } else {
                releaseEnt(ent, accread);
                ent = null;
            }
            if (a2b(ent)) {
                ans = del_ChainRem(ent, keyStr, kLen, ansStr, pkt, wcbSar);
                if ((ans) >= 0) if ((oldId) != (str2long(ansStr, 0))) dprintf(">>>>ERROR<<<< " + ("parentDeleteUpdate_P") + ": bad value " + (str2long(ansStr, 0)) + " in deleted down pointer " + (oldId) + " told\n");
                releaseEnt(ent, accwrite);
            }
            if (!((a2b(ent) || (ans) >= 0))) {
                dprintf("WARNING: " + ("parentDeleteUpdate_P") + " blk=" + (seg_Id(seg)) + ":" + (oldId) + ", level=" + (level) + ", key=" + (kLen) + "\n");
                return false;
            } else return (true);
        }
    }
