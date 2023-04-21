    public static Ent nextNonemptyEnt(Seg seg, int blknum) {
        if ((blknum) <= 0) return null; else {
            Ent xent = getEnt(seg, blknum, accread);
            Lloop: while (true) {
                if (a2b(xent)) ents_EntUpdateAccess_P(xent, accread, accwrite);
                if (!(a2b(xent))) return null; else if (!(blkEmpty_P(ent_Blk(xent)))) return xent; else if (0 == (blk_NxtId(ent_Blk(xent)))) {
                    releaseEnt(xent, accwrite);
                    return null;
                } else {
                    xent = switchEnt(xent, accwrite, blk_NxtId(ent_Blk(xent)), accwrite);
                    continue Lloop;
                }
            }
        }
    }
