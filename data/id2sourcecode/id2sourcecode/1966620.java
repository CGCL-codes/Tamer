    @Override
    public void index() {
        List<String> directories = optionsManager.getDirectories();
        String[] locations = directories.toArray(new String[0]);
        setLocations(locations);
        Log.log(Log.DEBUG, this, "Indexing - locations: " + Arrays.toString(locations));
        Date startDate = new Date();
        long startTime = startDate.getTime();
        indexStatsManager.setIndexStartTime(startDate);
        indexStatsManager.setIndexing(true);
        super.index();
        Date endDate = new Date();
        long endTime = endDate.getTime();
        long indexingTime = endTime - startTime;
        indexStatsManager.setIndexEndTime(endDate);
        indexStatsManager.setDirectoriesIndexed(getDirectoriesIndexed());
        indexStatsManager.setFilesIndexed(getFilesIndexed());
        Log.log(Log.DEBUG, this, "Indexing complete - time: " + indexingTime + " | directories: " + getDirectoriesIndexed() + " | files: " + getFilesIndexed());
        File indexStoreDir = new LucenePlugin().getIndexStoreDirectory();
        indexStoreDir = new File(indexStoreDir, "LucenePlugin");
        File readIndexLocation = new File(indexStoreDir, "read");
        File writeIndexLocation = new File(indexStoreDir, "write");
        if (readIndexLocation.exists()) {
            Log.log(Log.DEBUG, this, "Replacing old index files with new ones.");
            replaceReadOnlyIndex(readIndexLocation, writeIndexLocation);
        } else {
            Log.log(Log.DEBUG, this, "Couldn't find old index files, moving new ones in place.");
            copyWriteIndexToReadIndex(readIndexLocation, writeIndexLocation);
        }
    }
