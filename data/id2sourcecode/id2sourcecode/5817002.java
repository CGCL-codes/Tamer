    public static boolean del_DeleteBck(Ent ent) {
        byte[] blk = ent_Blk(ent);
        boolean win_P = !(del_DeferBlockDeletes_P);
        if (win_P) {
            ents_EntUpdateAccess_P(ent, accwrite, accnone);
            {
                Ent prent = prevBlkEnt(ent, blk_Level(blk));
                win_P = ents_EntUpdateAccess_P(ent, accnone, accwrite);
                if (win_P && a2b(prent)) win_P = ents_EntUpdateAccess_P(prent, accread, accwrite);
                win_P = win_P && 1 == (ent_Ref(ent));
                if (win_P) {
                    if (!(atRootLevel_P(ent_Seg(ent), blk))) {
                        int skeyPos = splitKeyPos(blk);
                        if (a2b(skeyPos)) {
                            int topNum = blk_TopId(blk);
                            Seg seg = ent_Seg(ent);
                            int level = blk_Level(blk);
                            byte[] keyStr = new byte[0x100];
                            int kLen = reconThisKey(blk, skeyPos, keyStr, 0, 0x100);
                            win_P = parentDeleteUpdate_P(seg, topNum, level, ent_Id(ent), keyStr, kLen);
                        }
                    }
                    win_P = win_P && 1 == (ent_Ref(ent));
                    if (win_P) {
                        if (a2b(prent)) {
                            blk_SetNxtId(ent_Blk(prent), blk_NxtId(blk));
                            ent_SetDty(prent, true);
                            ents_EntWrite(prent);
                        }
                        win_P = blkFree(ent);
                        if (!(win_P)) dprintf(">>>>ERROR<<<< " + ("blkDelete") + ": could not free " + (seg_Id(ent_Seg(ent))) + ":" + (ent_Id(ent)) + "\n");
                    }
                }
                if (a2b(prent)) releaseEnt(prent, ent_Acc(prent));
            }
        }
        if (win_P) {
            blockDeletes = (blockDeletes) + 1;
        } else {
            deferredDeletes = 1 + (deferredDeletes);
            dprintf("Can't delete block " + (ent_Id(ent)) + "\n");
        }
        return win_P;
    }
