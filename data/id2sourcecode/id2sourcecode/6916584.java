    protected void runFile(String fileName) throws Throwable {
        File file = new File(fileName);
        reset();
        sceDisplay.ignoreLWJGLError = true;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            try {
                FileChannel roChannel = raf.getChannel();
                try {
                    ByteBuffer readbuffer = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) roChannel.size());
                    {
                        emulator.load(file.getPath(), readbuffer);
                    }
                } finally {
                    roChannel.close();
                }
            } finally {
                raf.close();
            }
        } catch (FileNotFoundException fileNotFoundException) {
        }
        RuntimeContext.setIsHomebrew(true);
        UmdIsoReader umdIsoReader = new UmdIsoReader("pspautotests/input/cube.cso");
        Modules.IoFileMgrForUserModule.setIsoReader(umdIsoReader);
        jpcsp.HLE.Modules.sceUmdUserModule.setIsoReader(umdIsoReader);
        Modules.IoFileMgrForUserModule.setfilepath(file.getPath());
        System.out.print(String.format("Running: %s...", fileName));
        {
            RuntimeContext.setIsHomebrew(false);
            HLEModuleManager.getInstance().startModules();
            {
                emulator.RunEmu();
                long startTime = System.currentTimeMillis();
                while (!Emulator.pause) {
                    if (System.currentTimeMillis() - startTime > 5 * 1000) {
                        throw (new TimeoutException());
                    }
                    Thread.sleep(1);
                }
            }
            HLEModuleManager.getInstance().stopModules();
        }
    }
