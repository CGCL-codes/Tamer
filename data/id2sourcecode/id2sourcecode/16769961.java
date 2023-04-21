        public int call() {
            if (((CPU_Regs.reg_eax.high() != 0x50) && (CPU_Regs.reg_eax.high() != 0x51) && (CPU_Regs.reg_eax.high() != 0x62) && (CPU_Regs.reg_eax.high() != 0x64)) && (CPU_Regs.reg_eax.high() < 0x6c)) {
                Dos_PSP psp = new Dos_PSP(dos.psp());
                psp.SetStack(Memory.RealMake((int) CPU.Segs_SSval, CPU_Regs.reg_esp.word() - 18));
            }
            switch(CPU_Regs.reg_eax.high() & 0xFF) {
                case 0x00:
                    Dos_execute.DOS_Terminate(Memory.mem_readw(CPU.Segs_SSphys + CPU_Regs.reg_esp.word() + 2), false, 0);
                    break;
                case 0x01:
                    {
                        byte[] c = new byte[1];
                        IntRef n = new IntRef(1);
                        dos.echo = true;
                        Dos_files.DOS_ReadFile(Dos_files.STDIN, c, n);
                        CPU_Regs.reg_eax.low(c[0]);
                        dos.echo = false;
                    }
                    break;
                case 0x02:
                    {
                        byte[] c = new byte[] { (byte) CPU_Regs.reg_edx.low() };
                        IntRef n = new IntRef(1);
                        Dos_files.DOS_WriteFile(Dos_files.STDOUT, c, n);
                        CPU_Regs.reg_eax.low(c[0]);
                    }
                    break;
                case 0x03:
                    {
                        int port = Memory.real_readw(0x40, 0);
                        if (port != 0 && Serialports.serialports[0] != null) {
                            ShortRef status = new ShortRef();
                            ShortRef al = new ShortRef(CPU_Regs.reg_eax.low());
                            IO.IO_WriteB(port + 4, 0x3);
                            Serialports.serialports[0].Getchar(al, status, true, 0xFFFFFFFF);
                            CPU_Regs.reg_eax.low(al.value);
                        }
                    }
                    break;
                case 0x04:
                    {
                        int port = Memory.real_readw(0x40, 0);
                        if (port != 0 && Serialports.serialports[0] != null) {
                            IO.IO_WriteB(port + 4, 0x3);
                            Serialports.serialports[0].Putchar(CPU_Regs.reg_edx.low(), true, true, 0xFFFFFFFF);
                            IO.IO_WriteB(port + 4, 0x1);
                        }
                    }
                    break;
                case 0x05:
                    Log.exit("DOS:Unhandled call " + Integer.toString(CPU_Regs.reg_eax.high(), 16));
                    break;
                case 0x06:
                    switch(CPU_Regs.reg_edx.low() & 0xFF) {
                        case 0xFF:
                            {
                                overhead();
                                if (!Dos_ioctl.DOS_GetSTDINStatus()) {
                                    CPU_Regs.reg_eax.low(0);
                                    Callback.CALLBACK_SZF(true);
                                    break;
                                }
                                byte[] c = new byte[1];
                                IntRef n = new IntRef(1);
                                Dos_files.DOS_ReadFile(Dos_files.STDIN, c, n);
                                CPU_Regs.reg_eax.low(c[0]);
                                Callback.CALLBACK_SZF(false);
                                break;
                            }
                        default:
                            {
                                byte[] c = new byte[] { (byte) CPU_Regs.reg_edx.low() };
                                IntRef n = new IntRef(1);
                                Dos_files.DOS_WriteFile(Dos_files.STDOUT, c, n);
                                CPU_Regs.reg_eax.low(CPU_Regs.reg_edx.low());
                            }
                            break;
                    }
                    break;
                case 0x07:
                    {
                        byte[] c = new byte[1];
                        IntRef n = new IntRef(1);
                        Dos_files.DOS_ReadFile(Dos_files.STDIN, c, n);
                        CPU_Regs.reg_eax.low(c[0]);
                        break;
                    }
                case 0x08:
                    {
                        byte[] c = new byte[1];
                        IntRef n = new IntRef(1);
                        Dos_files.DOS_ReadFile(Dos_files.STDIN, c, n);
                        CPU_Regs.reg_eax.low(c[0]);
                        break;
                    }
                case 0x09:
                    {
                        byte[] c = new byte[1];
                        IntRef n = new IntRef(1);
                        int buf = CPU.Segs_DSphys + CPU_Regs.reg_edx.word();
                        while ((c[0] = ((byte) (Memory.mem_readb(buf++) & 0xFF))) != '$') {
                            Dos_files.DOS_WriteFile(Dos_files.STDOUT, c, n);
                        }
                    }
                    break;
                case 0x0a:
                    {
                        int data = CPU.Segs_DSphys + CPU_Regs.reg_edx.word();
                        short free = Memory.mem_readb(data);
                        short read = 0;
                        byte[] c = new byte[1];
                        IntRef n = new IntRef(1);
                        if (free == 0) break;
                        for (; ; ) {
                            Dos_files.DOS_ReadFile(Dos_files.STDIN, c, n);
                            if (c[0] == 8) {
                                if (read != 0) {
                                    Dos_files.DOS_WriteFile(Dos_files.STDOUT, c, n);
                                    c[0] = ' ';
                                    Dos_files.DOS_WriteFile(Dos_files.STDOUT, c, n);
                                    c[0] = 8;
                                    Dos_files.DOS_WriteFile(Dos_files.STDOUT, c, n);
                                    --read;
                                }
                                continue;
                            }
                            if (read >= free) {
                                byte[] bell = new byte[] { 7 };
                                Dos_files.DOS_WriteFile(Dos_files.STDOUT, bell, n);
                                continue;
                            }
                            Dos_files.DOS_WriteFile(Dos_files.STDOUT, c, n);
                            Memory.mem_writeb(data + read + 2, c[0]);
                            if (c[0] == 13) break;
                            read++;
                        }
                        Memory.mem_writeb(data + 1, read);
                        break;
                    }
                case 0x0b:
                    if (!Dos_ioctl.DOS_GetSTDINStatus()) {
                        CPU_Regs.reg_eax.low(0x00);
                    } else {
                        CPU_Regs.reg_eax.low(0xFF);
                    }
                    overhead();
                    break;
                case 0x0c:
                    {
                        int handle = RealHandle(Dos_files.STDIN);
                        if (handle != 0xFF && Dos_files.Files[handle] != null && Dos_files.Files[handle].IsName("CON")) {
                            byte[] c = new byte[1];
                            IntRef n = new IntRef(0);
                            while (Dos_ioctl.DOS_GetSTDINStatus()) {
                                n.value = 1;
                                Dos_files.DOS_ReadFile(Dos_files.STDIN, c, n);
                            }
                        }
                        switch(CPU_Regs.reg_eax.low()) {
                            case 0x1:
                            case 0x6:
                            case 0x7:
                            case 0x8:
                            case 0xa:
                                {
                                    short oldah = CPU_Regs.reg_eax.high();
                                    CPU_Regs.reg_eax.high(CPU_Regs.reg_eax.low());
                                    DOS_21Handler.call();
                                    CPU_Regs.reg_eax.high(oldah);
                                }
                                break;
                            default:
                                CPU_Regs.reg_eax.low(0);
                                break;
                        }
                    }
                    break;
                case 0x0d:
                    break;
                case 0x0e:
                    Dos_files.DOS_SetDefaultDrive(CPU_Regs.reg_edx.low());
                    CPU_Regs.reg_eax.low(Dos_files.DOS_DRIVES);
                    break;
                case 0x0f:
                    if (Dos_files.DOS_FCBOpen((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word())) {
                        CPU_Regs.reg_eax.low(0);
                    } else {
                        CPU_Regs.reg_eax.low(0xff);
                    }
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x0f FCB-fileopen used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x10:
                    if (Dos_files.DOS_FCBClose((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word())) {
                        CPU_Regs.reg_eax.low(0);
                    } else {
                        CPU_Regs.reg_eax.low(0xff);
                    }
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x10 FCB-fileclose used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x11:
                    if (Dos_files.DOS_FCBFindFirst((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word())) CPU_Regs.reg_eax.low(0x00); else CPU_Regs.reg_eax.low(0xFF);
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x11 FCB-FindFirst used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x12:
                    if (Dos_files.DOS_FCBFindNext((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word())) CPU_Regs.reg_eax.low(0x00); else CPU_Regs.reg_eax.low(0xFF);
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x12 FCB-FindNext used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x13:
                    if (Dos_files.DOS_FCBDeleteFile((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word())) CPU_Regs.reg_eax.low(0x00); else CPU_Regs.reg_eax.low(0xFF);
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x16 FCB-Delete used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x14:
                    CPU_Regs.reg_eax.low(Dos_files.DOS_FCBRead((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word(), 0));
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x14 FCB-Read used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x15:
                    CPU_Regs.reg_eax.low(Dos_files.DOS_FCBWrite((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word(), 0));
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x15 FCB-Write used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x16:
                    if (Dos_files.DOS_FCBCreate((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word())) CPU_Regs.reg_eax.low(0x00); else CPU_Regs.reg_eax.low(0xFF);
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x16 FCB-Create used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x17:
                    if (Dos_files.DOS_FCBRenameFile((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word())) CPU_Regs.reg_eax.low(0x00); else CPU_Regs.reg_eax.low(0xFF);
                    break;
                case 0x1b:
                    {
                        IntRef cx = new IntRef(CPU_Regs.reg_ecx.word());
                        IntRef dx = new IntRef(CPU_Regs.reg_edx.word());
                        ShortRef al = new ShortRef(CPU_Regs.reg_eax.low());
                        if (!Dos_files.DOS_GetAllocationInfo((short) 0, cx, al, dx)) CPU_Regs.reg_eax.low(0xff); else {
                            CPU_Regs.reg_ecx.word(cx.value);
                            CPU_Regs.reg_edx.word(dx.value);
                            CPU_Regs.reg_eax.low(al.value);
                        }
                        break;
                    }
                case 0x1c:
                    {
                        IntRef cx = new IntRef(CPU_Regs.reg_ecx.word());
                        IntRef dx = new IntRef(CPU_Regs.reg_edx.word());
                        ShortRef al = new ShortRef(CPU_Regs.reg_eax.low());
                        if (!Dos_files.DOS_GetAllocationInfo(CPU_Regs.reg_edx.low(), cx, al, dx)) CPU_Regs.reg_eax.low(0xff); else {
                            CPU_Regs.reg_ecx.word(cx.value);
                            CPU_Regs.reg_edx.word(dx.value);
                            CPU_Regs.reg_eax.low(al.value);
                        }
                        break;
                    }
                case 0x21:
                    CPU_Regs.reg_eax.low(Dos_files.DOS_FCBRandomRead((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word(), 1, true));
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x21 FCB-Random read used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x22:
                    CPU_Regs.reg_eax.low(Dos_files.DOS_FCBRandomWrite((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word(), 1, true));
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x22 FCB-Random write used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x23:
                    if (Dos_files.DOS_FCBGetFileSize((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word())) CPU_Regs.reg_eax.low(0x00); else CPU_Regs.reg_eax.low(0xFF);
                    break;
                case 0x24:
                    Dos_files.DOS_FCBSetRandomRecord((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word());
                    break;
                case 0x27:
                    CPU_Regs.reg_eax.low(Dos_files.DOS_FCBRandomRead((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word(), CPU_Regs.reg_ecx.word(), false));
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x27 FCB-Random(block) read used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x28:
                    CPU_Regs.reg_eax.low(Dos_files.DOS_FCBRandomWrite((int) CPU.Segs_DSval, CPU_Regs.reg_edx.word(), CPU_Regs.reg_ecx.word(), false));
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:0x28 FCB-Random(block) write used, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x29:
                    {
                        ShortRef difference = new ShortRef();
                        String string;
                        string = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_esi.word(), 1023);
                        CPU_Regs.reg_eax.low(Dos_files.FCB_Parsename((int) CPU.Segs_ESval, CPU_Regs.reg_edi.word(), CPU_Regs.reg_eax.low(), string, difference));
                        CPU_Regs.reg_esi.word(CPU_Regs.reg_esi.word() + difference.value);
                    }
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_FCB, LogSeverities.LOG_NORMAL, "DOS:29:FCB Parse Filename, result:al=" + CPU_Regs.reg_eax.low());
                    break;
                case 0x19:
                    CPU_Regs.reg_eax.low(Dos_files.DOS_GetDefaultDrive());
                    break;
                case 0x1a:
                    dos.dta((int) CPU_Regs.RealMakeSegDS(CPU_Regs.reg_edx.word()));
                    break;
                case 0x25:
                    Memory.RealSetVec(CPU_Regs.reg_eax.low(), CPU_Regs.RealMakeSegDS(CPU_Regs.reg_edx.word()));
                    break;
                case 0x26:
                    Dos_execute.DOS_NewPSP(CPU_Regs.reg_edx.word(), new Dos_PSP(dos.psp()).GetSize());
                    break;
                case 0x2a:
                    {
                        int a = (14 - dos.date.month) / 12;
                        int y = dos.date.year - a;
                        int m = dos.date.month + 12 * a - 2;
                        CPU_Regs.reg_eax.low((dos.date.day + y + (y / 4) - (y / 100) + (y / 400) + (31 * m) / 12) % 7);
                        CPU_Regs.reg_ecx.word(dos.date.year);
                        CPU_Regs.reg_edx.high(dos.date.month);
                        CPU_Regs.reg_edx.low(dos.date.day);
                    }
                    break;
                case 0x2b:
                    if (CPU_Regs.reg_ecx.word() < 1980) {
                        CPU_Regs.reg_eax.low(0xff);
                        break;
                    }
                    if ((CPU_Regs.reg_edx.high() > 12) || (CPU_Regs.reg_edx.high() == 0)) {
                        CPU_Regs.reg_eax.low(0xff);
                        break;
                    }
                    if ((CPU_Regs.reg_edx.low() > 31) || (CPU_Regs.reg_edx.low() == 0)) {
                        CPU_Regs.reg_eax.low(0xff);
                        break;
                    }
                    dos.date.year = (short) CPU_Regs.reg_ecx.word();
                    dos.date.month = (byte) CPU_Regs.reg_edx.high();
                    dos.date.day = (byte) CPU_Regs.reg_edx.low();
                    CPU_Regs.reg_eax.low(0);
                    break;
                case 0x2c:
                    {
                        long ticks = 5 * (Memory.mem_readd(Bios.BIOS_TIMER) & 0xFFFFFFFFl);
                        ticks = ((ticks / 59659) << 16) + ((ticks % 59659) << 16) / 59659;
                        long seconds = (ticks / 100);
                        CPU_Regs.reg_ecx.high((short) (seconds / 3600));
                        CPU_Regs.reg_ecx.low((short) ((seconds % 3600) / 60));
                        CPU_Regs.reg_edx.high((short) (seconds % 60));
                        CPU_Regs.reg_edx.low((short) (ticks % 100));
                    }
                    overhead();
                    break;
                case 0x2d:
                    Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "DOS:Set System Time not supported");
                    if (CPU_Regs.reg_ecx.high() > 23 || CPU_Regs.reg_ecx.low() > 59 || CPU_Regs.reg_edx.high() > 59 || CPU_Regs.reg_edx.low() > 99) CPU_Regs.reg_eax.low(0xff); else CPU_Regs.reg_eax.low(0);
                    break;
                case 0x2e:
                    dos.verify = (CPU_Regs.reg_eax.low() == 1);
                    break;
                case 0x2f:
                    CPU_Regs.SegSet16ES(Memory.RealSeg(dos.dta()));
                    CPU_Regs.reg_ebx.word(Memory.RealOff(dos.dta()));
                    break;
                case 0x30:
                    if (CPU_Regs.reg_eax.low() == 0) CPU_Regs.reg_ebx.high(0xFF);
                    if (CPU_Regs.reg_eax.low() == 1) CPU_Regs.reg_ebx.high(0x10);
                    CPU_Regs.reg_eax.low(dos.version.major);
                    CPU_Regs.reg_eax.high(dos.version.minor);
                    CPU_Regs.reg_ebx.low(0x00);
                    CPU_Regs.reg_ecx.word(0x0000);
                    break;
                case 0x31:
                    {
                        IntRef dx = new IntRef(CPU_Regs.reg_edx.word());
                        Dos_memory.DOS_ResizeMemory(dos.psp(), dx);
                        Dos_execute.DOS_Terminate(dos.psp(), true, CPU_Regs.reg_eax.low());
                        break;
                    }
                case 0x1f:
                case 0x32:
                    {
                        short drive = CPU_Regs.reg_edx.low();
                        if (drive == 0 || CPU_Regs.reg_eax.high() == 0x1f) drive = Dos_files.DOS_GetDefaultDrive(); else drive--;
                        if (Dos_files.Drives[drive] != null) {
                            CPU_Regs.reg_eax.low(0x00);
                            CPU_Regs.SegSet16DS(dos.tables.dpb);
                            CPU_Regs.reg_ebx.word(drive);
                            Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "Get drive parameter block.");
                        } else {
                            CPU_Regs.reg_eax.low(0xff);
                        }
                    }
                    break;
                case 0x33:
                    switch(CPU_Regs.reg_eax.low()) {
                        case 0:
                            CPU_Regs.reg_edx.low(dos.breakcheck ? 1 : 0);
                            break;
                        case 1:
                            dos.breakcheck = (CPU_Regs.reg_edx.low() > 0);
                            break;
                        case 2:
                            {
                                boolean old = dos.breakcheck;
                                dos.breakcheck = (CPU_Regs.reg_edx.low() > 0);
                                CPU_Regs.reg_edx.low(old ? 1 : 0);
                            }
                            break;
                        case 3:
                        case 4:
                            if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "Someone playing with cpsw " + Integer.toString(CPU_Regs.reg_eax.word(), 16));
                            break;
                        case 5:
                            CPU_Regs.reg_edx.low(3);
                            break;
                        case 6:
                            CPU_Regs.reg_ebx.low(dos.version.major);
                            CPU_Regs.reg_ebx.high(dos.version.minor);
                            CPU_Regs.reg_edx.low(dos.version.revision);
                            CPU_Regs.reg_edx.high(0x10);
                            break;
                        default:
                            Log.exit("DOS:Illegal 0x33 Call " + Integer.toString(CPU_Regs.reg_eax.low(), 16));
                    }
                    break;
                case 0x34:
                    CPU_Regs.SegSet16ES(DOS_SDA_SEG);
                    CPU_Regs.reg_ebx.word(DOS_SDA_OFS + 0x01);
                    break;
                case 0x35:
                    CPU_Regs.reg_ebx.word(Memory.real_readw(0, ((int) CPU_Regs.reg_eax.low()) * 4));
                    CPU_Regs.SegSet16ES(Memory.real_readw(0, ((int) CPU_Regs.reg_eax.low()) * 4 + 2));
                    break;
                case 0x36:
                    {
                        IntRef bytes = new IntRef(0);
                        ShortRef sectors = new ShortRef();
                        IntRef clusters = new IntRef(0), free = new IntRef(0);
                        if (Dos_files.DOS_GetFreeDiskSpace(CPU_Regs.reg_edx.low(), bytes, sectors, clusters, free)) {
                            CPU_Regs.reg_eax.word(sectors.value);
                            CPU_Regs.reg_ebx.word(free.value);
                            CPU_Regs.reg_ecx.word(bytes.value);
                            CPU_Regs.reg_edx.word(clusters.value);
                        } else {
                            short drive = CPU_Regs.reg_edx.low();
                            if (drive == 0) drive = Dos_files.DOS_GetDefaultDrive(); else drive--;
                            if (drive < 2) {
                            }
                            CPU_Regs.reg_eax.word(0xffff);
                        }
                    }
                    break;
                case 0x37:
                    switch(CPU_Regs.reg_eax.low()) {
                        case 0:
                            CPU_Regs.reg_eax.low(0);
                            CPU_Regs.reg_edx.low(0x2f);
                            break;
                        case 1:
                            CPU_Regs.reg_eax.low(0);
                            break;
                        case 2:
                            CPU_Regs.reg_eax.low(0);
                            CPU_Regs.reg_edx.low(0x2f);
                            break;
                        case 3:
                            CPU_Regs.reg_eax.low(0);
                            break;
                    }
                    Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_ERROR, "DOS:0x37:Call for not supported switchchar");
                    break;
                case 0x38:
                    if (CPU_Regs.reg_eax.low() == 0) {
                        int dest = CPU.Segs_DSphys + CPU_Regs.reg_edx.word();
                        Memory.MEM_BlockWrite(dest, dos.tables.country, 0x18);
                        CPU_Regs.reg_eax.word(0x01);
                        CPU_Regs.reg_ebx.word(0x01);
                        Callback.CALLBACK_SCF(false);
                        break;
                    } else {
                        Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_ERROR, "DOS:Setting country code not supported");
                    }
                    Callback.CALLBACK_SCF(true);
                    break;
                case 0x39:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        if (Dos_files.DOS_MakeDir(name1)) {
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x3a:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        if (Dos_files.DOS_RemoveDir(name1)) {
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                            if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_NORMAL, "Remove dir failed on " + name1 + " with error " + Integer.toString(dos.errorcode));
                        }
                        break;
                    }
                case 0x3b:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        if (Dos_files.DOS_ChangeDir(name1)) {
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x3c:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        IntRef ax = new IntRef(CPU_Regs.reg_eax.word());
                        if (Dos_files.DOS_CreateFile(name1, CPU_Regs.reg_ecx.word(), ax)) {
                            CPU_Regs.reg_eax.word(ax.value);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x3d:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        IntRef ax = new IntRef(CPU_Regs.reg_eax.word());
                        if (Dos_files.DOS_OpenFile(name1, CPU_Regs.reg_eax.low(), ax)) {
                            CPU_Regs.reg_eax.word(ax.value);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x3e:
                    if (Dos_files.DOS_CloseFile(CPU_Regs.reg_ebx.word())) {
                        Callback.CALLBACK_SCF(false);
                    } else {
                        CPU_Regs.reg_eax.word(dos.errorcode);
                        Callback.CALLBACK_SCF(true);
                    }
                    break;
                case 0x3f:
                    {
                        IntRef toread = new IntRef(CPU_Regs.reg_ecx.word());
                        dos.echo = true;
                        if (Dos_files.DOS_ReadFile(CPU_Regs.reg_ebx.word(), dos_copybuf, toread)) {
                            Memory.MEM_BlockWrite(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), dos_copybuf, toread.value);
                            CPU_Regs.reg_eax.word(toread.value);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        modify_cycles(CPU_Regs.reg_eax.word());
                        dos.echo = false;
                        break;
                    }
                case 0x40:
                    {
                        IntRef towrite = new IntRef(CPU_Regs.reg_ecx.word());
                        Memory.MEM_BlockRead(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), dos_copybuf, towrite.value);
                        if (Dos_files.DOS_WriteFile(CPU_Regs.reg_ebx.word(), dos_copybuf, towrite)) {
                            CPU_Regs.reg_eax.word(towrite.value);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        modify_cycles(CPU_Regs.reg_eax.word());
                        break;
                    }
                case 0x41:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        if (Dos_files.DOS_UnlinkFile(name1)) {
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x42:
                    {
                        LongRef pos = new LongRef((((long) CPU_Regs.reg_ecx.word() << 16) + CPU_Regs.reg_edx.word()) & 0xFFFFFFFFl);
                        if (Dos_files.DOS_SeekFile(CPU_Regs.reg_ebx.word(), pos, CPU_Regs.reg_eax.low())) {
                            CPU_Regs.reg_edx.word((int) (pos.value >>> 16));
                            CPU_Regs.reg_eax.word((int) (pos.value & 0xFFFF));
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x43:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        switch(CPU_Regs.reg_eax.low()) {
                            case 0x00:
                                {
                                    IntRef attr_val = new IntRef(CPU_Regs.reg_ecx.word());
                                    if (Dos_files.DOS_GetFileAttr(name1, attr_val)) {
                                        CPU_Regs.reg_ecx.word(attr_val.value);
                                        CPU_Regs.reg_eax.word(attr_val.value);
                                        Callback.CALLBACK_SCF(false);
                                    } else {
                                        Callback.CALLBACK_SCF(true);
                                        CPU_Regs.reg_eax.word(dos.errorcode);
                                    }
                                    break;
                                }
                            case 0x01:
                                if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_ERROR, "DOS:Set File Attributes for " + name1 + " not supported");
                                if (Dos_files.DOS_SetFileAttr(name1, CPU_Regs.reg_ecx.word())) {
                                    CPU_Regs.reg_eax.word(0x202);
                                    Callback.CALLBACK_SCF(false);
                                } else {
                                    Callback.CALLBACK_SCF(true);
                                    CPU_Regs.reg_eax.word(dos.errorcode);
                                }
                                break;
                            default:
                                if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_ERROR, "DOS:0x43:Illegal subfunction " + Integer.toString(CPU_Regs.reg_eax.low(), 16));
                                CPU_Regs.reg_eax.word(1);
                                Callback.CALLBACK_SCF(true);
                                break;
                        }
                        break;
                    }
                case 0x44:
                    if (Dos_ioctl.DOS_IOCTL()) {
                        Callback.CALLBACK_SCF(false);
                    } else {
                        CPU_Regs.reg_eax.word(dos.errorcode);
                        Callback.CALLBACK_SCF(true);
                    }
                    break;
                case 0x45:
                    {
                        IntRef ax = new IntRef(CPU_Regs.reg_eax.word());
                        if (Dos_files.DOS_DuplicateEntry(CPU_Regs.reg_ebx.word(), ax)) {
                            CPU_Regs.reg_eax.word(ax.value);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x46:
                    if (Dos_files.DOS_ForceDuplicateEntry(CPU_Regs.reg_ebx.word(), CPU_Regs.reg_ecx.word())) {
                        CPU_Regs.reg_eax.word(CPU_Regs.reg_ecx.word());
                        Callback.CALLBACK_SCF(false);
                    } else {
                        CPU_Regs.reg_eax.word(dos.errorcode);
                        Callback.CALLBACK_SCF(true);
                    }
                    break;
                case 0x47:
                    {
                        StringRef name1 = new StringRef();
                        if (Dos_files.DOS_GetCurrentDir(CPU_Regs.reg_edx.low(), name1)) {
                            Memory.MEM_BlockWrite(CPU.Segs_DSphys + CPU_Regs.reg_esi.word(), name1.value, (int) (name1.value.length() + 1));
                            CPU_Regs.reg_eax.word(0x0100);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x48:
                    {
                        IntRef size = new IntRef(CPU_Regs.reg_ebx.word());
                        IntRef seg = new IntRef(0);
                        if (Dos_memory.DOS_AllocateMemory(seg, size)) {
                            CPU_Regs.reg_eax.word(seg.value);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            CPU_Regs.reg_ebx.word(size.value);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x49:
                    if (Dos_memory.DOS_FreeMemory((int) CPU.Segs_ESval)) {
                        Callback.CALLBACK_SCF(false);
                    } else {
                        CPU_Regs.reg_eax.word(dos.errorcode);
                        Callback.CALLBACK_SCF(true);
                    }
                    break;
                case 0x4a:
                    {
                        IntRef size = new IntRef(CPU_Regs.reg_ebx.word());
                        if (Dos_memory.DOS_ResizeMemory((int) CPU.Segs_ESval, size)) {
                            CPU_Regs.reg_eax.word((int) CPU.Segs_ESval);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            CPU_Regs.reg_ebx.word(size.value);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x4b:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_EXEC, LogSeverities.LOG_ERROR, "Execute " + name1 + " " + CPU_Regs.reg_eax.low());
                        if (!Dos_execute.DOS_Execute(name1, CPU.Segs_ESphys + CPU_Regs.reg_ebx.word(), CPU_Regs.reg_eax.low())) {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                    }
                    break;
                case 0x4c:
                    Dos_execute.DOS_Terminate(dos.psp(), false, CPU_Regs.reg_eax.low());
                    break;
                case 0x4d:
                    CPU_Regs.reg_eax.low(dos.return_code);
                    CPU_Regs.reg_eax.high(dos.return_mode);
                    break;
                case 0x4e:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        if (Dos_files.DOS_FindFirst(name1, CPU_Regs.reg_ecx.word())) {
                            Callback.CALLBACK_SCF(false);
                            CPU_Regs.reg_eax.word(0);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x4f:
                    if (Dos_files.DOS_FindNext()) {
                        Callback.CALLBACK_SCF(false);
                        CPU_Regs.reg_eax.word(0);
                    } else {
                        CPU_Regs.reg_eax.word(dos.errorcode);
                        Callback.CALLBACK_SCF(true);
                    }
                    break;
                case 0x50:
                    dos.psp(CPU_Regs.reg_ebx.word());
                    break;
                case 0x51:
                    CPU_Regs.reg_ebx.word(dos.psp());
                    break;
                case 0x52:
                    {
                        int addr = dos_infoblock.GetPointer();
                        CPU_Regs.SegSet16ES(Memory.RealSeg(addr));
                        CPU_Regs.reg_ebx.word(Memory.RealOff(addr));
                        Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_NORMAL, "Call is made for list of lists - let's hope for the best");
                        break;
                    }
                case 0x53:
                    Log.exit("Unhandled Dos 21 call " + Integer.toString(CPU_Regs.reg_eax.high(), 16));
                    break;
                case 0x54:
                    CPU_Regs.reg_eax.low(dos.verify ? 1 : 0);
                    break;
                case 0x55:
                    Dos_execute.DOS_ChildPSP(CPU_Regs.reg_edx.word(), CPU_Regs.reg_esi.word());
                    dos.psp(CPU_Regs.reg_edx.word());
                    break;
                case 0x56:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        String name2 = Memory.MEM_StrCopy(CPU.Segs_ESphys + CPU_Regs.reg_edi.word(), 256);
                        if (Dos_files.DOS_Rename(name1, name2)) {
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x57:
                    if (CPU_Regs.reg_eax.low() == 0x00) {
                        IntRef cx = new IntRef(CPU_Regs.reg_ecx.word());
                        IntRef dx = new IntRef(CPU_Regs.reg_edx.word());
                        if (Dos_files.DOS_GetFileDate(CPU_Regs.reg_ebx.word(), cx, dx)) {
                            CPU_Regs.reg_ecx.word(cx.value);
                            CPU_Regs.reg_edx.word(dx.value);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            Callback.CALLBACK_SCF(true);
                        }
                    } else if (CPU_Regs.reg_eax.low() == 0x01) {
                        Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "DOS:57:Set File Date Time Faked");
                        Callback.CALLBACK_SCF(false);
                    } else {
                        if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "DOS:57:Unsupported subtion " + Integer.toString(CPU_Regs.reg_eax.low(), 16));
                    }
                    break;
                case 0x58:
                    switch(CPU_Regs.reg_eax.low()) {
                        case 0:
                            CPU_Regs.reg_eax.word(Dos_memory.DOS_GetMemAllocStrategy());
                            break;
                        case 1:
                            if (Dos_memory.DOS_SetMemAllocStrategy(CPU_Regs.reg_ebx.word())) Callback.CALLBACK_SCF(false); else {
                                CPU_Regs.reg_eax.word(1);
                                Callback.CALLBACK_SCF(true);
                            }
                            break;
                        case 2:
                            CPU_Regs.reg_eax.low(dos_infoblock.GetUMBChainState() & 1);
                            Callback.CALLBACK_SCF(false);
                            break;
                        case 3:
                            if (Dos_memory.DOS_LinkUMBsToMemChain(CPU_Regs.reg_ebx.word())) Callback.CALLBACK_SCF(false); else {
                                CPU_Regs.reg_eax.word(1);
                                Callback.CALLBACK_SCF(true);
                            }
                            break;
                        default:
                            if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "DOS:58:Not Supported Set//Get memory allocation call " + Integer.toString(CPU_Regs.reg_eax.low(), 16));
                            CPU_Regs.reg_eax.word(1);
                            Callback.CALLBACK_SCF(true);
                    }
                    break;
                case 0x59:
                    CPU_Regs.reg_eax.word(dos.errorcode);
                    if (dos.errorcode == DOSERR_FILE_NOT_FOUND || dos.errorcode == DOSERR_PATH_NOT_FOUND) {
                        CPU_Regs.reg_ebx.high(8);
                    } else {
                        CPU_Regs.reg_ebx.high(0);
                    }
                    CPU_Regs.reg_ebx.low(1);
                    CPU_Regs.reg_ecx.high(0);
                    break;
                case 0x5a:
                    {
                        IntRef handle = new IntRef(0);
                        StringRef name1 = new StringRef(Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256));
                        if (Dos_files.DOS_CreateTempFile(name1, handle)) {
                            CPU_Regs.reg_eax.word(handle.value);
                            Memory.MEM_BlockWrite(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), name1.value, (int) (name1.value.length() + 1));
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                    }
                    break;
                case 0x5b:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_edx.word(), 256);
                        IntRef handle = new IntRef(0);
                        if (Dos_files.DOS_OpenFile(name1, 0, handle)) {
                            Dos_files.DOS_CloseFile(handle.value);
                            DOS_SetError(DOSERR_FILE_ALREADY_EXISTS);
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                            break;
                        }
                        if (Dos_files.DOS_CreateFile(name1, CPU_Regs.reg_ecx.word(), handle)) {
                            CPU_Regs.reg_eax.word(handle.value);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x5c:
                    DOS_SetError(DOSERR_FUNCTION_NUMBER_INVALID);
                    CPU_Regs.reg_eax.word(dos.errorcode);
                    Callback.CALLBACK_SCF(true);
                    break;
                case 0x5d:
                    if (CPU_Regs.reg_eax.low() == 0x06) {
                        CPU_Regs.SegSet16DS(DOS_SDA_SEG);
                        CPU_Regs.reg_esi.word(DOS_SDA_OFS);
                        CPU_Regs.reg_ecx.word(0x80);
                        CPU_Regs.reg_edx.word(0x1a);
                        Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "Get SDA, Let's hope for the best!");
                    }
                    break;
                case 0x5f:
                    CPU_Regs.reg_eax.word(0x0001);
                    Callback.CALLBACK_SCF(true);
                    break;
                case 0x60:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_esi.word(), 256);
                        StringRef name2 = new StringRef();
                        if (Dos_files.DOS_Canonicalize(name1, name2)) {
                            Memory.MEM_BlockWrite(CPU.Segs_ESphys + CPU_Regs.reg_edi.word(), name2.value, (int) name2.value.length() + 1);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x62:
                    CPU_Regs.reg_ebx.word(dos.psp());
                    break;
                case 0x63:
                    if (CPU_Regs.reg_eax.low() == 0) {
                        CPU_Regs.SegSet16DS(Memory.RealSeg(dos.tables.dbcs));
                        CPU_Regs.reg_esi.word(Memory.RealOff(dos.tables.dbcs));
                        CPU_Regs.reg_eax.low(0);
                        Callback.CALLBACK_SCF(false);
                    } else CPU_Regs.reg_eax.low(0xff);
                    break;
                case 0x64:
                    Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_NORMAL, "set driver look ahead flag");
                    break;
                case 0x65:
                    {
                        if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "DOS:65:Extended country information call " + Integer.toString(CPU_Regs.reg_eax.word(), 16));
                        if ((CPU_Regs.reg_eax.low() <= 0x07) && (CPU_Regs.reg_ecx.word() < 0x05)) {
                            DOS_SetError(DOSERR_FUNCTION_NUMBER_INVALID);
                            Callback.CALLBACK_SCF(true);
                            break;
                        }
                        int len = 0;
                        int data = CPU.Segs_ESphys + CPU_Regs.reg_edi.word();
                        switch(CPU_Regs.reg_eax.low()) {
                            case 0x01:
                                Memory.mem_writeb(data + 0x00, CPU_Regs.reg_eax.low());
                                Memory.mem_writew(data + 0x01, 0x26);
                                Memory.mem_writew(data + 0x03, 1);
                                if (CPU_Regs.reg_ecx.word() > 0x06) Memory.mem_writew(data + 0x05, dos.loaded_codepage);
                                if (CPU_Regs.reg_ecx.word() > 0x08) {
                                    int amount = (CPU_Regs.reg_ecx.word() >= 0x29) ? 0x22 : (CPU_Regs.reg_ecx.word() - 7);
                                    Memory.MEM_BlockWrite(data + 0x07, dos.tables.country, amount);
                                    CPU_Regs.reg_ecx.word((CPU_Regs.reg_ecx.word() >= 0x29) ? 0x29 : CPU_Regs.reg_ecx.word());
                                }
                                Callback.CALLBACK_SCF(false);
                                break;
                            case 0x05:
                                Memory.mem_writeb(data + 0x00, CPU_Regs.reg_eax.low());
                                Memory.mem_writed(data + 0x01, dos.tables.filenamechar);
                                CPU_Regs.reg_ecx.word(5);
                                Callback.CALLBACK_SCF(false);
                                break;
                            case 0x02:
                                Memory.mem_writeb(data + 0x00, CPU_Regs.reg_eax.low());
                                Memory.mem_writed(data + 0x01, dos.tables.upcase);
                                CPU_Regs.reg_ecx.word(5);
                                Callback.CALLBACK_SCF(false);
                                break;
                            case 0x06:
                                Memory.mem_writeb(data + 0x00, CPU_Regs.reg_eax.low());
                                Memory.mem_writed(data + 0x01, dos.tables.collatingseq);
                                CPU_Regs.reg_ecx.word(5);
                                Callback.CALLBACK_SCF(false);
                                break;
                            case 0x03:
                            case 0x04:
                            case 0x07:
                                Memory.mem_writeb(data + 0x00, CPU_Regs.reg_eax.low());
                                Memory.mem_writed(data + 0x01, dos.tables.dbcs);
                                CPU_Regs.reg_ecx.word(5);
                                Callback.CALLBACK_SCF(false);
                                break;
                            case 0x20:
                                {
                                    int in = CPU_Regs.reg_edx.low();
                                    int out = String.valueOf((char) in).toUpperCase().charAt(0);
                                    CPU_Regs.reg_edx.low((short) out);
                                }
                                Callback.CALLBACK_SCF(false);
                                break;
                            case 0x21:
                            case 0x22:
                                data = CPU.Segs_DSphys + CPU_Regs.reg_edx.word();
                                if (CPU_Regs.reg_eax.low() == 0x21) len = CPU_Regs.reg_ecx.word(); else len = Memory.mem_strlen(data);
                                if (len > DOS_COPYBUFSIZE - 1) Log.exit("DOS:0x65 Buffer overflow");
                                if (len > 0) {
                                    Memory.MEM_BlockRead(data, dos_copybuf, len);
                                    System.arraycopy(new String(dos_copybuf, 0, len).toUpperCase().getBytes(), 0, dos_copybuf, 0, len);
                                    Memory.MEM_BlockWrite(data, dos_copybuf, len);
                                }
                                Callback.CALLBACK_SCF(false);
                                break;
                            default:
                                Log.exit("DOS:0x65:Unhandled country information call " + Integer.toString(CPU_Regs.reg_eax.low(), 16));
                        }
                        break;
                    }
                case 0x66:
                    if (CPU_Regs.reg_eax.low() == 1) {
                        Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "Getting global code page table");
                        CPU_Regs.reg_ebx.word(dos.loaded_codepage);
                        CPU_Regs.reg_edx.word(dos.loaded_codepage);
                        Callback.CALLBACK_SCF(false);
                        break;
                    }
                    Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_NORMAL, "DOS:Setting code page table is not supported");
                    break;
                case 0x67:
                    {
                        Dos_PSP psp = new Dos_PSP(dos.psp());
                        psp.SetNumFiles(CPU_Regs.reg_ebx.word());
                        Callback.CALLBACK_SCF(false);
                        break;
                    }
                case 0x68:
                    if (Dos_files.DOS_FlushFile(CPU_Regs.reg_ebx.low())) {
                        Callback.CALLBACK_SCF(false);
                    } else {
                        CPU_Regs.reg_eax.word(dos.errorcode);
                        Callback.CALLBACK_SCF(true);
                    }
                    break;
                case 0x69:
                    {
                        switch(CPU_Regs.reg_eax.low()) {
                            case 0x00:
                                Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "DOS:Get Disk serial number");
                                Callback.CALLBACK_SCF(true);
                                break;
                            case 0x01:
                                Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "DOS:Set Disk serial number");
                            default:
                                Log.exit("DOS:Illegal Get Serial Number call " + Integer.toString(CPU_Regs.reg_eax.low(), 16));
                        }
                        break;
                    }
                case 0x6c:
                    {
                        String name1 = Memory.MEM_StrCopy(CPU.Segs_DSphys + CPU_Regs.reg_esi.word(), 256);
                        IntRef ax = new IntRef(CPU_Regs.reg_eax.word());
                        IntRef cx = new IntRef(CPU_Regs.reg_ecx.word());
                        if (Dos_files.DOS_OpenFileExtended(name1, CPU_Regs.reg_ebx.word(), CPU_Regs.reg_ecx.word(), CPU_Regs.reg_edx.word(), ax, cx)) {
                            CPU_Regs.reg_eax.word(ax.value);
                            CPU_Regs.reg_ecx.word(cx.value);
                            Callback.CALLBACK_SCF(false);
                        } else {
                            CPU_Regs.reg_eax.word(dos.errorcode);
                            Callback.CALLBACK_SCF(true);
                        }
                        break;
                    }
                case 0x71:
                    CPU_Regs.reg_eax.word(0x7100);
                    Callback.CALLBACK_SCF(true);
                    if (Log.level <= LogSeverities.LOG_NORMAL) Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_NORMAL, "DOS:Windows long file name support call " + Integer.toString(CPU_Regs.reg_eax.low(), 16));
                    break;
                case 0xE0:
                case 0x18:
                case 0x1d:
                case 0x1e:
                case 0x20:
                case 0x6b:
                case 0x61:
                case 0xEF:
                case 0x5e:
                default:
                    if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_DOSMISC, LogSeverities.LOG_ERROR, "DOS:Unhandled call " + Integer.toString(CPU_Regs.reg_eax.high(), 16) + " al=" + Integer.toString(CPU_Regs.reg_eax.low(), 16) + ". Set al to default of 0");
                    CPU_Regs.reg_eax.low(0x00);
                    break;
            }
            return Callback.CBRET_NONE;
        }
