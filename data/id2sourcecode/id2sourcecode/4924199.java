    public static void main(String[] args) {
        final String br = System.getProperty("line.separator");
        KylmConfigUtils config = new KylmConfigUtils("ConvertLM" + br + "A program to convert models from one format to another" + br + "Example: java -cp kylm.jar kylm.main.ConvertNgram -arpain model.arpa -wfstout model.wfst");
        config.addGroup("Input format options");
        config.addEntry("arpain", KylmConfigUtils.BOOLEAN_TYPE, false, false, "input model is in arpa format");
        config.addEntry("binin", KylmConfigUtils.BOOLEAN_TYPE, false, false, "input model is in binary format");
        config.addGroup("Output format options");
        config.addEntry("arpaout", KylmConfigUtils.BOOLEAN_TYPE, false, false, "output model is in arpa format");
        config.addEntry("binout", KylmConfigUtils.BOOLEAN_TYPE, false, false, "output model is in binary format");
        config.addEntry("wfstout", KylmConfigUtils.BOOLEAN_TYPE, false, false, "output model is in wfst format");
        args = config.parseArguments(args);
        if (args.length != 2) config.exitOnUsage();
        NgramReader ngr = null;
        if (config.getBoolean("arpain")) ngr = new ArpaNgramReader(); else if (config.getBoolean("binin")) ngr = new SerializedNgramReader(); else {
            System.err.println("Must select an input format (-arpain/-binin)");
            System.exit(1);
        }
        NgramWriter ngw = null;
        if (config.getBoolean("arpaout")) ngw = new ArpaNgramWriter(); else if (config.getBoolean("binout")) ngw = new SerializedNgramWriter(); else if (config.getBoolean("wfstout")) ngw = new WFSTNgramWriter(); else {
            System.err.println("Must select an output format (-arpaout/-binout/-wfstout)");
            System.exit(1);
        }
        try {
            ngw.write(ngr.read(args[0]), args[1]);
        } catch (IOException e) {
            System.err.println("Error while printing: " + e.getMessage());
            System.exit(1);
        }
    }
