    public void loadIntervalArrays() {
        ArrayList command = new ArrayList();
        command.add(parameters.getAppsDirectory() + "/LoadChipSetIntervalOligoInfo");
        if (parameters.getGenomeFastaDirectory() != null) {
            command.add("-s");
            command.add(parameters.getGenomeFastaDirectory().toString());
        }
        command.add("-o");
        command.add(oligoPositions.toString());
        command.add("-i");
        File[] intervalFiles = IO.extractOnlyFiles(intervalDirectory);
        int numToLoad = intervalFiles.length;
        String treatmentDirs = IO.concatinateFileFullPathNames(treatmentReplicas, ",");
        String controlDirs = IO.concatinateFileFullPathNames(controlReplicas, ",");
        for (int i = 0; i < numToLoad; i++) {
            ArrayList all = new ArrayList();
            all.addAll(command);
            all.add(intervalFiles[i].toString());
            String t = "-t";
            String c = "-c";
            if (intervalFiles[i].toString().startsWith("rd")) {
                t = "-c";
                c = "-t";
            }
            all.add(t);
            all.add(treatmentDirs);
            all.add(c);
            all.add(controlDirs);
            launchJQSub(all, parameters.getMaxMemory());
        }
        int timer = 240;
        System.out.print("\t# to load ");
        while (timer != 0) {
            int num = numToLoad - IO.numberFilesExist(intervalDirectory, "Ld");
            System.out.print(num + " ");
            if (num == 0) break;
            timer--;
            Misc.sleep(60);
        }
        System.out.println();
        if (timer == 0) Misc.printExit("\nError: timed out on loading intervals.\n"); else {
            IO.deleteFilesNotEndingInExtension(intervalDirectory, "Ld");
            IO.removeExtension(intervalDirectory, "Ld");
        }
    }
