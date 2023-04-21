            public final int call() {
                final short rm = Fetchb();
                if (rm >= 0xc0) {
                    int op3 = Fetchb() & 0x1F;
                    if (op3 != 0) {
                        Reg r = Modrm.GetEArd[rm];
                        r.dword = DSHRD(Modrm.Getrd[rm].dword, op3, r.dword);
                    }
                } else {
                    int eaa = getEaa(rm);
                    int op3 = Fetchb() & 0x1F;
                    if (op3 != 0) Memory.mem_writed(eaa, DSHRD(Modrm.Getrd[rm].dword, op3, Memory.mem_readd(eaa)));
                }
                return HANDLED;
            }
