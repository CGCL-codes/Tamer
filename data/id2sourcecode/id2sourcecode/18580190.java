        public int call(int vidstart, int line) {
            if (VGA.svga.hardware_cursor_active == null || !VGA.svga.hardware_cursor_active.call()) return VGA.vga.mem.linear + vidstart;
            int lineat = (vidstart - (VGA.vga.config.real_start << 2)) / VGA.vga.draw.width;
            if ((VGA.vga.s3.hgc.posx >= VGA.vga.draw.width) || (lineat < VGA.vga.s3.hgc.originy) || (lineat > (VGA.vga.s3.hgc.originy + (63 - VGA.vga.s3.hgc.posy)))) {
                return VGA.vga.mem.linear + vidstart;
            } else {
                Memory.host_memcpy(TempLine, VGA.vga.mem.linear + vidstart, VGA.vga.draw.width);
                int sourceStartBit = ((lineat - VGA.vga.s3.hgc.originy) + VGA.vga.s3.hgc.posy) * 64 + VGA.vga.s3.hgc.posx;
                int cursorMemStart = ((sourceStartBit >> 2) & ~1) + (VGA.vga.s3.hgc.startaddr << 10);
                int cursorStartBit = sourceStartBit & 0x7;
                if ((cursorMemStart & 0x2) != 0) cursorMemStart--;
                int cursorMemEnd = cursorMemStart + ((64 - VGA.vga.s3.hgc.posx) >> 2);
                int dst_off = VGA.vga.s3.hgc.originx + TempLine;
                cursorMemStart += VGA.vga.mem.linear;
                cursorMemEnd += VGA.vga.mem.linear;
                for (int m = cursorMemStart; m < cursorMemEnd; ) {
                    int bitsA = Memory.host_readb(m);
                    int bitsB = Memory.host_readb(m + 2);
                    for (int bit = (0x80 >> cursorStartBit); bit != 0; bit >>= 1) {
                        cursorStartBit = 0;
                        if ((bitsA & bit) != 0) {
                            if ((bitsB & bit) != 0) {
                                Memory.host_writebs(dst_off, (byte) (Memory.host_readbs(dst_off) ^ 0xFF));
                                dst_off++;
                            }
                        } else if ((bitsB & bit) != 0) {
                            Memory.host_writebs(dst_off, VGA.vga.s3.hgc.forestack.p[0]);
                        } else {
                            Memory.host_writebs(dst_off, VGA.vga.s3.hgc.backstack.p[0]);
                        }
                        dst_off++;
                    }
                    if ((m & 1) != 0) m += 3; else m++;
                }
                return TempLine;
            }
        }
