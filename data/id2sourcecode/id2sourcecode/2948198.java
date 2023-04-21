    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("splitFastq [OPTIONS] -i <fastq file> -n <# files>", "Parse a fastQ file and re-write the data into several files.  Each file will contain 1/nth of the total number of reads." + "the files will be named <original fastq file>.part_[0-(n-1)].fastq", options, "Created by Danny Katzel");
    }
