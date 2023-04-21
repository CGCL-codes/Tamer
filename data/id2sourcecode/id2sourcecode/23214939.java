    public static int CreateProcessA(int lpApplicationName, int lpCommandLine, int lpProcessAttributes, int lpThreadAttributes, int bInheritHandles, int dwCreationFlags, int lpEnvironment, int lpCurrentDirectory, int lpStartupInfo, int lpProcessInformation) {
        String name = null;
        String cwd = null;
        String commandLine = "";
        WinProcess currentProcess = WinSystem.getCurrentProcess();
        if ((lpApplicationName == 0 && lpCommandLine == 0) || lpStartupInfo == 0 || lpProcessInformation == 0) {
            CPU_Regs.reg_eax.dword = WinAPI.FALSE;
            Scheduler.getCurrentThread().setLastError(Error.ERROR_INVALID_PARAMETER);
        }
        if (lpCommandLine != 0) {
            commandLine = new LittleEndianFile(lpCommandLine).readCString();
        }
        if (lpApplicationName != 0) {
            name = new LittleEndianFile(lpApplicationName).readCString();
        } else {
            name = StringUtil.parseQuotedString(commandLine)[0];
        }
        if (lpCurrentDirectory != 0) {
            cwd = new LittleEndianFile(lpCurrentDirectory).readCString();
        } else {
            cwd = currentProcess.currentWorkingDirectory;
        }
        StartupInfo info = new StartupInfo(lpStartupInfo);
        int pos = name.lastIndexOf("\\");
        if (pos >= 0) {
            if (!name.substring(0, pos + 1).equalsIgnoreCase(cwd)) {
                Console.out("***WARNING*** Creating process using full path where path is not current working directory.  This may not work");
            }
            name = name.substring(pos + 1);
        }
        WinProcess process = WinProcess.create(name, commandLine, currentProcess.paths, currentProcess.currentWorkingDirectory);
        if (process == null) {
            SetLastError(Error.ERROR_FILE_NOT_FOUND);
            return FALSE;
        } else {
            CPU_Regs.reg_eax.dword = WinAPI.TRUE;
            process.open();
            process.getMainThread().open();
            Memory.mem_writed(lpProcessInformation, process.getHandle());
            Memory.mem_writed(lpProcessInformation + 4, process.getMainThread().getHandle());
            Memory.mem_writed(lpProcessInformation + 8, process.getHandle());
            Memory.mem_writed(lpProcessInformation + 12, process.getMainThread().getHandle());
            return TRUE;
        }
    }
