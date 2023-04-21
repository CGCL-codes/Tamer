    public static void main(String[] args) {
        try {
            Xar handler = new Xar();
            int optind = 0;
            while (optind < args.length) {
                if (args[optind].equals("-h")) {
                    System.err.println("Pierre Lindenbaum PhD 2009.");
                    System.err.println(Compilation.getLabel());
                    System.err.println("This tool expand an XML archive and generate the files (see format below)");
                    System.err.println("options:");
                    System.err.println(" -h this screen");
                    System.err.println(" -X eXclusive create: files should not already exist");
                    System.err.println(" -N No overwrite: a file will not be over-written if it already exists.");
                    System.err.println(" -D output directory : where to exand the file (default is the current-directory)");
                    System.err.println("(stdin|urls|files) sources ending with *.xarz or *.xml.gz will be g-unzipped.");
                    System.err.println("\n\nExample:\n\n" + "<?xml version=\"1.0\"?>\n" + "<archive>\n" + " <file path=\"mydir/file.01.txt\">\n" + "  Hello World !\n" + " </file>\n" + " <file path=\"mydir/file.02.text\" content-type=\"text/plain\">\n" + "  Hello World &lt;!\n" + " </file>\n" + " <file path=\"mydir/file.03.text\" overwrite='false'> <!-- won't write if file exists -->\n" + "  Hello World &lt;!\n" + " </file>\n" + " <file path=\"mydir/file.02.xml\" content-type=\"text/xml\">\n" + "  <a>Hello World !<b xmlns=\"urn:any\" att=\"x\">azdpoazd<i/></b></a>\n" + " </file>\n" + "</archive>\n");
                    return;
                } else if (args[optind].equals("-X")) {
                    handler.exclusiveCreate = true;
                } else if (args[optind].equals("-N")) {
                    handler.allowOverwrite = false;
                } else if (args[optind].equals("-D")) {
                    handler.outputDir = new File(args[++optind]);
                    if (!handler.outputDir.exists()) {
                        System.err.println(handler.outputDir + " directory doesn't exist");
                        return;
                    }
                    if (!handler.outputDir.isDirectory()) {
                        System.err.println(handler.outputDir + " is not a directory");
                        return;
                    }
                } else if (args[optind].equals("--")) {
                    ++optind;
                    break;
                } else if (args[optind].startsWith("-")) {
                    System.err.println("bad argument " + args[optind]);
                    System.exit(-1);
                } else {
                    break;
                }
                ++optind;
            }
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            saxFactory.setNamespaceAware(true);
            saxFactory.setValidating(false);
            SAXParser parser = saxFactory.newSAXParser();
            if (optind == args.length) {
                parser.parse(System.in, handler);
            } else {
                while (optind < args.length) {
                    String file = args[optind++];
                    if (StringUtils.startsWith(file, "http://", "https://", "ftp://")) {
                        parser.parse(file, handler);
                    } else if (StringUtils.endsWith(file, ".xarz", ".xml.gz")) {
                        InputStream in = new GZIPInputStream(new FileInputStream(file));
                        parser.parse(in, handler);
                        in.close();
                    } else {
                        parser.parse(new File(file), handler);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Error] " + e.getMessage());
        }
    }
