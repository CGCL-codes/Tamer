    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("multiThreadedReAbacusAce -a <ace file> -nav <input nav file> -o <ace out file> -num_threads <num>", "Parse an ace file and input nav file with abacus problem regions and write out a new ace file " + "with those regions realigned and consensus recalled to try to correct the problem regions.", options, "Created by Danny Katzel");
    }
