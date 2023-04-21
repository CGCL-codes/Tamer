    public static void copyPairs(List<PairEntry> pairs, String dest) throws IOException {
        dest = TextRulerToolkit.addTrailingSlashToPath(dest);
        String withTags = dest + "withtags";
        String withoutTags = dest + "withouttags";
        new File(withTags).mkdir();
        new File(withoutTags).mkdir();
        for (PairEntry pe : pairs) {
            File srcFile = new File(trainingDir + pe.trainingsFileName);
            File dstFile = new File(withTags);
            FileUtils.copyFile(srcFile, dstFile);
            srcFile = new File(testDir + pe.testFileName);
            dstFile = new File(withoutTags);
            FileUtils.copyFile(srcFile, dstFile);
        }
    }
