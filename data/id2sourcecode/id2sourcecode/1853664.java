    private void doConversion() {
        String sOutputFormat;
        if (MIMETypes.LATEX.equals(sTargetMIME)) {
            sOutputFormat = "LaTeX";
        } else if (MIMETypes.BIBTEX.equals(sTargetMIME)) {
            sOutputFormat = "BibTeX";
        } else {
            sOutputFormat = "xhtml";
        }
        System.out.println();
        System.out.println("This is Writer2" + sOutputFormat + ", Version " + ConverterFactory.getVersion() + " (" + ConverterFactory.getDate() + ")");
        System.out.println();
        System.out.println("Starting conversion...");
        File source = new File(sSource);
        if (!source.exists()) {
            System.out.println("I'm sorry, I can't find " + sSource);
            System.exit(1);
        }
        if (!source.canRead()) {
            System.out.println("I'm sorry, I can't read " + sSource);
            System.exit(1);
        }
        boolean bBatch = source.isDirectory();
        File target;
        if (bBatch) {
            if (sTarget == null) {
                target = source;
            } else {
                target = new File(sTarget);
            }
        } else {
            if (sTarget == null) {
                target = new File(source.getParent(), Misc.removeExtension(source.getName()));
            } else {
                target = new File(sTarget);
                if (sTarget.endsWith(File.separator)) {
                    target = new File(target, Misc.removeExtension(source.getName()));
                }
            }
        }
        Converter converter = ConverterFactory.createConverter(sTargetMIME);
        if (converter == null) {
            System.out.println("Failed to create converter for " + sTargetMIME);
            System.exit(1);
        }
        BatchConverter batchCv = null;
        if (bBatch) {
            batchCv = ConverterFactory.createBatchConverter(MIMETypes.XHTML);
            if (batchCv == null) {
                System.out.println("Failed to create batch converter");
                System.exit(1);
            }
            batchCv.setConverter(converter);
        }
        if (sTemplateFileName != null) {
            try {
                System.out.println("Reading template " + sTemplateFileName);
                byte[] templateBytes = Misc.inputStreamToByteArray(new FileInputStream(sTemplateFileName));
                converter.readTemplate(new ByteArrayInputStream(templateBytes));
                if (batchCv != null) {
                    batchCv.readTemplate(new ByteArrayInputStream(templateBytes));
                }
            } catch (FileNotFoundException e) {
                System.out.println("--> This file does not exist!");
                System.out.println("    " + e.getMessage());
            } catch (IOException e) {
                System.out.println("--> Failed to read the template file!");
                System.out.println("    " + e.getMessage());
            }
        }
        if (sStyleSheetFileName != null) {
            try {
                System.out.println("Reading style sheet " + sStyleSheetFileName);
                byte[] styleSheetBytes = Misc.inputStreamToByteArray(new FileInputStream(sStyleSheetFileName));
                converter.readStyleSheet(new ByteArrayInputStream(styleSheetBytes));
            } catch (FileNotFoundException e) {
                System.out.println("--> This file does not exist!");
                System.out.println("    " + e.getMessage());
            } catch (IOException e) {
                System.out.println("--> Failed to read the style sheet file!");
                System.out.println("    " + e.getMessage());
            }
        }
        for (String sResource : resources) {
            String sMediaType;
            String sFileName;
            int nSeparator = sResource.indexOf("::");
            if (nSeparator > -1) {
                sFileName = sResource.substring(0, nSeparator);
                sMediaType = sResource.substring(nSeparator + 2);
            } else {
                sFileName = sResource;
                sMediaType = null;
            }
            System.out.println("Reading resource file " + sFileName);
            try {
                byte[] resourceBytes = Misc.inputStreamToByteArray(new FileInputStream(sFileName));
                converter.readResource(new ByteArrayInputStream(resourceBytes), sFileName, sMediaType);
            } catch (IOException e) {
                System.out.println("--> Failed to read the resource file!");
                System.out.println("    " + e.getMessage());
            }
        }
        for (int i = 0; i < configFileNames.size(); i++) {
            String sConfigFileName = (String) configFileNames.get(i);
            if (sConfigFileName.startsWith("*")) {
                sConfigFileName = sConfigFileName.substring(1);
                System.out.println("Reading default configuration " + sConfigFileName);
                try {
                    converter.getConfig().readDefaultConfig(sConfigFileName);
                } catch (IllegalArgumentException e) {
                    System.err.println("--> This configuration is unknown!");
                    System.out.println("    " + e.getMessage());
                }
            } else {
                System.out.println("Reading configuration file " + sConfigFileName);
                try {
                    byte[] configBytes = Misc.inputStreamToByteArray(new FileInputStream(sConfigFileName));
                    converter.getConfig().read(new ByteArrayInputStream(configBytes));
                    if (bBatch) {
                        batchCv.getConfig().read(new ByteArrayInputStream(configBytes));
                    }
                } catch (IOException e) {
                    System.err.println("--> Failed to read the configuration!");
                    System.out.println("    " + e.getMessage());
                }
            }
        }
        Enumeration<String> keys = options.keys();
        while (keys.hasMoreElements()) {
            String sKey = keys.nextElement();
            String sValue = (String) options.get(sKey);
            converter.getConfig().setOption(sKey, sValue);
            if (batchCv != null) {
                batchCv.getConfig().setOption(sKey, sValue);
            }
        }
        if (bBatch) {
            batchCv.convert(source, target, bRecurse, new BatchHandlerImpl());
        } else {
            System.out.println("Converting " + source.getPath());
            ConverterResult dataOut = null;
            try {
                dataOut = converter.convert(source, target.getName());
            } catch (FileNotFoundException e) {
                System.out.println("--> The file " + source.getPath() + " does not exist!");
                System.out.println("    " + e.getMessage());
                System.exit(1);
            } catch (IOException e) {
                System.out.println("--> Failed to convert the file " + source.getPath() + "!");
                System.out.println("    " + e.getMessage());
                System.exit(1);
            }
            File targetDir = target.getParentFile();
            if (targetDir != null && !targetDir.exists()) {
                targetDir.mkdirs();
            }
            try {
                dataOut.write(targetDir);
            } catch (IOException e) {
                System.out.println("--> Error writing out file!");
                System.out.println("    " + e.getMessage());
                System.exit(1);
            }
        }
        System.out.println("Done!");
    }
