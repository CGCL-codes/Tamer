        public void handler(int offset, int data) {
            Z80_Regs regs = new Z80_Regs();
            switch(data) {
                case 0x10:
                    return;
                case 0x71:
                    {
                        int in;
                        if (osd_key_pressed(OSD_KEY_3)) {
                            if (coin == 0 && credits < 99) credits++;
                            coin = 1;
                        } else coin = 0;
                        if (osd_key_pressed(OSD_KEY_1)) {
                            if (start1 == 0 && credits >= 1) credits--;
                            start1 = 1;
                        } else start1 = 0;
                        if (osd_key_pressed(OSD_KEY_2)) {
                            if (start2 == 0 && credits >= 2) credits -= 2;
                            start2 = 1;
                        } else start2 = 0;
                        in = readinputport(2);
                        if ((in & 0x20) == 0) {
                            if (fire == 0) {
                                in &= ~0x10;
                                fire = 1;
                            }
                        } else fire = 0;
                        if ((in & 0x01) == 0) in = (in & ~0x0f) | 0x00; else if ((in & 0x02) == 0) in = (in & ~0x0f) | 0x02; else if ((in & 0x04) == 0) in = (in & ~0x0f) | 0x04; else if ((in & 0x08) == 0) in = (in & ~0x0f) | 0x06; else in = (in & ~0x0f) | 0x08;
                        if (mode != 0) cpu_writemem(0x7000, 0x80); else cpu_writemem(0x7000, (credits / 10) * 16 + credits % 10);
                        cpu_writemem(0x7000 + 1, in);
                        cpu_writemem(0x7000 + 2, 0xff);
                    }
                    break;
                case 0xb1:
                    credits = 0;
                    cpu_writemem(0x7000, 0);
                    cpu_writemem(0x7000 + 1, 0);
                    cpu_writemem(0x7000 + 2, 0);
                    break;
                case 0xa1:
                    mode = 1;
                    break;
                case 0xc1:
                case 0xe1:
                    mode = 0;
                    break;
                case 0xd2:
                    cpu_writemem(0x7000, readinputport(0));
                    cpu_writemem(0x7001, readinputport(1));
                    break;
                default:
                    if (errorlog != null) fprintf(errorlog, "%04x: warning: unknown custom IO command %02x\n", new Object[] { Integer.valueOf(cpu_getpc()), Integer.valueOf(data) });
                    break;
            }
            Z80_GetRegs(regs);
            while (regs.BC2 > 1) {
                cpu_writemem(regs.DE2, cpu_readmem(regs.HL2));
                regs.DE2 = (regs.DE2 + 1 & 0xFFFF);
                regs.HL2 = (regs.HL2 + 1 & 0xFFFF);
                regs.BC2 = (regs.BC2 - 1 & 0xFFFF);
            }
            regs.SP = (regs.SP - 2 & 0xFFFF);
            RAM[regs.SP] = (char) (regs.PC & 0xFF);
            RAM[(regs.SP + 1 & 0xFFFF)] = (char) (regs.PC >> 8 & 0xFF);
            regs.PC = 0x0066;
            Z80_SetRegs(regs);
        }
