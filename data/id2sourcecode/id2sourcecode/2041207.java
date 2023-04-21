    public void findSubBindingRegions() {
        ArrayList command = new ArrayList();
        command.add(parameters.getAppsDirectory() + "/FindSubBindingRegions");
        command.add("-w");
        command.add(parameters.getSubWindowSize() + "");
        command.add("-n");
        command.add(parameters.getMinNumberOligosInSubWin() + "");
        command.add("-s");
        command.add(parameters.getPeakPickerWindowSize() + "");
        command.add("-m");
        command.add(parameters.getMaxNumberPeaks() + "");
        command.add("-i");
        File[] intervalFiles = IO.extractOnlyFiles(intervalDirectory);
        int numToSub = intervalFiles.length;
        for (int i = 0; i < numToSub; i++) {
            ArrayList all = new ArrayList();
            all.addAll(command);
            all.add(intervalFiles[i].toString());
            launchJQSub(all, parameters.getMaxMemory());
        }
        int timer = 120;
        System.out.print("\t# to pick peaks ");
        while (timer != 0) {
            int num = numToSub - IO.numberFilesExist(intervalDirectory, "Sub");
            System.out.print(num + " ");
            if (num == 0) break;
            timer--;
            Misc.sleep(60);
        }
        System.out.println();
        if (timer == 0) Misc.printExit("\nError: timed out picking interval peaks.\n"); else {
            IO.deleteFilesNotEndingInExtension(intervalDirectory, "Sub");
            IO.removeExtension(intervalDirectory, "Sub");
        }
    }
