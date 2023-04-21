        public int call() {
            IoHandler.IO_Write(0x70, 0xc);
            IoHandler.IO_Read(0x71);
            if (Memory.mem_readb(BIOS_WAIT_FLAG_ACTIVE) != 0) {
                long count = Memory.mem_readd(BIOS_WAIT_FLAG_COUNT) & 0xFFFFFFFFl;
                if (count > 997) {
                    Memory.mem_writed(BIOS_WAIT_FLAG_COUNT, (int) count - 997);
                } else {
                    Memory.mem_writed(BIOS_WAIT_FLAG_COUNT, 0);
                    int where = Memory.Real2Phys(Memory.mem_readd(BIOS_WAIT_FLAG_POINTER));
                    Memory.mem_writeb(where, (short) (Memory.mem_readb(where) | 0x80));
                    Memory.mem_writeb(BIOS_WAIT_FLAG_ACTIVE, 0);
                    Memory.mem_writed(BIOS_WAIT_FLAG_POINTER, Memory.RealMake(0, BIOS_WAIT_FLAG_TEMP));
                    IoHandler.IO_Write(0x70, 0xb);
                    IoHandler.IO_Write(0x71, (IoHandler.IO_Read(0x71) & ~0x40));
                }
            }
            IoHandler.IO_Write(0xa0, 0x20);
            IoHandler.IO_Write(0x20, 0x20);
            return 0;
        }
