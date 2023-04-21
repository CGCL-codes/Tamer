        public int call() {
            int value = Memory.mem_readd(BIOS_TIMER) + 1;
            Memory.mem_writed(BIOS_TIMER, value);
            short val = Memory.mem_readb(BIOS_DISK_MOTOR_TIMEOUT);
            if (val != 0) Memory.mem_writeb(BIOS_DISK_MOTOR_TIMEOUT, (short) (val - 1));
            Memory.mem_writeb(BIOS_DRIVE_RUNNING, Memory.mem_readb(BIOS_DRIVE_RUNNING) & 0xF0);
            return Callback.CBRET_NONE;
        }
