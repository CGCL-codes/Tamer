    public static boolean DOS_Execute(String name, int block_pt, short flags) {
        EXE_Header head = new EXE_Header();
        int i;
        IntRef fhandle = new IntRef(0);
        int loadseg;
        int loadaddress;
        int headersize = 0, imagesize = 0;
        Dos_ParamBlock block = new Dos_ParamBlock(block_pt);
        block.LoadData();
        if ((flags & 0x80) != 0) Log.log(LogTypes.LOG_EXEC, LogSeverities.LOG_ERROR, "using loadhigh flag!!!!!. dropping it");
        flags &= 0x7f;
        if (flags != LOADNGO && flags != OVERLAY && flags != LOAD) {
            Dos.DOS_SetError(Dos.DOSERR_FORMAT_INVALID);
            return false;
        }
        boolean iscom = false;
        if (!Dos_files.DOS_OpenFile(name, Dos_files.OPEN_READ, fhandle)) {
            Dos.DOS_SetError(Dos.DOSERR_FILE_NOT_FOUND);
            return false;
        }
        if (Dos_files.Files[Dos.RealHandle(fhandle.value)] instanceof Drive_local.localFile) {
            String path = ((Drive_local.localFile) Dos_files.Files[Dos.RealHandle(fhandle.value)]).GetPath();
            if (winRun(path)) {
                return true;
            }
        } else if (Dos_files.Files[Dos.RealHandle(fhandle.value)] instanceof Drive_fat.fatFile) {
            Drive_fat.fatFile file = (Drive_fat.fatFile) Dos_files.Files[Dos.RealHandle(fhandle.value)];
            if (winRun(file.myDrive, file, "C:\\" + file.myDrive.curdir + file.name)) {
                return true;
            }
        }
        IntRef len = new IntRef(EXE_Header.size);
        byte[] hd = new byte[len.value];
        if (!Dos_files.DOS_ReadFile(fhandle.value, hd, len)) {
            Dos_files.DOS_CloseFile(fhandle.value);
            return false;
        }
        if (len.value < EXE_Header.size) {
            if (len.value == 0) {
                Dos.DOS_SetError(Dos.DOSERR_ACCESS_DENIED);
                Dos_files.DOS_CloseFile(fhandle.value);
                return false;
            }
            iscom = true;
        } else {
            head.fill(hd);
            if ((head.signature != MAGIC1) && (head.signature != MAGIC2)) iscom = true; else {
                if ((head.pages & ~0x07ff) != 0) Log.log(LogTypes.LOG_EXEC, LogSeverities.LOG_NORMAL, "Weird header: head.pages > 1 MB");
                head.pages &= 0x07ff;
                headersize = head.headersize * 16;
                imagesize = head.pages * 512 - headersize;
                if (imagesize + headersize < 512) imagesize = 512 - headersize;
            }
        }
        byte[] loadbuf = new byte[0x10000];
        IntRef envseg = new IntRef(block.exec.envseg);
        IntRef pspseg = new IntRef(0);
        IntRef memsize = new IntRef(0);
        if (flags != OVERLAY) {
            if (!MakeEnv(name, envseg)) {
                Dos_files.DOS_CloseFile(fhandle.value);
                return false;
            }
            int minsize;
            IntRef maxsize = new IntRef(0);
            IntRef maxfree = new IntRef(0xffff);
            Dos_memory.DOS_AllocateMemory(pspseg, maxfree);
            if (iscom) {
                minsize = 0x1000;
                maxsize.value = 0xffff;
                if (Dosbox.machine == MachineType.MCH_PCJR) {
                    LongRef pos = new LongRef(0);
                    Dos_files.DOS_SeekFile(fhandle.value, pos, Dos_files.DOS_SEEK_SET);
                    IntRef dataread = new IntRef(0x1800);
                    Dos_files.DOS_ReadFile(fhandle.value, loadbuf, dataread);
                    if (dataread.value < 0x1800) maxsize.value = dataread.value;
                    if (minsize > maxsize.value) minsize = maxsize.value;
                }
            } else {
                minsize = long2para(imagesize + (head.minmemory << 4) + 256);
                if (head.maxmemory != 0) maxsize.value = long2para(imagesize + (head.maxmemory << 4) + 256); else maxsize.value = 0xffff;
            }
            if (maxfree.value < minsize) {
                if (iscom) {
                    LongRef pos = new LongRef(0);
                    Dos_files.DOS_SeekFile(fhandle.value, pos, Dos_files.DOS_SEEK_SET);
                    IntRef dataread = new IntRef(0xf800);
                    Dos_files.DOS_ReadFile(fhandle.value, loadbuf, dataread);
                    if (dataread.value < 0xf800) minsize = ((dataread.value + 0x10) >> 4) + 0x20;
                }
                if (maxfree.value < minsize) {
                    Dos_files.DOS_CloseFile(fhandle.value);
                    Dos.DOS_SetError(Dos.DOSERR_INSUFFICIENT_MEMORY);
                    Dos_memory.DOS_FreeMemory(envseg.value);
                    return false;
                }
            }
            if (maxfree.value < maxsize.value) memsize.value = maxfree.value; else memsize.value = maxsize.value;
            if (!Dos_memory.DOS_AllocateMemory(pspseg, memsize)) Log.exit("DOS:Exec error in memory");
            if (iscom && (Dosbox.machine == MachineType.MCH_PCJR) && (pspseg.value < 0x2000)) {
                maxsize.value = 0xffff;
                Dos_memory.DOS_ResizeMemory(pspseg.value, maxsize);
                if ((Memory.real_readb(0x2000, 0) == 0x5a) && (Memory.real_readw(0x2000, 1) == 0) && (Memory.real_readw(0x2000, 3) == 0x7ffe)) {
                    if (pspseg.value + maxsize.value == 0x17ff) {
                        Dos_MCB cmcb = new Dos_MCB(pspseg.value - 1);
                        cmcb.SetType((short) 0x5a);
                    }
                }
            }
            loadseg = pspseg.value + 16;
            if (!iscom) {
                if ((head.minmemory == 0) && (head.maxmemory == 0)) loadseg = (int) (((pspseg.value + memsize.value) * 0x10 - imagesize) / 0x10);
            }
        } else loadseg = block.overlay.loadseg;
        loadaddress = Memory.PhysMake(loadseg, 0);
        if (iscom) {
            LongRef pos = new LongRef(0);
            Dos_files.DOS_SeekFile(fhandle.value, pos, Dos_files.DOS_SEEK_SET);
            IntRef readsize = new IntRef(0xffff - 256);
            Dos_files.DOS_ReadFile(fhandle.value, loadbuf, readsize);
            Memory.MEM_BlockWrite(loadaddress, loadbuf, readsize.value);
        } else {
            LongRef pos = new LongRef(headersize);
            Dos_files.DOS_SeekFile(fhandle.value, pos, Dos_files.DOS_SEEK_SET);
            while (imagesize > 0x7FFF) {
                IntRef readsize = new IntRef(0x8000);
                Dos_files.DOS_ReadFile(fhandle.value, loadbuf, readsize);
                Memory.MEM_BlockWrite(loadaddress, loadbuf, readsize.value);
                loadaddress += 0x8000;
                imagesize -= 0x8000;
            }
            if (imagesize > 0) {
                IntRef readsize = new IntRef(imagesize);
                Dos_files.DOS_ReadFile(fhandle.value, loadbuf, readsize);
                Memory.MEM_BlockWrite(loadaddress, loadbuf, readsize.value);
            }
            int relocate;
            if (flags == OVERLAY) relocate = block.overlay.relocation; else relocate = loadseg;
            pos.value = head.reloctable;
            Dos_files.DOS_SeekFile(fhandle.value, pos, 0);
            for (i = 0; i < head.relocations; i++) {
                byte[] d = new byte[4];
                IntRef readsize = new IntRef(4);
                Dos_files.DOS_ReadFile(fhandle.value, d, readsize);
                int relocpt = new Ptr(d, 0).readd(0);
                int address = Memory.PhysMake(Memory.RealSeg(relocpt) + loadseg, Memory.RealOff(relocpt));
                Memory.mem_writew(address, Memory.mem_readw(address) + relocate);
            }
        }
        Dos_files.DOS_CloseFile(fhandle.value);
        if (flags != OVERLAY) {
            SetupPSP(pspseg.value, memsize.value, envseg.value);
            SetupCMDLine(pspseg.value, block);
        }
        Callback.CALLBACK_SCF(false);
        if (flags == OVERLAY) return true;
        int csip, sssp;
        if (iscom) {
            csip = Memory.RealMake(pspseg.value, 0x100);
            sssp = Memory.RealMake(pspseg.value, 0xfffe);
            Memory.mem_writew(Memory.PhysMake(pspseg.value, 0xfffe), 0);
        } else {
            csip = Memory.RealMake(loadseg + head.initCS, head.initIP);
            sssp = Memory.RealMake(loadseg + head.initSS, head.initSP);
            if (head.initSP < 4) Log.log(LogTypes.LOG_EXEC, LogSeverities.LOG_ERROR, "stack underflow/wrap at EXEC");
        }
        if (flags == LOAD) {
            SaveRegisters();
            Dos_PSP callpsp = new Dos_PSP(Dos.dos.psp());
            callpsp.SetStack(CPU_Regs.RealMakeSegSS(CPU_Regs.reg_esp.word()));
            CPU_Regs.reg_esp.word(CPU_Regs.reg_esp.word() + 18);
            Dos.dos.psp(pspseg.value);
            Dos_PSP newpsp = new Dos_PSP(Dos.dos.psp());
            Dos.dos.dta((int) Memory.RealMake(newpsp.GetSegment(), 0x80));
            Memory.real_writew(Memory.RealSeg(sssp - 2), Memory.RealOff(sssp - 2), 0xffff);
            block.exec.initsssp = sssp - 2;
            block.exec.initcsip = csip;
            block.SaveData();
            return true;
        }
        if (flags == LOADNGO) {
            if ((CPU_Regs.reg_esp.word() > 0xfffe) || (CPU_Regs.reg_esp.word() < 18)) Log.log(LogTypes.LOG_EXEC, LogSeverities.LOG_ERROR, "stack underflow/wrap at EXEC");
            Memory.RealSetVec(0x22, Memory.RealMake(Memory.mem_readw(CPU.Segs_SSphys + CPU_Regs.reg_esp.word() + 2), Memory.mem_readw(CPU.Segs_SSphys + CPU_Regs.reg_esp.word())));
            SaveRegisters();
            Dos_PSP callpsp = new Dos_PSP(Dos.dos.psp());
            callpsp.SetStack(CPU_Regs.RealMakeSegSS(CPU_Regs.reg_esp.word()));
            Dos.dos.psp(pspseg.value);
            Dos_PSP newpsp = new Dos_PSP(Dos.dos.psp());
            Dos.dos.dta((int) Memory.RealMake(newpsp.GetSegment(), 0x80));
            newpsp.SaveVectors();
            newpsp.SetFCB1(block.exec.fcb1);
            newpsp.SetFCB2(block.exec.fcb2);
            CPU_Regs.SegSet16SS(Memory.RealSeg(sssp));
            CPU_Regs.reg_esp.word(Memory.RealOff(sssp));
            CPU.CPU_Push16(Memory.RealSeg(csip));
            CPU.CPU_Push16(Memory.RealOff(csip));
            CPU_Regs.flags = (CPU_Regs.flags & (~CPU_Regs.FMASK_TEST)) | CPU_Regs.IF;
            CPU_Regs.reg_ip(CPU_Regs.reg_ip() + 1);
            CPU_Regs.reg_eax.word(0);
            CPU_Regs.reg_ebx.word(0);
            CPU_Regs.reg_ecx.word(0xff);
            CPU_Regs.reg_edx.word(pspseg.value);
            CPU_Regs.reg_esi.word(Memory.RealOff(csip));
            CPU_Regs.reg_edi.word(Memory.RealOff(sssp));
            CPU_Regs.reg_ebp.word(0x91c);
            CPU_Regs.SegSet16DS(pspseg.value);
            CPU_Regs.SegSet16ES(pspseg.value);
            if (Config.C_DEBUG) {
            }
            String stripname = "";
            while (name.length() > 0) {
                char chr = name.charAt(0);
                name = name.substring(1);
                switch(chr) {
                    case ':':
                    case '\\':
                    case '/':
                        stripname = "";
                        break;
                    default:
                        stripname += String.valueOf(chr).toUpperCase();
                }
            }
            int p = stripname.indexOf('.');
            if (p >= 0) {
                stripname = stripname.substring(0, p);
            }
            Dos_MCB pspmcb = new Dos_MCB(Dos.dos.psp() - 1);
            pspmcb.SetFileName(stripname);
            DOS_UpdatePSPName();
            return true;
        }
        return false;
    }
