    public void perform(ResultList resultList, boolean continueOnError) {
        FileUtils.copyFile(file, destDir);
        resultList.addResult(new SyncResult(Result.Type.OK, "Copied " + file.getName() + " from " + file.getParent() + " to " + destDir));
    }
