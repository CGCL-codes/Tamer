    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("filterFastqDataFromCLC -cas <cas file> -d <max coverage> -o <output include file> [OPTIONS]", "Parse an CLC cas file and write out include list of solexa/ illumina read ids " + "that are required inorder achieve the specified max coverage depth.", options, "Created by Danny Katzel");
    }
