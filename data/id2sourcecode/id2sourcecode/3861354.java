    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("multiThreadedFindIndelsInAce -a <ace file> -nav <out nav file> -num_threads <num>", "Parse an ace file and find indel errors in the contigs and write the locations to a nav file.", options, "Created by Danny Katzel");
    }
