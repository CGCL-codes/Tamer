    public static void createBackupStatisticsFile() throws IOException {
        deleteBackupStatistics();
        FileReader fr = new FileReader("PlayerStatistics.txt");
        Scanner scan = new Scanner(fr);
        FileWriter fw = new FileWriter("BackupStatistics.txt", true);
        BufferedWriter out = new BufferedWriter(fw);
        while (scan.hasNext()) {
            out.write(scan.nextLine());
            out.newLine();
        }
        scan.close();
        out.close();
    }
