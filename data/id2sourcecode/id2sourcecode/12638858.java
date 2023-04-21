        public void call(Section sec) {
            VGA.vga.svga.bank_read = VGA.vga.svga.bank_write = 0;
            VGA.vga.svga.bank_read_full = VGA.vga.svga.bank_write_full = 0;
            int vga_allocsize = VGA.vga.vmemsize;
            if (vga_allocsize < 512 * 1024) vga_allocsize = 512 * 1024;
            vga_allocsize += 2048;
            VGA.vga.mem.linear_orgptr = Memory.allocate(vga_allocsize + 16);
            VGA.vga.mem.linear = VGA.vga.mem.linear_orgptr;
            VGA.vga.fastmem_orgptr = Memory.allocate((VGA.vga.vmemsize << 1) + 4096 + 16);
            VGA.vga.fastmem = VGA.vga.fastmem_orgptr;
            VGA_draw.TempLine = Memory.allocate(VGA_draw.TEMPLINE_SIZE);
            VGA.vga.vmemwrap = VGA.vga.vmemsize;
            if (VGA_KEEP_CHANGES) {
                VGA.vga.changes = new VGA.VGA_Changes();
                int changesMapSize = (VGA.vga.vmemsize >> VGA.VGA_CHANGE_SHIFT) + 32;
                VGA.vga.changes.map = new Ptr(changesMapSize);
            }
            VGA.vga.svga.bank_read = VGA.vga.svga.bank_write = 0;
            VGA.vga.svga.bank_read_full = VGA.vga.svga.bank_write_full = 0;
            VGA.vga.svga.bank_size = 0x10000;
            sec.AddDestroyFunction(VGA_Memory_ShutDown);
            if (Dosbox.machine == MachineType.MCH_PCJR) {
            }
        }
