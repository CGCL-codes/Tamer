    private void work(String[] args) throws Exception {
        String contentDir = CONTENT_DIR;
        File contentDirHandle = new File(contentDir);
        FilenameFilter ffilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                String lcFn = name.toLowerCase();
                if (lcFn.startsWith("official-cpe-dictionary") && lcFn.endsWith(".xml")) {
                    return true;
                }
                return false;
            }
        };
        File[] dictFiles = contentDirHandle.listFiles(ffilter);
        if (dictFiles == null || dictFiles.length == 0) {
            throw new RuntimeException("official cpe dictionary file not found!");
        }
        String dictFilename = dictFiles[0].getAbsolutePath();
        String ovalFilename = dictFiles[0].getAbsoluteFile().getParentFile().getAbsolutePath() + File.separator + OVAL_FILE;
        File dictFile = new File(dictFilename);
        File ovalFile = new File(ovalFilename);
        boolean dictChanged = false;
        CPEDictionaryDocument cpeDict = (CPEDictionaryDocument) SCAPDocumentFactory.loadDocument(dictFile);
        if (cpeDict == null) {
            throw new IllegalStateException("CPEDictionary could not be loaded!");
        }
        OvalDefinitionsDocument cpeOval = (OvalDefinitionsDocument) SCAPDocumentFactory.createNewDocument(SCAPDocumentTypeEnum.OVAL_59);
        cpeOval.setFilename(ovalFile.getAbsolutePath());
        int itemsProcessed = 0;
        int itemsWithChecks = 0;
        List<CPEItem> items = cpeDict.getItems();
        MergeStats stats = new MergeStats();
        for (int x = 0; x < items.size(); x++) {
            CPEItem item = items.get(x);
            itemsProcessed++;
            List<CPEItemCheck> checks = item.getChecks();
            if (checks == null) {
                continue;
            }
            for (int y = 0; y < checks.size(); y++) {
                CPEItemCheck check = checks.get(y);
                if (check.getSystem() != null && check.getSystem().equals(CPEItemCheckSystemType.OVAL5)) {
                    String href = check.getHref();
                    if (href.indexOf(":") == -1) {
                        continue;
                    }
                    System.out.println("Downloading content for cpe item " + item.getName() + "(" + itemsProcessed + ")");
                    dictChanged = true;
                    itemsWithChecks++;
                    URL url = new URL(href);
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    LOG.debug("loading oval definitions document from URL " + href);
                    OvalDefinitionsDocument odd = (OvalDefinitionsDocument) SCAPDocumentFactory.loadDocument(is);
                    LOG.debug("oval definitions document loaded");
                    if (odd != null) {
                        cpeOval.merge(odd, stats);
                    }
                    odd = null;
                    check.setHref(ovalFile.getName());
                }
            }
        }
        if (dictChanged) {
            System.out.println("Items processed = " + itemsProcessed);
            System.out.println("Items with checks = " + itemsWithChecks);
            cpeDict.save();
            cpeOval.save();
        } else {
            System.out.println("Files have already been processed, no changes needed.");
        }
    }
