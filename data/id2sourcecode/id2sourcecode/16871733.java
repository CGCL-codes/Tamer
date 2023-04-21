    public static int btScan(Han han, int operation, byte[] kstr1, int len1, byte[] kstr2, int len2, java.lang.reflect.Method func, int[] longTab, int[] respkt, int blkLimit) {
        LbtScan: while (true) {
            {
                int[] pkt = new int[pktSize];
                int[] opkt = new int[pktSize];
                Ent ent = null;
                byte[] vstr = new byte[0x100];
                int accmode = ((operation) == (countScan) ? accread : accwrite);
                int result = success;
                if ((len1) < -2) {
                    dprintf(">>>>ERROR<<<< " + ("btScan") + ": bad length string1 " + (len1) + "\n");
                    return argerr;
                } else if ((len2) < -1) {
                    dprintf(">>>>ERROR<<<< " + ("btScan") + ": bad length string2 " + (len2) + "\n");
                    return argerr;
                } else if ((operation) == (modifyScan) && !(a2b(func))) {
                    dprintf(">>>>ERROR<<<< " + ("btScan") + ": MODIFY-SCAN requires func be specified\n");
                    return argerr;
                } else {
                    ent = chainFindEnt(han, accmode, kstr1, len1, pkt);
                    if (a2b(ent) && a2b(blk_FindPos(ent_Blk(ent), kstr2, len2, opkt))) {
                        if ((operation) == (countScan)) {
                            Ent nent = allocateEnt();
                            entCopy(nent, ent);
                            releaseEnt(ent, accmode);
                            result = chainScan(nent, operation, pkt, opkt, kstr1, func, longTab, vstr, respkt, han_Wcb(han));
                            recycleEnt(nent);
                        } else {
                            result = chainScan(ent, operation, pkt, opkt, kstr1, func, longTab, vstr, respkt, han_Wcb(han));
                            releaseEnt(ent, accmode);
                            if ((result) > 0) {
                                result = btPut(han, kstr1, pkt_SkeyLen(respkt), vstr, result);
                                if ((result) == (success)) {
                                    pkt_SetSkeyCount(respkt, (pkt_SkeyCount(respkt)) + 1);
                                    pkt_SetSkeyLen(respkt, incrementString(kstr1, pkt_SkeyLen(respkt), 0x100));
                                    result = notpres;
                                }
                            }
                        }
                        if ((result) == (notpres) && 0 != (blkLimit)) {
                            len1 = pkt_SkeyLen(respkt);
                            blkLimit = (blkLimit) - 1;
                            continue LbtScan;
                        } else return result;
                    } else {
                        if (a2b(ent)) releaseEnt(ent, accmode);
                        remFct = 1 + (remFct);
                        return unkerr;
                    }
                }
            }
        }
    }
