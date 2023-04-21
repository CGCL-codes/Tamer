    @Override
    public int run(List<XValue> args) throws Exception {
        boolean opt_all = false;
        boolean opt_relative = false;
        Options opts = new Options("a=all,r=relative", SerializeOpts.getOptionDefs());
        opts.parse(args);
        SerializeOpts serializeOpts = getSerializeOpts(opts);
        XMLEventReader reader = getStdin().asXMLEventReader(serializeOpts);
        XMLEventWriter writer = getStdout().asXMLEventWriter(serializeOpts);
        opt_all = opts.hasOpt("all");
        opt_relative = opts.hasOpt("relative");
        add_base(reader, writer, opt_all, opt_relative, getStdin().getSystemId());
        return 0;
    }
