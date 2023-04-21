    public static void sort(String[] args) throws IOException {
        Date begin = new Date();
        String input = args[1];
        String out = args[2];
        FileInputStream unsorted = null;
        try {
            unsorted = new FileInputStream(input);
        } catch (FileNotFoundException e) {
            System.err.println("sort input file args[1]=\"" + input + "\" caused FileNotFoundException");
            e.printStackTrace();
            throw e;
        }
        ReadableByteChannel leftAsFile = unsorted.getChannel();
        FileOutputStream outStream = new FileOutputStream(out);
        WritableByteChannel outChannel = outStream.getChannel();
        CondorDriver driver = new CondorDriver(leftAsFile, null, outChannel);
        try {
            driver.sort();
        } finally {
            if (leftAsFile != null) leftAsFile.close();
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
        }
        if (driver.hasCaughtExceptions()) {
            System.err.println("main thread caught the following child exceptions: ");
            System.err.println("<exceptions>");
            StringWriter logOfExceptions = new StringWriter();
            PrintWriter writer = new PrintWriter(logOfExceptions);
            driver.printCaughtExceptions(writer);
            System.err.print(logOfExceptions.toString());
            System.err.println("</exceptions>");
        } else {
            System.out.println("no exceptions caught by main thread.");
        }
        Date end = new Date();
        System.out.println("sort took: " + (end.getTime() - begin.getTime()) + " milliseconds");
    }
