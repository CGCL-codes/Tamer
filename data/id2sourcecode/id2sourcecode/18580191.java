        public int call(int vidstart, int line) {
            if (VGA.svga.hardware_cursor_active == null || !VGA.svga.hardware_cursor_active.call()) return VGA.vga.mem.linear + vidstart;
            int lineat = ((vidstart - (VGA.vga.config.real_start << 2)) >> 1) / VGA.vga.draw.width;
            if ((VGA.vga.s3.hgc.posx >= VGA.vga.draw.width) || (lineat < VGA.vga.s3.hgc.originy) || (lineat > (VGA.vga.s3.hgc.originy + (63 - VGA.vga.s3.hgc.posy)))) {
                return VGA.vga.mem.linear + vidstart;
            } else {
                Memory.host_memcpy(TempLine, VGA.vga.mem.linear + vidstart, VGA.vga.draw.width * 2);
                int sourceStartBit = ((lineat - VGA.vga.s3.hgc.originy) + VGA.vga.s3.hgc.posy) * 64 + VGA.vga.s3.hgc.posx;
                int cursorMemStart = ((sourceStartBit >> 2) & ~1) + (((int) VGA.vga.s3.hgc.startaddr) << 10);
                int cursorStartBit = sourceStartBit & 0x7;
                if ((cursorMemStart & 0x2) != 0) cursorMemStart--;
                int cursorMemEnd = cursorMemStart + ((64 - VGA.vga.s3.hgc.posx) >> 2);
                int xat = TempLine + VGA.vga.s3.hgc.originx * 2;
                for (int m = cursorMemStart; m < cursorMemEnd; ) {
                    int bitsA = Memory.host_readb(VGA.vga.mem.linear + m);
                    int bitsB = Memory.host_readb(VGA.vga.mem.linear + m + 2);
                    for (int bit = (0x80 >> cursorStartBit); bit != 0; bit >>= 1) {
                        cursorStartBit = 0;
                        if ((bitsA & bit) != 0) {
                            if ((bitsB & bit) != 0) Memory.host_writew(xat, ~Memory.host_readw(xat));
                        } else if ((bitsB & bit) != 0) {
                            Memory.host_writew(xat, VGA.vga.s3.hgc.forestack.readw(0));
                        } else {
                            Memory.host_writew(xat, VGA.vga.s3.hgc.backstack.readw(0));
                        }
                        xat += 2;
                    }
                    if ((m & 1) != 0) m += 3; else m++;
                }
                return TempLine;
            }
        }
