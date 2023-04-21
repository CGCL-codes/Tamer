    private static boolean MakeEnv(String name, IntRef segment) {
        Dos_PSP psp = new Dos_PSP(Dos.dos.psp());
        int envread, envwrite;
        int envsize = 1;
        boolean parentenv = true;
        if (segment.value == 0) {
            if (psp.GetEnvironment() == 0) parentenv = false;
            envread = Memory.PhysMake(psp.GetEnvironment(), 0);
        } else {
            if (segment.value == 0) parentenv = false;
            envread = Memory.PhysMake(segment.value, 0);
        }
        if (parentenv) {
            for (envsize = 0; ; envsize++) {
                if (envsize >= MAXENV - ENV_KEEPFREE) {
                    Dos.DOS_SetError(Dos.DOSERR_ENVIRONMENT_INVALID);
                    return false;
                }
                if (Memory.mem_readw(envread + envsize) == 0) break;
            }
            envsize += 2;
        }
        IntRef size = new IntRef(long2para(envsize + ENV_KEEPFREE));
        if (!Dos_memory.DOS_AllocateMemory(segment, size)) return false;
        envwrite = Memory.PhysMake(segment.value, 0);
        if (parentenv) {
            Memory.MEM_BlockCopy(envwrite, envread, envsize);
            envwrite += envsize;
        } else {
            Memory.mem_writeb(envwrite++, 0);
        }
        Memory.mem_writew(envwrite, 1);
        envwrite += 2;
        StringRef namebuf = new StringRef();
        if (Dos_files.DOS_Canonicalize(name, namebuf)) {
            Memory.MEM_BlockWrite(envwrite, namebuf.value, namebuf.value.length() + 1);
            return true;
        } else return false;
    }
