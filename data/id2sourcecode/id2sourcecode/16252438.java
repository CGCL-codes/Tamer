    private static void bank_setup_pvga1a() {
        if ((pvga1a.PR1 & 0x08) != 0) {
        } else {
            VGA.vga.svga.bank_read = VGA.vga.svga.bank_write = (short) pvga1a.PR0A;
            VGA.vga.svga.bank_size = 4 * 1024;
            VGA_memory.VGA_SetupHandlers();
        }
    }
