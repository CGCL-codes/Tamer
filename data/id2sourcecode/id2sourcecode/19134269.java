    protected void persistSearchEngineResourceArtifacts() throws Exception {
        Date now = new Date();
        String searchEngineResourceDateStr = Long.toString(System.currentTimeMillis());
        searchEngineResourceDateStr += "_" + DateUtils.convertDateToString(now, SearchEngineResourceService.SEARCH_ENGINE_RESOURCE_DIR_DATE_FORMAT);
        String auditResourceDirPath = getSearchEngineResourceService().getSearchEngineResourceAuditDirectoryPath();
        auditResourceDirPath = GeneralUtils.generateAbsolutePath(auditResourceDirPath, searchEngineResourceDateStr, GeneralConstants.FORWARD_SLASH_TOKEN);
        File auditResourceDir = new File(auditResourceDirPath);
        FileUtils.forceMkdir(auditResourceDir);
        SearchEngineResource searchEngineResource = getSearchEngineResourceService().createSearchEngineResource();
        String auditSearchEngineResourceFilePath = GeneralUtils.generateAbsolutePath(auditResourceDirPath, SearchEngineResourceService.SEARCH_ENGINE_RESOURCE_FILE_NAME, GeneralConstants.FORWARD_SLASH_TOKEN);
        File auditSearchEngineResourceFile = new File(auditSearchEngineResourceFilePath);
        System.gc();
        if (isLoggingInfo()) {
            logInfo("Before serializing the search engine resource");
        }
        getSearchEngineResourceService().serializeSearchEngineResource(searchEngineResource, auditSearchEngineResourceFilePath);
        if (isLoggingInfo()) {
            logInfo("After serializing the search engine resource");
        }
        SearchEngineResourceMetaInfo searchEngineResourceMetaInfo = new SearchEngineResourceMetaInfo();
        searchEngineResourceMetaInfo.setSearchEngineResourceDate(now);
        long timeStamp = System.currentTimeMillis();
        searchEngineResourceMetaInfo.setSearchEngineResourceTimeStamp(timeStamp);
        long searchEngineResourceChecksum = FileUtils.checksumCRC32(auditSearchEngineResourceFile);
        searchEngineResourceMetaInfo.setSearchEngineResourceChecksum(searchEngineResourceChecksum);
        String auditSearchEngineResourceMetaInfoFilePath = GeneralUtils.generateAbsolutePath(auditResourceDirPath, SearchEngineResourceService.SEARCH_ENGINE_RESOURCE_META_INFO_FILE_NAME, GeneralConstants.FORWARD_SLASH_TOKEN);
        File auditSearchEngineResourceMetaInfoFile = new File(auditSearchEngineResourceMetaInfoFilePath);
        getSearchEngineResourceService().generateSearchEngineResourceMetaInfoFile(searchEngineResourceMetaInfo, auditSearchEngineResourceMetaInfoFilePath);
        if (isLoggingInfo()) {
            logInfo("Serialized the search engine resource in: " + auditSearchEngineResourceFilePath);
            logInfo("Wrote the search engine resource meta file: " + auditSearchEngineResourceMetaInfoFilePath);
        }
        String latestResourceDirPath = getSearchEngineResourceService().getSearchEngineResourceLatestDirectoryPath();
        File latestResourceDir = new File(latestResourceDirPath);
        FileUtils.deleteDirectory(latestResourceDir);
        FileUtils.forceMkdir(latestResourceDir);
        FileUtils.copyFileToDirectory(auditSearchEngineResourceFile, latestResourceDir);
        FileUtils.copyFileToDirectory(auditSearchEngineResourceMetaInfoFile, latestResourceDir);
        if (isLoggingInfo()) {
            logInfo("Copied " + auditSearchEngineResourceFilePath + " to " + latestResourceDir);
            logInfo("Copied " + auditSearchEngineResourceMetaInfoFilePath + " to " + latestResourceDir);
        }
    }
