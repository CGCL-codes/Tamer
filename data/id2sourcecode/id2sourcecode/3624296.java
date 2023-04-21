    protected OptionParser getOptionParser() {
        OptionParser parser = new OptionParser();
        parser.acceptsAll(Arrays.asList("s3-url", "u"), "Optional: a URL of the form s3://<bucket>/<path>/<file>").withRequiredArg().describedAs("S3 path");
        parser.acceptsAll(Arrays.asList("list-buckets", "l"), "Optional: list all the buckets you own.");
        parser.accepts("reset-owner-permissions", "Optional: this will give the bucket owner full read/write permissions, useful if many different people have been writing to the same bucket.");
        parser.acceptsAll(Arrays.asList("tab-output-file", "t"), "Optional: tab-formated output file.").withRequiredArg().describedAs("file path");
        parser.acceptsAll(Arrays.asList("search-local-dir", "s"), "Optional: attempt to match files in S3 with files in this local directory.").withRequiredArg().describedAs("directory path");
        return (parser);
    }
