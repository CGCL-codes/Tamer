    public static boolean INT10_VideoState_Save(int state, int buffer) {
        int ct;
        if ((state & 7) == 0) return false;
        int base_seg = Memory.RealSeg(buffer);
        int base_dest = Memory.RealOff(buffer) + 0x20;
        if ((state & 1) != 0) {
            Memory.real_writew(base_seg, Memory.RealOff(buffer), base_dest);
            int crt_reg = Memory.real_readw(Int10.BIOSMEM_SEG, Int10.BIOSMEM_CRTC_ADDRESS);
            Memory.real_writew(base_seg, base_dest + 0x40, crt_reg);
            Memory.real_writeb(base_seg, base_dest + 0x00, IO.IO_ReadB(0x3c4));
            Memory.real_writeb(base_seg, base_dest + 0x01, IO.IO_ReadB(0x3d4));
            Memory.real_writeb(base_seg, base_dest + 0x02, IO.IO_ReadB(0x3ce));
            IO.IO_ReadB(crt_reg + 6);
            Memory.real_writeb(base_seg, base_dest + 0x03, IO.IO_ReadB(0x3c0));
            Memory.real_writeb(base_seg, base_dest + 0x04, IO.IO_ReadB(0x3ca));
            for (ct = 1; ct < 5; ct++) {
                IO.IO_WriteB(0x3c4, ct);
                Memory.real_writeb(base_seg, base_dest + 0x04 + ct, IO.IO_ReadB(0x3c5));
            }
            Memory.real_writeb(base_seg, base_dest + 0x09, IO.IO_ReadB(0x3cc));
            for (ct = 0; ct < 0x19; ct++) {
                IO.IO_WriteB(crt_reg, ct);
                Memory.real_writeb(base_seg, base_dest + 0x0a + ct, IO.IO_ReadB(crt_reg + 1));
            }
            for (ct = 0; ct < 4; ct++) {
                IO.IO_ReadB(crt_reg + 6);
                IO.IO_WriteB(0x3c0, 0x10 + ct);
                Memory.real_writeb(base_seg, base_dest + 0x33 + ct, IO.IO_ReadB(0x3c1));
            }
            for (ct = 0; ct < 9; ct++) {
                IO.IO_WriteB(0x3ce, ct);
                Memory.real_writeb(base_seg, base_dest + 0x37 + ct, IO.IO_ReadB(0x3cf));
            }
            IO.IO_WriteB(0x3c4, 2);
            short crtc_2 = (short) IO.IO_ReadB(0x3c5);
            IO.IO_WriteB(0x3c4, 4);
            short crtc_4 = (short) IO.IO_ReadB(0x3c5);
            IO.IO_WriteB(0x3ce, 6);
            short gfx_6 = (short) IO.IO_ReadB(0x3cf);
            IO.IO_WriteB(0x3ce, 5);
            short gfx_5 = (short) IO.IO_ReadB(0x3cf);
            IO.IO_WriteB(0x3ce, 4);
            short gfx_4 = (short) IO.IO_ReadB(0x3cf);
            IO.IO_WriteW(0x3c4, 0x0f02);
            IO.IO_WriteW(0x3c4, 0x0704);
            IO.IO_WriteW(0x3ce, 0x0406);
            IO.IO_WriteW(0x3ce, 0x0105);
            Memory.mem_writeb(0xaffff, 0);
            for (ct = 0; ct < 4; ct++) {
                IO.IO_WriteW(0x3ce, 0x0004 + ct * 0x100);
                Memory.real_writeb(base_seg, base_dest + 0x42 + ct, Memory.mem_readb(0xaffff));
            }
            IO.IO_WriteW(0x3ce, 0x0004 | (gfx_4 << 8));
            IO.IO_WriteW(0x3ce, 0x0005 | (gfx_5 << 8));
            IO.IO_WriteW(0x3ce, 0x0006 | (gfx_6 << 8));
            IO.IO_WriteW(0x3c4, 0x0004 | (crtc_4 << 8));
            IO.IO_WriteW(0x3c4, 0x0002 | (crtc_2 << 8));
            for (ct = 0; ct < 0x10; ct++) {
                IO.IO_ReadB(crt_reg + 6);
                IO.IO_WriteB(0x3c0, ct);
                Memory.real_writeb(base_seg, base_dest + 0x23 + ct, IO.IO_ReadB(0x3c1));
            }
            IO.IO_WriteB(0x3c0, 0x20);
            base_dest += 0x46;
        }
        if ((state & 2) != 0) {
            Memory.real_writew(base_seg, Memory.RealOff(buffer) + 2, base_dest);
            Memory.real_writeb(base_seg, base_dest + 0x00, Memory.mem_readb(0x410) & 0x30);
            for (ct = 0; ct < 0x1e; ct++) {
                Memory.real_writeb(base_seg, base_dest + 0x01 + ct, Memory.mem_readb(0x449 + ct));
            }
            for (ct = 0; ct < 0x07; ct++) {
                Memory.real_writeb(base_seg, base_dest + 0x1f + ct, Memory.mem_readb(0x484 + ct));
            }
            Memory.real_writed(base_seg, base_dest + 0x26, Memory.mem_readd(0x48a));
            Memory.real_writed(base_seg, base_dest + 0x2a, Memory.mem_readd(0x14));
            Memory.real_writed(base_seg, base_dest + 0x2e, Memory.mem_readd(0x74));
            Memory.real_writed(base_seg, base_dest + 0x32, Memory.mem_readd(0x7c));
            Memory.real_writed(base_seg, base_dest + 0x36, Memory.mem_readd(0x10c));
            base_dest += 0x3a;
        }
        if ((state & 4) != 0) {
            Memory.real_writew(base_seg, Memory.RealOff(buffer) + 4, base_dest);
            int crt_reg = Memory.real_readw(Int10.BIOSMEM_SEG, Int10.BIOSMEM_CRTC_ADDRESS);
            IO.IO_ReadB(crt_reg + 6);
            IO.IO_WriteB(0x3c0, 0x14);
            Memory.real_writeb(base_seg, base_dest + 0x303, (short) IO.IO_ReadB(0x3c1));
            int dac_state = IO.IO_ReadB(0x3c7) & 1;
            int dac_windex = IO.IO_ReadB(0x3c8);
            if (dac_state != 0) dac_windex--;
            Memory.real_writeb(base_seg, base_dest + 0x000, (short) dac_state);
            Memory.real_writeb(base_seg, base_dest + 0x001, (short) dac_windex);
            Memory.real_writeb(base_seg, base_dest + 0x002, (short) IO.IO_ReadB(0x3c6));
            for (ct = 0; ct < 0x100; ct++) {
                IO.IO_WriteB(0x3c7, ct);
                Memory.real_writeb(base_seg, base_dest + 0x003 + ct * 3 + 0, (short) IO.IO_ReadB(0x3c9));
                Memory.real_writeb(base_seg, base_dest + 0x003 + ct * 3 + 1, (short) IO.IO_ReadB(0x3c9));
                Memory.real_writeb(base_seg, base_dest + 0x003 + ct * 3 + 2, (short) IO.IO_ReadB(0x3c9));
            }
            IO.IO_ReadB(crt_reg + 6);
            IO.IO_WriteB(0x3c0, 0x20);
            base_dest += 0x303;
        }
        if ((Dosbox.svgaCard == SVGACards.SVGA_S3Trio) && (state & 8) != 0) {
            Memory.real_writew(base_seg, Memory.RealOff(buffer) + 6, base_dest);
            int crt_reg = Memory.real_readw(Int10.BIOSMEM_SEG, Int10.BIOSMEM_CRTC_ADDRESS);
            IO.IO_WriteB(0x3c4, 0x08);
            IO.IO_ReadB(0x3c5);
            IO.IO_WriteB(0x3c5, 0x06);
            for (ct = 0; ct < 0x13; ct++) {
                IO.IO_WriteB(0x3c4, 0x09 + ct);
                Memory.real_writeb(base_seg, base_dest + 0x00 + ct, (short) IO.IO_ReadB(0x3c5));
            }
            IO.IO_WriteW(crt_reg, 0x4838);
            IO.IO_WriteW(crt_reg, 0xa539);
            int ct_dest = 0x13;
            for (ct = 0; ct < 0x40; ct++) {
                if ((ct == 0x4a - 0x30) || (ct == 0x4b - 0x30)) {
                    IO.IO_WriteB(crt_reg, 0x45);
                    IO.IO_ReadB(crt_reg + 1);
                    IO.IO_WriteB(crt_reg, 0x30 + ct);
                    Memory.real_writeb(base_seg, base_dest + (ct_dest++), (short) IO.IO_ReadB(crt_reg + 1));
                    Memory.real_writeb(base_seg, base_dest + (ct_dest++), (short) IO.IO_ReadB(crt_reg + 1));
                    Memory.real_writeb(base_seg, base_dest + (ct_dest++), (short) IO.IO_ReadB(crt_reg + 1));
                } else {
                    IO.IO_WriteB(crt_reg, 0x30 + ct);
                    Memory.real_writeb(base_seg, base_dest + (ct_dest++), (short) IO.IO_ReadB(crt_reg + 1));
                }
            }
        }
        return true;
    }
