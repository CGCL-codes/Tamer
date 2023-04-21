        public int call() {
            IntRef temp = new IntRef(0);
            switch(CPU_Regs.reg_eax.high()) {
                case 0x00:
                    if ((get_key(temp)) && (!IsEnhancedKey(temp))) {
                        CPU_Regs.reg_eax.word(temp.value);
                    } else {
                        CPU_Regs.reg_ip(CPU_Regs.reg_ip() + 1);
                    }
                    break;
                case 0x10:
                    if (get_key(temp)) {
                        if (((temp.value & 0xff) == 0xf0) && (temp.value >> 8) != 0) {
                            temp.value &= 0xff00;
                        }
                        CPU_Regs.reg_eax.word(temp.value);
                    } else {
                        CPU_Regs.reg_ip(CPU_Regs.reg_ip() + 1);
                    }
                    break;
                case 0x01:
                    Memory.mem_writew(CPU.Segs_SSphys + CPU_Regs.reg_esp.word() + 4, (Memory.mem_readw(CPU.Segs_SSphys + CPU_Regs.reg_esp.word() + 4) | CPU_Regs.IF));
                    for (; ; ) {
                        if (check_key(temp)) {
                            if (!IsEnhancedKey(temp)) {
                                Callback.CALLBACK_SZF(false);
                                CPU_Regs.reg_eax.word(temp.value);
                                break;
                            } else {
                                get_key(temp);
                            }
                        } else {
                            Callback.CALLBACK_SZF(true);
                            break;
                        }
                    }
                    break;
                case 0x11:
                    if (!check_key(temp)) {
                        Callback.CALLBACK_SZF(true);
                    } else {
                        Callback.CALLBACK_SZF(false);
                        if (((temp.value & 0xff) == 0xf0) && (temp.value >> 8) != 0) {
                            temp.value &= 0xff00;
                        }
                        CPU_Regs.reg_eax.word(temp.value);
                    }
                    break;
                case 0x02:
                    CPU_Regs.reg_eax.low(Memory.mem_readb(Bios.BIOS_KEYBOARD_FLAGS1));
                    break;
                case 0x03:
                    if (CPU_Regs.reg_eax.low() == 0x00) {
                        IoHandler.IO_Write(0x60, 0xf3);
                        IoHandler.IO_Write(0x60, 0x20);
                    } else if (CPU_Regs.reg_eax.low() == 0x05) {
                        IoHandler.IO_Write(0x60, 0xf3);
                        IoHandler.IO_Write(0x60, (CPU_Regs.reg_ebx.high() & 3) << 5 | (CPU_Regs.reg_ebx.low() & 0x1f));
                    } else {
                        if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_BIOS, LogSeverities.LOG_ERROR, "INT16:Unhandled Typematic Rate Call " + Integer.toString(CPU_Regs.reg_eax.low(), 16) + " BX=" + Integer.toString(CPU_Regs.reg_ebx.word(), 16));
                    }
                    break;
                case 0x05:
                    if (BIOS_AddKeyToBuffer(CPU_Regs.reg_ecx.word())) CPU_Regs.reg_eax.low(0); else CPU_Regs.reg_eax.low(1);
                    break;
                case 0x12:
                    CPU_Regs.reg_eax.low(Memory.mem_readb(Bios.BIOS_KEYBOARD_FLAGS1));
                    CPU_Regs.reg_eax.high(Memory.mem_readb(Bios.BIOS_KEYBOARD_FLAGS2));
                    break;
                case 0x55:
                    Log.log(LogTypes.LOG_BIOS, LogSeverities.LOG_NORMAL, "INT16:55:Word TSR compatible call");
                    break;
                default:
                    if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_BIOS, LogSeverities.LOG_ERROR, "INT16:Unhandled call " + Integer.toString(CPU_Regs.reg_eax.high(), 16));
                    break;
            }
            return Callback.CBRET_NONE;
        }
