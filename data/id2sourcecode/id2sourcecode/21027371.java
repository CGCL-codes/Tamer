    protected int _parseArgs(String[] args) throws CmdLineArgException, FileNotFoundException, IOException {
        int i = 0;
        int argumentsRead;
        String arg;
        String title = "Ptolemy plot";
        int width = 500;
        int height = 300;
        while ((args != null) && (i < args.length)) {
            arg = args[i++];
            if (arg.equals("-height")) {
                if (i > (args.length - 1)) {
                    throw new CmdLineArgException(_usage());
                }
                height = Integer.valueOf(args[i++]).intValue();
                continue;
            } else if (arg.equals("-help")) {
                System.out.println(_usage());
                StringUtilities.exit(0);
                continue;
            } else if (arg.equals("-test")) {
                _test = true;
                continue;
            } else if (arg.equals("-version")) {
                System.out.println("Version " + PlotBox.PTPLOT_RELEASE + ", Build $Id: PlotApplication.java,v 1.69.4.1 2008/04/04 20:01:01 cxh Exp $");
                StringUtilities.exit(0);
                continue;
            } else if (arg.equals("-width")) {
                if (i > (args.length - 1)) {
                    throw new CmdLineArgException("-width requires an integer argument");
                }
                width = Integer.valueOf(args[i++]).intValue();
                continue;
            } else if (arg.equals("")) {
            } else if (arg.equals("-")) {
                URL base = new URL("file", null, "standard input");
                _read(base, System.in);
            } else if (!arg.startsWith("-")) {
                InputStream instream;
                URL base;
                try {
                    URL inurl = new URL(null, arg);
                    base = inurl;
                    instream = inurl.openStream();
                } catch (MalformedURLException ex) {
                    File file = new File(arg);
                    instream = new FileInputStream(file);
                    _file = new File(file.getAbsolutePath());
                    title = _file.getName();
                    _directory = _file.getParentFile();
                    base = new URL("file", null, _directory.getAbsolutePath());
                }
                _read(base, instream);
            } else {
                throw new CmdLineArgException("Unrecognized option: " + arg);
            }
        }
        setSize(width, height);
        setTitle(title);
        argumentsRead = i;
        return argumentsRead;
    }
