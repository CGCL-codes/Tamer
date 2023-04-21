        public int call(int port, int iolen) {
            return (VGA.vga.svga.bank_read << 4) | VGA.vga.svga.bank_write;
        }
