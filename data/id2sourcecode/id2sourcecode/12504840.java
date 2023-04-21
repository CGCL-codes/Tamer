    public static void main(String[] args) {
        try {
            int optind = 0;
            Pivot pivot = new Pivot();
            while (optind < args.length) {
                if (args[optind].equals("-h")) {
                    System.out.println("Pivot [options] (<File>|<file.gz>|<url>|stdin)");
                    System.out.println("Author: Pierre Lindenbaum PhD. 2007");
                    System.out.println("Compiled on " + Compilation.getDate() + " by " + Compilation.getUser());
                    System.out.println("$LastChangedRevision$");
                    System.out.println(" -h help (this screen)");
                    System.out.println(" -L \'column1,column2,column3,...\' columns for left. (required)");
                    System.out.println(" -T \'column1,column2,column3,...\' columns for top. (optional)");
                    System.out.println(" -D \'column1,column3,column3,...\' columns for data.(required)");
                    System.out.println(" -p <regex> pattern used to break the input into tokens default:TAB");
                    System.out.println(" -i case insensitive");
                    System.out.println(" -t trim each column");
                    System.out.println(" -null <string> value for null");
                    System.out.println(" -e <string> value for empty string");
                    System.out.println(" -f first line is NOT the header");
                    System.out.println(" -html html output");
                    System.out.println(" -no-vt disable vertical summary");
                    System.out.println(" -no-ht disable horizontal summary");
                    System.out.println(" -hw (Hardy Weinberg display option)");
                    for (Choice c : Choice.values()) {
                        System.out.println(" -" + getLabel(c) + " (display option)");
                    }
                    return;
                } else if (args[optind].equals("-i")) {
                    pivot.casesensible = false;
                } else if (args[optind].equals("-no-vt")) {
                    pivot.print_vertical_total = false;
                } else if (args[optind].equals("-no-ht")) {
                    pivot.print_horizontal_total = false;
                } else if (args[optind].equals("-f")) {
                    pivot.firstLineIsHeader = false;
                } else if (args[optind].equals("-null")) {
                    pivot.NULL_VALUE = args[++optind];
                } else if (args[optind].equals("-e")) {
                    pivot.EMPTY_VALUE = args[++optind];
                } else if (args[optind].equals("-t")) {
                    pivot.trimTokens = true;
                } else if (args[optind].equals("-html")) {
                    pivot.printer = pivot.newHTMLPrinter(System.out);
                } else if (args[optind].equals("-p")) {
                    pivot.delimiterIn = Pattern.compile(args[++optind]);
                } else if (args[optind].equals("-L")) {
                    assignColumnModel(args[++optind], pivot.leftColumnIndex, "L");
                } else if (args[optind].equals("-T")) {
                    assignColumnModel(args[++optind], pivot.topColumnIndex, "T");
                } else if (args[optind].equals("-D")) {
                    assignColumnModel(args[++optind], pivot.dataColumnIndex, "D");
                } else if (args[optind].equals("-hw")) {
                    pivot.choices.put(Choice.HW_FREQ_A1, true);
                    pivot.choices.put(Choice.HW_FREQ_A2, true);
                    pivot.choices.put(Choice.HW_CHI2, true);
                } else if (args[optind].equals("--")) {
                    ++optind;
                    break;
                } else if (args[optind].startsWith("-")) {
                    boolean found = false;
                    for (Choice c : Choice.values()) {
                        if (args[optind].substring(1).equals(getLabel(c))) {
                            pivot.choices.put(c, true);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        throw new IllegalArgumentException("Unknown option " + args[optind]);
                    }
                } else {
                    break;
                }
                ++optind;
            }
            if (pivot.leftColumnIndex.isEmpty()) {
                throw new IllegalArgumentException("-L undefined");
            }
            if (pivot.dataColumnIndex.isEmpty()) {
                throw new IllegalArgumentException("-D undefined");
            }
            boolean foundChoice = false;
            for (Choice c : Choice.values()) {
                if (pivot.choices.get(c)) {
                    foundChoice = true;
                    break;
                }
            }
            if (!foundChoice) pivot.choices.put(Choice.DEFAULT, true);
            if (optind == args.length) {
                pivot.read(new BufferedReader(new InputStreamReader(System.in)));
            } else if (optind + 1 == args.length) {
                BufferedReader in = null;
                if (args[optind].startsWith("http://") || args[optind].startsWith("https://") || args[optind].startsWith("file://") || args[optind].startsWith("ftp://")) {
                    URL url = new URL(args[optind]);
                    if (args[optind].endsWith(".gz")) {
                        in = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
                    } else {
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                    }
                } else {
                    File fin = new File(args[optind]);
                    if (fin.getName().endsWith(".gz")) {
                        in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fin))));
                    } else {
                        in = new BufferedReader(new FileReader(fin));
                    }
                }
                pivot.read(in);
                in.close();
            } else {
                throw new IllegalArgumentException("Too many arguments");
            }
            pivot.printer.print();
        } catch (Throwable e) {
            StackTraceElement t[] = e.getStackTrace();
            System.err.println("Error Pivot:" + e.getClass().getName() + ":[" + t[t.length - 1].getLineNumber() + "]: " + e.getMessage());
            System.exit(-1);
        }
    }
