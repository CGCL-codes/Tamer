            public final int call() {
                final short rm = Fetchb();
                int which = (rm >> 3) & 7;
                if (rm >= 0xc0) {
                    int id = Fetchbs();
                    Reg r = Modrm.GetEArd[rm];
                    switch(which) {
                        case 0x00:
                            r.dword = ADDD(id, r.dword);
                            break;
                        case 0x01:
                            r.dword = ORD(id, r.dword);
                            break;
                        case 0x02:
                            r.dword = ADCD(id, r.dword);
                            break;
                        case 0x03:
                            r.dword = SBBD(id, r.dword);
                            break;
                        case 0x04:
                            r.dword = ANDD(id, r.dword);
                            break;
                        case 0x05:
                            r.dword = SUBD(id, r.dword);
                            break;
                        case 0x06:
                            r.dword = XORD(id, r.dword);
                            break;
                        case 0x07:
                            CMPD(id, r.dword);
                            break;
                    }
                } else {
                    int eaa = getEaa(rm);
                    int id = Fetchbs();
                    switch(which) {
                        case 0x00:
                            Memory.mem_writed(eaa, ADDD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x01:
                            Memory.mem_writed(eaa, ORD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x02:
                            Memory.mem_writed(eaa, ADCD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x03:
                            Memory.mem_writed(eaa, SBBD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x04:
                            Memory.mem_writed(eaa, ANDD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x05:
                            Memory.mem_writed(eaa, SUBD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x06:
                            Memory.mem_writed(eaa, XORD(id, Memory.mem_readd(eaa)));
                            break;
                        case 0x07:
                            CMPD(id, Memory.mem_readd(eaa));
                            break;
                    }
                }
                return HANDLED;
            }
