    public static void makeXFoldCrossValidationSets(int partitionCount, List<PairEntry> pairs, String destinationPath) throws IOException {
        List<PairEntry> pairsCopy = new ArrayList<PairEntry>(pairs);
        int partSize = (int) Math.ceil((double) pairsCopy.size() / ((double) partitionCount));
        Random rand = new Random();
        ArrayList<ArrayList<PairEntry>> partition = new ArrayList<ArrayList<PairEntry>>();
        for (int partNo = 0; partNo < partitionCount; partNo++) {
            ArrayList<PairEntry> part = new ArrayList<PairEntry>();
            partition.add(part);
            if (partNo == partitionCount - 1) {
                part.addAll(pairsCopy);
            } else {
                for (int i = 0; i < partSize; i++) {
                    int index = rand.nextInt(pairsCopy.size());
                    part.add(pairsCopy.get(index));
                    pairsCopy.remove(index);
                }
            }
        }
        destinationPath = TextRulerToolkit.addTrailingSlashToPath(destinationPath);
        for (int i = 0; i < partitionCount; i++) {
            String foldDest = destinationPath + i + "/";
            TextRulerToolkit.log("MK: " + new File(foldDest).mkdir());
            new File(foldDest + "training").mkdir();
            new File(foldDest + "testing").mkdir();
            new File(foldDest + "training/withtags").mkdir();
            new File(foldDest + "training/withouttags").mkdir();
            new File(foldDest + "testing/withtags").mkdir();
            new File(foldDest + "testing/withouttags").mkdir();
            String trainingWithTagsDir = foldDest + "training/withtags/";
            String trainingWithoutTagsDir = foldDest + "training/withouttags/";
            String testingWithTagsDir = foldDest + "testing/withtags/";
            String testingWithoutTagsDir = foldDest + "testing/withouttags/";
            for (PairEntry pe : partition.get(i)) {
                File srcFile = new File(trainingDir + pe.trainingsFileName);
                File dstFile = new File(testingWithTagsDir);
                FileUtils.copyFile(srcFile, dstFile);
                srcFile = new File(testDir + pe.testFileName);
                dstFile = new File(testingWithoutTagsDir);
                FileUtils.copyFile(srcFile, dstFile);
            }
            for (int restI = 0; restI < partitionCount; restI++) if (restI != i) {
                for (PairEntry pe : partition.get(restI)) {
                    File srcFile = new File(trainingDir + pe.trainingsFileName);
                    File dstFile = new File(trainingWithTagsDir);
                    FileUtils.copyFile(srcFile, dstFile);
                    srcFile = new File(testDir + pe.testFileName);
                    dstFile = new File(trainingWithoutTagsDir);
                    FileUtils.copyFile(srcFile, dstFile);
                }
            }
        }
    }
