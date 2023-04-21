    public int run(List<XValue> args) throws Exception {
        Options opts = new Options("p=print", SerializeOpts.getOptionDefs());
        opts.parse(args);
        bIndent = opts.hasOpt("p");
        args = opts.getRemainingArgs();
        OutputPort stdout = getStdout();
        InputPort inp = args.isEmpty() ? getStdin() : getInput(args.get(0));
        SerializeOpts serializeOpts = getSerializeOpts(opts);
        XMLEventReader reader = inp.asXMLEventReader(serializeOpts);
        mSerializeOpts = serializeOpts.clone();
        serializeOpts.setOutputTextEncoding(kENCODING_UTF_8);
        PrintWriter writer = stdout.asPrintWriter(serializeOpts);
        parse(reader, writer, false);
        writer.flush();
        writer.close();
        while (reader.hasNext()) reader.nextEvent();
        reader.close();
        inp.release();
        return 0;
    }
