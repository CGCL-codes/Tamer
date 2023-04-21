        public void call(int reg, int val, int iolen) {
            switch(reg) {
                case 0x31:
                    VGA.vga.s3.reg_31 = (short) val;
                    VGA.vga.config.compatible_chain4 = (val & 0x08) == 0;
                    if (VGA.vga.config.compatible_chain4) VGA.vga.vmemwrap = 256 * 1024; else VGA.vga.vmemwrap = VGA.vga.vmemsize;
                    VGA.vga.config.display_start = (VGA.vga.config.display_start & ~0x30000) | ((val & 0x30) << 12);
                    VGA.VGA_DetermineMode();
                    VGA_memory.VGA_SetupHandlers();
                    break;
                case 0x35:
                    if (VGA.vga.s3.reg_lock1 != 0x48) return;
                    VGA.vga.s3.reg_35 = (short) (val & 0xf0);
                    if (((VGA.vga.svga.bank_read & 0xf) ^ (val & 0xf)) != 0) {
                        VGA.vga.svga.bank_read &= 0xf0;
                        VGA.vga.svga.bank_read |= val & 0xf;
                        VGA.vga.svga.bank_write = VGA.vga.svga.bank_read;
                        VGA_memory.VGA_SetupHandlers();
                    }
                    break;
                case 0x38:
                    VGA.vga.s3.reg_lock1 = (short) val;
                    break;
                case 0x39:
                    VGA.vga.s3.reg_lock2 = (short) val;
                    break;
                case 0x3a:
                    VGA.vga.s3.reg_3a = (short) val;
                    break;
                case 0x40:
                    VGA.vga.s3.reg_40 = (short) val;
                    break;
                case 0x41:
                    VGA.vga.s3.reg_41 = (short) val;
                    break;
                case 0x43:
                    VGA.vga.s3.reg_43 = (short) (val & ~0x4);
                    if ((((val & 0x4) ^ (VGA.vga.config.scan_len >> 6)) & 0x4) != 0) {
                        VGA.vga.config.scan_len &= 0x2ff;
                        VGA.vga.config.scan_len |= (val & 0x4) << 6;
                        VGA_draw.VGA_CheckScanLength();
                    }
                    break;
                case 0x45:
                    VGA.vga.s3.hgc.curmode = (short) val;
                    VGA_draw.VGA_ActivateHardwareCursor();
                    break;
                case 0x46:
                    VGA.vga.s3.hgc.originx = (VGA.vga.s3.hgc.originx & 0x00ff) | (val << 8);
                    break;
                case 0x47:
                    VGA.vga.s3.hgc.originx = (VGA.vga.s3.hgc.originx & 0xff00) | val;
                    break;
                case 0x48:
                    VGA.vga.s3.hgc.originy = (VGA.vga.s3.hgc.originy & 0x00ff) | (val << 8);
                    break;
                case 0x49:
                    VGA.vga.s3.hgc.originy = (VGA.vga.s3.hgc.originy & 0xff00) | val;
                    break;
                case 0x4A:
                    if (VGA.vga.s3.hgc.fstackpos > 2) VGA.vga.s3.hgc.fstackpos = 0;
                    VGA.vga.s3.hgc.forestack.set(VGA.vga.s3.hgc.fstackpos, val);
                    VGA.vga.s3.hgc.fstackpos++;
                    break;
                case 0x4B:
                    if (VGA.vga.s3.hgc.bstackpos > 2) VGA.vga.s3.hgc.bstackpos = 0;
                    VGA.vga.s3.hgc.backstack.set(VGA.vga.s3.hgc.bstackpos, val);
                    VGA.vga.s3.hgc.bstackpos++;
                    break;
                case 0x4c:
                    VGA.vga.s3.hgc.startaddr &= 0xff;
                    VGA.vga.s3.hgc.startaddr |= ((val & 0xf) << 8);
                    if ((((int) VGA.vga.s3.hgc.startaddr) << 10) + ((64 * 64 * 2) / 8) > VGA.vga.vmemsize) {
                        VGA.vga.s3.hgc.startaddr &= 0xff;
                        Log.log(LogTypes.LOG_VGAMISC, LogSeverities.LOG_NORMAL, "VGA:S3:CRTC: HGC pattern address beyond video memory");
                    }
                    break;
                case 0x4d:
                    VGA.vga.s3.hgc.startaddr &= 0xff00;
                    VGA.vga.s3.hgc.startaddr |= (val & 0xff);
                    break;
                case 0x4e:
                    VGA.vga.s3.hgc.posx = (short) (val & 0x3f);
                    break;
                case 0x4f:
                    VGA.vga.s3.hgc.posy = (short) (val & 0x3f);
                    break;
                case 0x50:
                    VGA.vga.s3.reg_50 = (short) val;
                    switch(val & VGA.S3_XGA_CMASK) {
                        case VGA.S3_XGA_32BPP:
                            VGA.vga.s3.xga_color_mode = VGA.M_LIN32;
                            break;
                        case VGA.S3_XGA_16BPP:
                            VGA.vga.s3.xga_color_mode = VGA.M_LIN16;
                            break;
                        case VGA.S3_XGA_8BPP:
                            VGA.vga.s3.xga_color_mode = VGA.M_LIN8;
                            break;
                    }
                    switch(val & VGA.S3_XGA_WMASK) {
                        case VGA.S3_XGA_1024:
                            VGA.vga.s3.xga_screen_width = 1024;
                            break;
                        case VGA.S3_XGA_1152:
                            VGA.vga.s3.xga_screen_width = 1152;
                            break;
                        case VGA.S3_XGA_640:
                            VGA.vga.s3.xga_screen_width = 640;
                            break;
                        case VGA.S3_XGA_800:
                            VGA.vga.s3.xga_screen_width = 800;
                            break;
                        case VGA.S3_XGA_1280:
                            VGA.vga.s3.xga_screen_width = 1280;
                            break;
                        default:
                            VGA.vga.s3.xga_screen_width = 1024;
                            break;
                    }
                    break;
                case 0x51:
                    VGA.vga.s3.reg_51 = (short) (val & 0xc0);
                    VGA.vga.config.display_start &= 0xF3FFFF;
                    VGA.vga.config.display_start |= (val & 3) << 18;
                    if (((VGA.vga.svga.bank_read & 0x30) ^ ((val & 0xc) << 2)) != 0) {
                        VGA.vga.svga.bank_read &= 0xcf;
                        VGA.vga.svga.bank_read |= (val & 0xc) << 2;
                        VGA.vga.svga.bank_write = VGA.vga.svga.bank_read;
                        VGA_memory.VGA_SetupHandlers();
                    }
                    if ((((val & 0x30) ^ (VGA.vga.config.scan_len >> 4)) & 0x30) != 0) {
                        VGA.vga.config.scan_len &= 0xff;
                        VGA.vga.config.scan_len |= (val & 0x30) << 4;
                        VGA_draw.VGA_CheckScanLength();
                    }
                    break;
                case 0x52:
                    VGA.vga.s3.reg_52 = (short) val;
                    break;
                case 0x53:
                    if (VGA.vga.s3.ext_mem_ctrl != val) {
                        VGA.vga.s3.ext_mem_ctrl = (short) val;
                        VGA_memory.VGA_SetupHandlers();
                    }
                    break;
                case 0x55:
                    VGA.vga.s3.reg_55 = (short) val;
                    break;
                case 0x58:
                    VGA.vga.s3.reg_58 = (short) val;
                    break;
                case 0x59:
                    if (((VGA.vga.s3.la_window & 0xff00) ^ (val << 8)) != 0) {
                        VGA.vga.s3.la_window = (VGA.vga.s3.la_window & 0x00ff) | (val << 8);
                        VGA_memory.VGA_StartUpdateLFB();
                    }
                    break;
                case 0x5a:
                    if (((VGA.vga.s3.la_window & 0x00ff) ^ val) != 0) {
                        VGA.vga.s3.la_window = (VGA.vga.s3.la_window & 0xff00) | val;
                        VGA_memory.VGA_StartUpdateLFB();
                    }
                    break;
                case 0x5D:
                    if (((val ^ VGA.vga.s3.ex_hor_overflow) & 3) != 0) {
                        VGA.vga.s3.ex_hor_overflow = (short) val;
                        VGA.VGA_StartResize();
                    } else VGA.vga.s3.ex_hor_overflow = (short) val;
                    break;
                case 0x5e:
                    VGA.vga.config.line_compare = (VGA.vga.config.line_compare & 0x3ff) | (val & 0x40) << 4;
                    if (((val ^ VGA.vga.s3.ex_ver_overflow) & 0x3) != 0) {
                        VGA.vga.s3.ex_ver_overflow = (short) val;
                        VGA.VGA_StartResize();
                    } else VGA.vga.s3.ex_ver_overflow = (short) val;
                    break;
                case 0x67:
                    VGA.vga.s3.misc_control_2 = (short) val;
                    VGA.VGA_DetermineMode();
                    break;
                case 0x69:
                    if ((((VGA.vga.config.display_start & 0x1f0000) >> 16) ^ (val & 0x1f)) != 0) {
                        VGA.vga.config.display_start &= 0xffff;
                        VGA.vga.config.display_start |= (val & 0x1f) << 16;
                    }
                    break;
                case 0x6a:
                    VGA.vga.svga.bank_read = (short) (val & 0x7f);
                    VGA.vga.svga.bank_write = VGA.vga.svga.bank_read;
                    VGA_memory.VGA_SetupHandlers();
                    break;
                case 0x6b:
                    VGA.vga.s3.reg_6b = (short) val;
                    break;
                default:
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_VGAMISC, LogSeverities.LOG_NORMAL, "VGA:S3:CRTC:Write to illegal index " + Integer.toString(reg, 16));
                    break;
            }
        }
