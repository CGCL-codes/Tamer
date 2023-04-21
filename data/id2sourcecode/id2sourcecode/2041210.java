    public void filterIntervals() {
        ArrayList command = new ArrayList();
        command.add(parameters.getAppsDirectory() + "/IntervalFilter");
        command.add("-i");
        command.add("1");
        command.add("-m");
        command.add("1.5");
        command.add("-e");
        command.add(IO.concatinateFileFullPathNames(parameters.getRepeatRegionFiles(), ","));
        command.add("-k");
        File[] intervalFiles = IO.extractFiles(intervalDirectory);
        int numToProc = intervalFiles.length;
        for (int i = 0; i < numToProc; i++) {
            ArrayList all = new ArrayList();
            all.addAll(command);
            all.add(intervalFiles[i].toString());
            launchJQSub(all, parameters.getMaxMemory());
        }
        int timer = 600;
        System.out.print("\t# to filter ");
        while (timer != 0) {
            int num = numToProc - IO.numberFilesExist(intervalDirectory, "Good");
            System.out.print(num + " ");
            if (num == 0) break;
            timer--;
            Misc.sleep(60);
        }
        System.out.println();
        if (timer == 0) Misc.printExit("\nError: timed out filtering intervals.\n"); else {
            IO.deleteFilesNotEndingInExtension(intervalDirectory, "Good");
            IO.removeExtension(intervalDirectory, "Good");
        }
    }
