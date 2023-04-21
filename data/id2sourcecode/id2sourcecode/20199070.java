    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("createTupleFile -D <db> -o <output tuple file> -b <bac id file>", "read the bac id file file, and write out the equivalent tuple file", options, "Created by Danny Katzel");
    }
