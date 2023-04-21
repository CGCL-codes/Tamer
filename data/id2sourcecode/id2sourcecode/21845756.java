    private static void usage() {
        System.out.println("Usage: \n " + CmdUtil.getJavaCommand(DbRunAction.class));
        System.out.println("  -h <environment home> ");
        System.out.println("  -a <batchClean|compress|evict|checkpoint|" + "removeDb|removeDbAndClean|activateCleaner>");
        System.out.println("  -ro (read-only - defaults to read-write)");
        System.out.println("  -s <dbName> (for removeDb)");
    }
