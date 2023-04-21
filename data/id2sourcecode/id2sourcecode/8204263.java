    public static void main(String[] args) {
        System.out.println("***The FaultInjector Front-End***");
        List<String> javaFiles = new ArrayList<String>();
        int startNumber = 1;
        boolean silent = false;
        boolean doSql = false;
        javaFiles = getListOfJavaFiles("testFiles");
        String separator = "\t";
        for (String s : Arrays.asList(args)) {
            if (s.equals("silent")) {
                System.out.println("Starting FaultInjectorFrontEnd  in silent mode.");
                silent = true;
            }
            if (s.startsWith("src=")) {
                List<String> fileSet = getListOfJavaFiles(s.substring(4));
                if (fileSet != null && fileSet.size() != 0) {
                    javaFiles = fileSet;
                } else {
                    System.out.println("The file or directory " + s.substring(4) + " does not exist. \n" + "Continuing with the default test folder.\n");
                }
            }
            if (s.equals("help")) {
                System.out.println("Usage: java FaultInjectorFrontEnd [src=\"dirToManipulate\" | silent | sql | help] \n" + "src=?: The directory to manipulate (read and write).\n" + "startnumber=? | n=?: The fault number to start with.\n" + "silent: If set, the program does not ask for confirmation for files to alter (suitable for external scripts).\n" + "sql: If set, the log will be prepared as a sql script to store fault data in a sql database. \n" + "separator=? | sep=?: Specify a different instead tabulator for log entries. \n" + "help: This output.\n");
                if (args.length == 1) {
                    System.exit(0);
                }
            }
            if (s.equals("sql")) {
                doSql = true;
            }
            if (s.startsWith("startnumber=") || s.startsWith("n=")) {
                String[] val = s.split("=");
                try {
                    startNumber = Integer.parseInt(val[1]);
                } catch (NumberFormatException e) {
                    System.err.println("You must specify a valid number to set the startnumber.\n" + "Injection aborted. None of your files have been touched.");
                    System.exit(0);
                }
            }
            if (s.startsWith("sep=") || s.startsWith("separator=") || s.startsWith("s=")) {
                String[] val = s.split("=");
                separator = val[1];
            }
        }
        System.out.println("Applying direct fault injection on the following java files:");
        for (String f : javaFiles) {
            System.out.println(f);
        }
        if (!silent) {
            System.out.println("Are you sure to poison all Java files of this directory? (y/n)\n" + "IMPORTANT: It is your responsibility to BACKUP files!");
            byte[] buffer = new byte[80];
            int read;
            boolean repeat = true;
            try {
                do {
                    read = System.in.read(buffer);
                    String input = new String(buffer, 0, read);
                    if (input.equals("y\n")) {
                        repeat = false;
                    } else if (input.equals("n\n")) {
                        System.out.println("Ok, none of your files will be touched. Good bye.");
                        System.exit(0);
                    } else {
                        buffer = new byte[2];
                        System.out.println("Please enter 'y' if you want to proceed or 'n' if you want to quit.");
                    }
                } while (repeat);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n");
        FaultInjector.faultNumber = startNumber - 1;
        InjectionLogger logger = InjectionLogger.getInstance();
        logger.setDoSql(doSql);
        logger.setSeparator(separator);
        for (String file : javaFiles) {
            String[] params = new String[] { file, "" };
            FaultInjector.main(params);
        }
        File file = new File("injectionlog.txt");
        logger.writeToFile(file);
        System.out.println("\nThank you.");
    }
