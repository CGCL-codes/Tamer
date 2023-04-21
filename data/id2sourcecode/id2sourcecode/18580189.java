        public int call(int vidstart, int line) {
            int ret = VGA.vga.draw.linear_base + (vidstart & VGA.vga.draw.linear_mask);
            int temps = TempLine;
            for (int i = 0; i < VGA.vga.draw.line_length; i++) {
                Memory.host_writew(temps, VGA.vga.dac.xlat16[Memory.host_readb(ret + i)]);
                temps += 2;
            }
            return TempLine;
        }
