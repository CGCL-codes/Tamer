    public static void main(String[] args) {
        Properties prop = System.getProperties();
        System.out.println("Testing NativeRawAccess");
        System.out.println("=======================\n");
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("xxl applications.release.io.NativeRawAccessTest [flushBufferMode [readWriteMode file]]");
            System.out.println();
            System.out.println("This example has three modes:");
            System.out.println("- without parameters: Output help only");
            System.out.println("- one parameter: Copy test with RAFRawAccesses");
            System.out.println("- three parameters: A read/write test on an existing raw access");
            System.out.println();
            System.out.println("flushBufferMode: 0 (no flushing), 1 (flushing buffers after each write)");
            System.out.println("readWriteMode: read/write/both");
            System.out.println("file: name of the file/disk/partition.");
            System.out.println("      Be careful: this can destroy the content of whole partitions!");
            System.out.println();
            System.out.println("Example under Windows:");
            System.out.println("      xxl xxl.core.applications.io.NativeRawAccessTest 1 read \\\\.\\h:");
            System.out.println();
            System.out.println("Path for searching the raw library: " + prop.getProperty("java.library.path"));
            System.out.println();
            return;
        }
        int flushBufferMode = Integer.parseInt(args[0]);
        if (args.length == 1) {
            String file1 = Common.getOutPath() + "test1";
            String file2 = Common.getOutPath() + "test2";
            System.out.println("File 1: " + file1);
            System.out.println("File 2: " + file2);
            long numberOfSectors = 10000;
            System.out.println("Creating Files if necessarry");
            RawAccessUtils.createFileForRaw(file1, numberOfSectors, 512);
            RawAccessUtils.createFileForRaw(file2, numberOfSectors, 512);
            System.out.println("Opening RawAccesses");
            RawAccess ra1 = new NativeRawAccess(file1, 512, flushBufferMode);
            RawAccess ra2 = new NativeRawAccess(file2, 512, flushBufferMode);
            System.out.println("Filling RawAccess 1");
            RawAccessUtils.fillRawAccess(ra1, -1, 'H');
            System.out.println("Copying RawAccess 1 to 2");
            RawAccessUtils.copyRawAccess(ra1, ra2);
            ra1.close();
            ra2.close();
            System.out.println("Checking RawAccess 2");
            ra2 = new NativeRawAccess(file2);
            if (!RawAccessUtils.checkRawAccess(ra2, -1, (byte) 'H')) throw new RuntimeException("Data not read correct");
            ra2.close();
            System.out.println("Test succeeded");
        } else if (args.length == 3) {
            int mode = 0;
            if (args[1].equalsIgnoreCase("read")) mode = 1; else if (args[1].equalsIgnoreCase("write")) mode = 2; else if (args[1].equalsIgnoreCase("both")) mode = 3;
            System.out.print("Random operations on a RawAccess. Kind of operations: ");
            if ((mode & 1) == 1) System.out.print("read ");
            if ((mode & 2) == 2) System.out.print("write ");
            System.out.println();
            RawAccess ra1 = new NativeRawAccess(args[2], 512, flushBufferMode);
            long sector;
            long numSectors = ra1.getNumSectors();
            System.out.println("Number of sectors: " + numSectors);
            Random random = new Random();
            byte b[] = new byte[512];
            Timer t = (Timer) TimerUtils.FACTORY_METHOD.invoke();
            TimerUtils.warmup(t);
            long zerotime = TimerUtils.getZeroTime(t);
            long ti;
            t.start();
            for (int i = 0; i < 1000; i++) {
                sector = (long) (random.nextDouble() * (numSectors));
                boolean write = false;
                if (mode == 3) write = random.nextBoolean(); else if (mode == 2) write = true;
                if (write) ra1.write(b, sector); else ra1.read(b, sector);
            }
            ra1.close();
            ti = t.getDuration();
            double duration = ((double) ti - zerotime) / t.getTicksPerSecond();
            System.out.println("Time for 1000 random read/write operations: " + (long) (duration * 1000) + " ms");
            System.out.println("Test succeeded");
        } else {
            System.out.println("Invalid parameters");
        }
    }
