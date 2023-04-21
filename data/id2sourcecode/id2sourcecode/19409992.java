            public final int call() {
                short rm = Fetchb();
                int which = (rm >> 3) & 7;
                switch(which) {
                    case 0x00:
                        if (rm >= 0xc0) {
                            Reg r = Modrm.GetEArd[rm];
                            r.dword = INCD(r.dword);
                        } else {
                            int eaa = getEaa(rm);
                            Memory.mem_writed(eaa, INCD(Memory.mem_readd(eaa)));
                        }
                        break;
                    case 0x01:
                        if (rm >= 0xc0) {
                            Reg r = Modrm.GetEArd[rm];
                            r.dword = DECD(r.dword);
                        } else {
                            int eaa = getEaa(rm);
                            Memory.mem_writed(eaa, DECD(Memory.mem_readd(eaa)));
                        }
                        break;
                    case 0x02:
                        {
                            int eip;
                            if (rm >= 0xc0) {
                                eip = Modrm.GetEArd[rm].dword;
                            } else {
                                int eaa = getEaa(rm);
                                eip = Memory.mem_readd(eaa);
                            }
                            CPU.CPU_Push32(GETIP());
                            reg_eip = eip;
                            return CONTINUE;
                        }
                    case 0x03:
                        {
                            if (rm >= 0xc0) return ILLEGAL_OPCODE;
                            int eaa = getEaa(rm);
                            int newip = Memory.mem_readd(eaa);
                            int newcs = Memory.mem_readw(eaa + 4);
                            FillFlags();
                            CPU.CPU_CALL(true, newcs, newip, GETIP());
                            if (CPU_TRAP_CHECK) if (GETFLAG(TF) != 0) {
                                CPU.cpudecoder = Core_normal.CPU_Core_Normal_Trap_Run;
                                return CBRET_NONE;
                            }
                            return CONTINUE;
                        }
                    case 0x04:
                        if (rm >= 0xc0) {
                            reg_eip = Modrm.GetEArd[rm].dword;
                        } else {
                            int eaa = getEaa(rm);
                            reg_eip = Memory.mem_readd(eaa);
                        }
                        return CONTINUE;
                    case 0x05:
                        {
                            if (rm >= 0xc0) return ILLEGAL_OPCODE;
                            int eaa = getEaa(rm);
                            int newip = Memory.mem_readd(eaa);
                            int newcs = Memory.mem_readw(eaa + 4);
                            FillFlags();
                            CPU.CPU_JMP(true, newcs, newip, GETIP());
                            if (CPU_TRAP_CHECK) if (GETFLAG(TF) != 0) {
                                CPU.cpudecoder = Core_normal.CPU_Core_Normal_Trap_Run;
                                return CBRET_NONE;
                            }
                            return CONTINUE;
                        }
                    case 0x06:
                        if (rm >= 0xc0) {
                            CPU.CPU_Push32(Modrm.GetEArd[rm].dword);
                        } else {
                            int eaa = getEaa(rm);
                            CPU.CPU_Push32(Memory.mem_readd(eaa));
                        }
                        break;
                    default:
                        if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_CPU, LogSeverities.LOG_ERROR, "CPU:66:GRP5:Illegal call " + Integer.toString(which, 16));
                        return ILLEGAL_OPCODE;
                }
                return HANDLED;
            }
