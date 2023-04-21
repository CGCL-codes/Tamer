    public void printIntervalGraphs() {
        ArrayList command = new ArrayList();
        command.add(parameters.getAppsDirectory() + "/IntervalGraphPrinter");
        command.add("-f");
        File[] intervalFiles = IO.extractOnlyFiles(intervalDirectory);
        int numToProc = intervalFiles.length;
        for (int i = 0; i < numToProc; i++) {
            ArrayList all = new ArrayList();
            all.addAll(command);
            all.add(intervalFiles[i].toString());
            launchJQSub(all, parameters.getMaxMemory());
        }
        int timer = 60;
        System.out.print("\t# to graph ");
        while (timer != 0) {
            int num = numToProc - IO.numberFilesExist(intervalDirectory, ".bed");
            System.out.print(num + " ");
            if (num == 0) break;
            timer--;
            Misc.sleep(20);
        }
        System.out.println();
        if (timer == 0) Misc.printExit("\nError: timed out printing Intervals graphs.\n"); else {
            File dir = new File(intervalDirectory, "Sgr");
            if (dir.exists() == false) dir.mkdir();
            File[] res = IO.extractFiles(intervalDirectory, ".sgr.zip");
            for (int i = 0; i < res.length; i++) {
                File moved = new File(dir, res[i].getName());
                res[i].renameTo(moved);
            }
            dir = new File(intervalDirectory, "Bed");
            if (dir.exists() == false) dir.mkdir();
            res = IO.extractFiles(intervalDirectory, ".bed");
            for (int i = 0; i < res.length; i++) {
                File moved = new File(dir, res[i].getName());
                res[i].renameTo(moved);
            }
        }
    }
