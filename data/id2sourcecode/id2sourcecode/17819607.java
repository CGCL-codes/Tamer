        public int call(int port, int iolen) {
            return (VGA.vga.svga.bank_read << 3) | VGA.vga.svga.bank_write | ((VGA.vga.svga.bank_size == 128 * 1024) ? 0 : 0x40);
        }
