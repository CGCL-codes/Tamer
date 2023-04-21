    public static void merge(String[] args) throws IOException {
        Date begin = new Date();
        FileInputStream left = null;
        try {
            left = new FileInputStream(args[1]);
        } catch (FileNotFoundException e) {
            System.err.println("left file args[1]=\"" + args[1] + "\" caused FileNotFoundException");
            e.printStackTrace();
            throw e;
        }
        FileInputStream right = null;
        try {
            right = new FileInputStream(args[2]);
        } catch (FileNotFoundException e) {
            System.err.println("right file args[2]=\"" + args[2] + "\" caused FileNotFoundException");
            e.printStackTrace();
            throw e;
        }
        FileOutputStream out = new FileOutputStream(args[3]);
        CondorDriver driver = new CondorDriver(left.getChannel(), right.getChannel(), out.getChannel());
        try {
            driver.mergeHelper();
        } finally {
            if (left != null) left.close();
            if (right != null) right.close();
            if (out != null) {
                out.flush();
                out.close();
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
        System.out.println("merge took: " + (end.getTime() - begin.getTime()) + " milliseconds");
    }
