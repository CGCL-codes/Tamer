            public final int call() {
                final short rm = Fetchb();
                int val = reg_ecx.dword & 0x1f;
                if (rm >= 0xc0) {
                    if (val != 0) {
                        Reg r = Modrm.GetEArd[rm];
                        r.dword = DSHRD(Modrm.Getrd[rm].dword, val, r.dword);
                    }
                } else {
                    int eaa = getEaa(rm);
                    if (val != 0) Memory.mem_writed(eaa, DSHRD(Modrm.Getrd[rm].dword, val, Memory.mem_readd(eaa)));
                }
                return HANDLED;
            }
