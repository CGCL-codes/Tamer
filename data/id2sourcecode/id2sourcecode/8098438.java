    public Metadata() {
        super();
        parser.acceptsAll(Arrays.asList("list-tables", "lt"), "Optional: if provided will list out the tables this tools knows how to read and/or write to.");
        parser.acceptsAll(Arrays.asList("table", "t"), "Required: the table you are interested in reading or writing.").withRequiredArg();
        parser.acceptsAll(Arrays.asList("output-file", "of"), "Optional: if provided along with the --list or --list-tables options this will cause the output list of rows/tables to be written to the file specified rather than stdout.").withRequiredArg();
        parser.acceptsAll(Arrays.asList("list-fields", "lf"), "Optional: if provided along with the --table option this will list out the fields for that table and their type.");
        parser.acceptsAll(Arrays.asList("field", "f"), "Optional: the table you are interested in reading or writing.").withRequiredArg();
        parser.acceptsAll(Arrays.asList("create", "c"), "Optional: indicates you want to create a new row, must supply --table and all the required --field params.");
        ret.setExitStatus(ReturnValue.SUCCESS);
    }
