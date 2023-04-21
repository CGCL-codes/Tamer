            public final int call() {
                final short rm = Fetchb();
                int which = (rm >> 3) & 7;
                switch(which) {
                    case 0x00:
                    case 0x01:
                        {
                            if (rm >= 0xc0) {
                                TESTD(Fetchd(), Modrm.GetEArd[rm].dword);
                            } else {
                                int eaa = getEaa(rm);
                                TESTD(Fetchd(), Memory.mem_readd(eaa));
                            }
                            break;
                        }
                    case 0x02:
                        {
                            if (rm >= 0xc0) {
                                Reg r = Modrm.GetEArd[rm];
                                r.dword = ~r.dword;
                            } else {
                                int eaa = getEaa(rm);
                                Memory.mem_writed(eaa, ~Memory.mem_readd(eaa));
                            }
                            break;
                        }
                    case 0x03:
                        {
                            if (rm >= 0xc0) {
                                Reg r = Modrm.GetEArd[rm];
                                r.dword = Instructions.Negd(r.dword);
                            } else {
                                int eaa = getEaa(rm);
                                Memory.mem_writed(eaa, Instructions.Negd(Memory.mem_readd(eaa)));
                            }
                            break;
                        }
                    case 0x04:
                        if (rm >= 0xc0) {
                            MULD(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            MULD(Memory.mem_readd(eaa));
                        }
                        break;
                    case 0x05:
                        if (rm >= 0xc0) {
                            IMULD(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            IMULD(Memory.mem_readd(eaa));
                        }
                        break;
                    case 0x06:
                        if (rm >= 0xc0) {
                            DIVD(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            DIVD(Memory.mem_readd(eaa));
                        }
                        break;
                    case 0x07:
                        if (rm >= 0xc0) {
                            IDIVD(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            IDIVD(Memory.mem_readd(eaa));
                        }
                        break;
                }
                return HANDLED;
            }
