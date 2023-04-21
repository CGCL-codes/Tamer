    public void doConversion() {
        String sOutputFormat = "xhtml";
        System.out.println();
        System.out.println("This is Writer2" + sOutputFormat + ", Version " + ConverterFactory.getVersion() + " (" + ConverterFactory.getDate() + ")");
        System.out.println();
        System.out.println("Starting conversion...");
        config = new Config();
        if (sConfigFileName != null) {
            File f = new File(sConfigFileName);
            if (!f.exists()) {
                System.err.println("The configuration file '" + sConfigFileName + "' does not exist.");
            } else {
                try {
                    config.read(new FileInputStream(sConfigFileName));
                } catch (Throwable t) {
                    System.err.println("I had trouble reading the configuration file " + sConfigFileName);
                    t.printStackTrace();
                }
            }
        }
        String sFullOutFileName = sTarget != null ? sTarget : Misc.removeExtension(sSource);
        if (sFullOutFileName.endsWith(File.separator)) {
            sOutPathName = sFullOutFileName;
            sOutFileName = (new File(sSource)).getName();
        } else {
            File f = new File(sFullOutFileName);
            sOutPathName = f.getParent();
            if (sOutPathName == null) {
                sOutPathName = "";
            } else {
                sOutPathName += File.separator;
            }
            sOutFileName = f.getName();
        }
        sOutFileName = Misc.removeExtension(sOutFileName);
        Enumeration keys = options.keys();
        while (keys.hasMoreElements()) {
            String sKey = (String) keys.nextElement();
            String sValue = (String) options.get(sKey);
            config.setOption(sKey, sValue);
        }
        l10n = new L10n();
        sDefaultLang = System.getProperty("user.language");
        sDefaultCountry = System.getProperty("user.country");
        l10n.setLocale(sDefaultLang, sDefaultCountry);
        File f = new File(sSource);
        if (!f.exists()) {
            System.out.println("I'm sorry, I can't find " + sSource);
            System.exit(1);
        }
        if (!f.canRead()) {
            System.out.println("I'm sorry, I can't read " + sSource);
            System.exit(1);
        }
        if (sTemplateFileName != null) {
            try {
                templateBytes = Misc.inputStreamToByteArray(new FileInputStream(sTemplateFileName));
                template = new XhtmlDocument("Template", XhtmlDocument.XHTML_MATHML);
                template.read(new ByteArrayInputStream(templateBytes));
            } catch (Throwable t) {
                t.printStackTrace();
                template = null;
            }
        }
        if (f.isDirectory()) {
            convertDirectory(f, sOutPathName);
        } else if (f.isFile()) {
            convertFile(f, sOutPathName);
        }
        System.out.println("Done!");
    }
