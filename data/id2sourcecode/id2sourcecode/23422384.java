    public PCLogHandler() {
        PrintStream fStream = null;
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("vermilion" + System.currentTimeMillis() + ".log.zip"));
            zos.putNextEntry(new ZipEntry("log.log"));
            fStream = new PrintStream(zos);
            killOldLogs();
        } catch (IOException ex) {
            fStream = null;
        }
        fileStream = fStream;
        consoleStream = System.err;
    }
