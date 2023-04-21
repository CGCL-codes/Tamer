    @BeforeClass
    public static void globalSetup() throws Exception {
        rootTestDir = getScratchDir(GRID_SCRATCH_DIR);
        command = new File(rootTestDir, "test.sh");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(command);
            writer.println("#!/bin/bash");
            writer.println("read SLEEP_TIME");
            writer.println("/bin/sleep $SLEEP_TIME");
            writer.println("/bin/echo $SLEEP_TIME");
            writer.flush();
        } finally {
            writer.close();
        }
        command.setReadable(true);
        command.setWritable(true);
        command.setExecutable(true);
    }
