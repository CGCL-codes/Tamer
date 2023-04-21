    public static void main(String[] args) throws IOException {
        SfntDump dumper = new SfntDump();
        File fontFile = null;
        int optionCount = 0;
        if (args.length > 0 && !(args[0].equals("-h") || args[0].equals("-help") || args[0].equals("-?"))) {
            fontFile = new File(args[args.length - 1]);
            args = Arrays.copyOfRange(args, 0, args.length - 1);
        } else {
            printUsage();
            System.exit(0);
        }
        for (int i = 0; i < args.length; i++) {
            String option = null;
            if (args[i].charAt(0) == '-') {
                option = args[i].substring(1);
            }
            if (option != null) {
                optionCount++;
                if (option.equals("count")) {
                    dumper.countSpecialGlyphs(true);
                    continue;
                }
                if (option.equals("t")) {
                    if (i + 1 < args.length) {
                        dumper.dumpTablesAsBinary(args[++i]);
                    }
                    continue;
                }
                if (option.equals("cm")) {
                    if (i + 1 < args.length) {
                        dumper.useCMap(args[++i]);
                    }
                    continue;
                }
                if (option.equals("table")) {
                    dumper.dumpTableList(true);
                    continue;
                }
                if (option.startsWith("name")) {
                    dumper.dumpNames(true);
                    continue;
                }
                if (option.startsWith("cmap")) {
                    dumper.dumpCMaps(true);
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        dumper.dumpCMaps(args[++i]);
                    }
                    continue;
                }
                if (option.startsWith("post")) {
                    dumper.dumpPost(true);
                    continue;
                }
                if (option.startsWith("eblc")) {
                    dumper.dumpEblc(true);
                    continue;
                }
                if (option.equals("glyph") || option.equals("g")) {
                    BitSet glyphSet = null;
                    if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
                        dumper.dumpAllGlyphs(true);
                        continue;
                    }
                    if (i + 1 < args.length) {
                        i++;
                        glyphSet = parseRange(args[i]);
                        if (glyphSet == null) {
                            glyphSet = parseList(args[i]);
                        }
                        if (glyphSet != null) {
                            dumper.dumpGlyphs(glyphSet);
                        }
                    }
                    if (glyphSet == null) {
                        System.out.println("glyph dump option requires a glyph range or list");
                        System.exit(0);
                    }
                }
                if (option.equals("char") || option.equals("c")) {
                    BitSet charSet = null;
                    if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
                        dumper.dumpAllChars(true);
                        continue;
                    }
                    if (i + 1 < args.length) {
                        i++;
                        charSet = parseRange(args[i]);
                        if (charSet == null) {
                            charSet = parseList(args[i]);
                        }
                        if (charSet != null) {
                            dumper.dumpChars(charSet);
                        }
                    }
                    if (charSet == null) {
                        System.out.println("character dump option requires a glyph range or list");
                        System.exit(0);
                    }
                }
                if (option.equals("all") || option.equals("a")) {
                    dumper.dumpAll(true);
                }
            }
        }
        if (optionCount == 0) {
            dumper.dumpTableList(true);
        }
        if (fontFile != null) {
            if (fontFile.isDirectory()) {
                File[] files = fontFile.listFiles();
                for (File file : files) {
                    if (file.isFile() && !file.isHidden()) {
                        try {
                            dumper.dumpFont(file);
                            System.out.println();
                        } catch (Throwable t) {
                            System.out.printf("Error processing file: %s\n", file);
                        }
                    }
                }
            } else {
                try {
                    dumper.dumpFont(fontFile);
                } catch (Throwable t) {
                    System.out.printf("Error processing file: %s\n", fontFile);
                }
            }
        } else {
            printUsage();
            System.exit(0);
        }
    }
