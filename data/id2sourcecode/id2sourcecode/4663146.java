    public void process(String[] args) {
        try {
            if (helpOpt) {
                printUsage();
                return;
            }
            String type = typeOpt;
            if (type != null && !type.equalsIgnoreCase("html") && !type.equalsIgnoreCase("xml")) {
                throw new IllegalArgumentException("Unknown type: " + type);
            }
            if (fileArgsOpt.length == 0) {
                if (type == null) {
                    type = "html";
                }
            } else {
                if (type == null) {
                    if (fileArgsOpt[0].toLowerCase().endsWith(".xml")) {
                        type = "xml";
                    } else {
                        type = "html";
                    }
                }
            }
            if (analyzeOpt) {
                HtmlAnalyzer analyzer = new HtmlAnalyzer(HtmlCompressor.JS_COMPRESSOR_CLOSURE.equalsIgnoreCase(jsCompressorOpt) ? HtmlCompressor.JS_COMPRESSOR_CLOSURE : HtmlCompressor.JS_COMPRESSOR_YUI);
                analyzer.analyze(readResource(buildReader(fileArgsOpt.length > 0 ? fileArgsOpt[0] : null)));
            } else {
                Compressor compressor = type.equals("xml") ? createXmlCompressor() : createHtmlCompressor();
                Map<String, String> ioMap = buildInputOutputMap();
                for (Map.Entry<String, String> entry : ioMap.entrySet()) {
                    writeResource(compressor.compress(readResource(buildReader(entry.getKey()))), buildWriter(entry.getValue()));
                }
            }
        } catch (NoClassDefFoundError e) {
            if (HtmlCompressor.JS_COMPRESSOR_CLOSURE.equalsIgnoreCase(jsCompressorOpt)) {
                System.out.println("ERROR: For JavaScript compression using Google Closure Compiler\n" + "additional jar file called compiler.jar must be present\n" + "in the same directory as HtmlCompressor jar");
            } else {
                System.out.println("ERROR: For CSS or JavaScript compression using YUICompressor additional jar file \n" + "called yuicompressor.jar must be present\n" + "in the same directory as HtmlCompressor jar");
            }
        } catch (OptionException e) {
            System.out.println("ERROR: " + e.getMessage());
            printUsage();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
