    public static void main1(String[] args) throws IOException {
        args = new String[] { "/Users/nickmain/Desktop/as3temp/abcbuilder.swf" };
        final AVM2ABCBuilder builder = new AVM2ABCBuilder();
        SWFTagTypes tags = new SWFTagTypesImpl(null) {

            @Override
            public ABC tagDoABC(int flags, String name) throws IOException {
                return builder;
            }
        };
        FileInputStream in = new FileInputStream(args[0]);
        SWFTags tagparser = new TagParser(tags);
        SWFReader reader = new SWFReader(tagparser, in);
        reader.readFile();
        FileWriter fw1 = new FileWriter("/Users/nickmain/Desktop/as3temp/tempdump1.txt");
        IndentingPrintWriter ipw1 = new IndentingPrintWriter(fw1);
        builder.file.dump(ipw1);
        ipw1.flush();
        fw1.close();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutStream os = new OutStream(bout);
        ABCWriter writer = new ABCWriter(os);
        builder.file.write(writer);
        InStream is = new InStream(bout.toByteArray());
        AVM2ABCBuilder builder2 = new AVM2ABCBuilder();
        ABCParser parser = new ABCParser(builder, is);
        parser.parse();
        FileWriter fw2 = new FileWriter("/Users/nickmain/Desktop/as3temp/tempdump2.txt");
        IndentingPrintWriter ipw2 = new IndentingPrintWriter(fw2);
        builder2.file.dump(ipw2);
        ipw2.flush();
        fw2.close();
    }
