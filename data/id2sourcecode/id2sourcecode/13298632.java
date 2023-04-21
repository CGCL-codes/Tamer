    private static void printHelp(Logger logger) {
        try {
            byte[] b = new byte[4096];
            int i;
            InputStream f = new FileInputStream("Help.txt");
            while ((i = f.read(b)) > 0) System.out.write(b, 0, i);
            f.close();
        } catch (IOException e) {
            logger.logError(WARN, "Couldnt read helptext" + e);
        }
    }
