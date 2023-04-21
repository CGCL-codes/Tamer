    public static void main(String args[]) throws IOException, InterruptedException {
        boolean simulate = false;
        boolean hidden = false;
        boolean update = false;
        boolean recursive = false;
        boolean verbose = false;
        String log_path = null;
        final Settings settings = Settings.getInstance();
        if (args.length == 1 && (args[0].equals("-gui") || args[0].equals("--gui"))) {
            java.awt.EventQueue.invokeLater(new GUILauncher(settings));
            return;
        } else if (args.length < 2) {
            System.out.println(help);
            System.exit(0);
        } else if (args.length > 2) {
            for (int i = 0; i < args.length - 2; i++) {
                if (args[i].equals("-s") || args[i].equals("--simulate")) {
                    simulate = true;
                } else if (args[i].equals("-h") || args[i].equals("--hidden")) {
                    hidden = true;
                } else if (args[i].equals("-u") || args[i].equals("--update")) {
                    update = true;
                } else if (args[i].equals("-R") || args[i].equals("--recursive")) {
                    recursive = true;
                } else if (args[i].startsWith("-l") || args[i].startsWith("--log")) {
                    log_path = (args[i].substring(args[i].length() - 1));
                } else if (args[i].equals("-v") || args[i].equals("--verbose")) {
                    verbose = true;
                } else if (args[i].equals("-gui") || args[i].equals("--gui")) {
                    java.awt.EventQueue.invokeLater(new GUILauncher(settings));
                    return;
                } else {
                    System.out.println("backupit: invalid option -- " + args[i] + "\n" + "Try `backupit --help' for more information.\n");
                }
            }
        }
        DirBackup backup = BackupFactory.getDirBackupInstance("default", new ResourceIdentifier(args[args.length - 2]), new ResourceIdentifier(args[args.length - 1]), hidden, update, recursive, simulate);
        DirBackupWriter writer = BackupWriterFactory.getInstance(backup);
        Thread t = new Thread(writer);
        t.start();
        t.join();
        DirBackupInfo backupInfo = writer.getBackupInfo();
        ReportManager reportManager = new ReportManager();
        try {
            reportManager.setReportQueue(settings.loadReportQueue());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (verbose) {
            System.out.println("Modes: " + ((hidden) ? "\n\t-Hidden mode" : "") + ((update) ? "\n\t-Update mode" : "") + ((recursive) ? "\n\t-Recursive mode" : ""));
            DirBackupConsoleReport r = new DirBackupConsoleReport();
            r.setBackupInfo(backupInfo);
            reportManager.getReportQueue().add(r);
            reportManager.flush();
        }
        settings.saveReportQueue(reportManager.getReportQueue());
    }
