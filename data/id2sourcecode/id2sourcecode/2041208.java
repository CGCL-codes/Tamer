    public void printSpreadSheets() {
        ArrayList command = new ArrayList();
        command.add(parameters.getAppsDirectory() + "/IntervalReportPrinter");
        command.add("-i");
        command.add("1");
        command.add("-a");
        command.add("-b");
        command.add("-f");
        File[] intervalFiles = IO.extractFiles(intervalDirectory);
        int numToProc = intervalFiles.length;
        for (int i = 0; i < numToProc; i++) {
            ArrayList all = new ArrayList();
            all.addAll(command);
            all.add(intervalFiles[i].toString());
            launchJQSub(all, parameters.getMaxMemory());
        }
        int timer = 60;
        System.out.print("\t# to print ");
        while (timer != 0) {
            int num = numToProc - IO.numberFilesExist(intervalDirectory, "xls");
            System.out.print(num + " ");
            if (num == 0) break;
            timer--;
            Misc.sleep(20);
        }
        System.out.println();
        if (timer == 0) Misc.printExit("\nError: timed out printing Intervals spread sheet reports.\n"); else {
            File spreadSheetDir = new File(intervalDirectory, "SpreadSheetReports");
            if (spreadSheetDir.exists() == false) spreadSheetDir.mkdir();
            File[] res = IO.extractFiles(intervalDirectory, ".xls");
            for (int i = 0; i < res.length; i++) {
                File moved = new File(spreadSheetDir, res[i].getName());
                res[i].renameTo(moved);
            }
        }
    }
