    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("sortedFasta2Fastq [OPTIONS] -s <seq file> -q <qual file> -o <fastq file>", "Parse a  sorted seq and qual file (ids in same order) and write the results out a fastq file. " + "This version should be orders of magnitude faster than fasta2Fastq because no indexing is required " + "and the seq and qual files are parsed at the same time in different threads.", options, "Created by Danny Katzel");
    }
