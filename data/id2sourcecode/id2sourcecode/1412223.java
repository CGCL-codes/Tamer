    public MakeblastdbProcessBuilder(String pathToJarFile, File aDatabaseFile, WaitingDialog waitingDialog) {
        this.waitingDialog = waitingDialog;
        try {
            File makeBlastDb = new File(pathToJarFile + File.separator + getMakeblastdbFolder() + File.separator + "makeblastdb");
            makeBlastDb.setExecutable(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        process_name_array.add(pathToJarFile + File.separator + getMakeblastdbFolder() + File.separator + "makeblastdb");
        process_name_array.add("-in");
        process_name_array.add(aDatabaseFile.getName());
        System.out.println("\n\nmakeblastdb command: ");
        for (int i = 0; i < process_name_array.size(); i++) {
            System.out.print(process_name_array.get(i) + " ");
        }
        System.out.println("\n");
        pb = new ProcessBuilder(process_name_array);
        pb.directory(aDatabaseFile.getParentFile());
        pb.redirectErrorStream(true);
        iDatabaseFile = aDatabaseFile;
    }
