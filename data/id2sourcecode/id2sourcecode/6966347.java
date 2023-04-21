    public static void createBackupAccountsFile() throws IOException {
        deleteBackupAccounts();
        FileReader fr = new FileReader("Accounts.txt");
        Scanner scan = new Scanner(fr);
        FileWriter fw = new FileWriter("BackupAccounts.txt", true);
        BufferedWriter out = new BufferedWriter(fw);
        while (scan.hasNext()) {
            out.write(scan.nextLine());
            out.newLine();
        }
        scan.close();
        out.close();
    }
