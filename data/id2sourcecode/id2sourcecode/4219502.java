    private void updateIndex(CmsSearchIndex index, I_CmsReport report, List resourcesToIndex) throws CmsException {
        CmsObject cms = OpenCms.initCmsObject(m_adminCms);
        if (report == null) {
            report = new CmsLogReport(cms.getRequestContext().getLocale(), CmsSearchManager.class);
        }
        if (!index.checkConfiguration(cms)) {
            return;
        }
        cms.getRequestContext().setSiteRoot("/");
        cms.getRequestContext().setCurrentProject(cms.readProject(index.getProject()));
        if ((resourcesToIndex == null) || resourcesToIndex.isEmpty()) {
            forceIndexUnlock(index, report, false);
            CmsIndexingThreadManager threadManager = new CmsIndexingThreadManager(m_timeout);
            IndexWriter writer = null;
            try {
                writer = index.getIndexWriter(true);
                report.println(Messages.get().container(Messages.RPT_SEARCH_INDEXING_REBUILD_BEGIN_1, index.getName()), I_CmsReport.FORMAT_HEADLINE);
                Iterator sources = index.getSources().iterator();
                while (sources.hasNext()) {
                    CmsSearchIndexSource source = (CmsSearchIndexSource) sources.next();
                    I_CmsIndexer indexer = source.getIndexer().newInstance(cms, report, index);
                    indexer.rebuildIndex(writer, threadManager, source);
                }
                while (threadManager.isRunning()) {
                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                    }
                }
                try {
                    writer.optimize();
                } catch (IOException e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn(Messages.get().getBundle().key(Messages.LOG_IO_INDEX_WRITER_OPTIMIZE_1, index.getPath(), index.getName()), e);
                    }
                }
                report.println(Messages.get().container(Messages.RPT_SEARCH_INDEXING_REBUILD_END_1, index.getName()), I_CmsReport.FORMAT_HEADLINE);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        if (LOG.isWarnEnabled()) {
                            LOG.warn(Messages.get().getBundle().key(Messages.LOG_IO_INDEX_WRITER_CLOSE_2, index.getPath(), index.getName()), e);
                        }
                    }
                }
            }
            threadManager.reportStatistics(report);
        } else {
            List updateCollections = new ArrayList();
            boolean hasResourcesToDelete = false;
            boolean hasResourcesToUpdate = false;
            Iterator sources = index.getSources().iterator();
            while (sources.hasNext()) {
                CmsSearchIndexSource source = (CmsSearchIndexSource) sources.next();
                I_CmsIndexer indexer = source.getIndexer().newInstance(cms, report, index);
                CmsSearchIndexUpdateData updateData = indexer.getUpdateData(source, resourcesToIndex);
                if (!updateData.isEmpty()) {
                    updateCollections.add(updateData);
                    hasResourcesToDelete = hasResourcesToDelete | updateData.hasResourcesToDelete();
                    hasResourcesToUpdate = hasResourcesToUpdate | updateData.hasResourceToUpdate();
                }
            }
            if (hasResourcesToDelete || hasResourcesToUpdate) {
                report.println(Messages.get().container(Messages.RPT_SEARCH_INDEXING_UPDATE_BEGIN_1, index.getName()), I_CmsReport.FORMAT_HEADLINE);
            }
            forceIndexUnlock(index, report, true);
            if (hasResourcesToDelete) {
                IndexReader reader = null;
                try {
                    reader = IndexReader.open(index.getPath());
                } catch (IOException e) {
                    LOG.error(Messages.get().getBundle().key(Messages.LOG_IO_INDEX_READER_OPEN_2, index.getPath(), index.getName()), e);
                }
                if (reader != null) {
                    try {
                        Iterator i = updateCollections.iterator();
                        while (i.hasNext()) {
                            CmsSearchIndexUpdateData updateCollection = (CmsSearchIndexUpdateData) i.next();
                            if (updateCollection.hasResourcesToDelete()) {
                                updateCollection.getIndexer().deleteResources(reader, updateCollection.getResourcesToDelete());
                            }
                        }
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            LOG.error(Messages.get().getBundle().key(Messages.LOG_IO_INDEX_READER_CLOSE_2, index.getPath(), index.getName()), e);
                        }
                    }
                }
            }
            if (hasResourcesToUpdate) {
                CmsIndexingThreadManager threadManager = new CmsIndexingThreadManager(m_timeout);
                IndexWriter writer = null;
                try {
                    writer = index.getIndexWriter(false);
                    Iterator i = updateCollections.iterator();
                    while (i.hasNext()) {
                        CmsSearchIndexUpdateData updateCollection = (CmsSearchIndexUpdateData) i.next();
                        if (updateCollection.hasResourceToUpdate()) {
                            updateCollection.getIndexer().updateResources(writer, threadManager, updateCollection.getResourcesToUpdate());
                        }
                    }
                    while (threadManager.isRunning()) {
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            LOG.error(Messages.get().getBundle().key(Messages.LOG_IO_INDEX_WRITER_CLOSE_2, index.getPath(), index.getName()), e);
                        }
                    }
                }
            }
            if (hasResourcesToDelete || hasResourcesToUpdate) {
                report.println(Messages.get().container(Messages.RPT_SEARCH_INDEXING_UPDATE_END_1, index.getName()), I_CmsReport.FORMAT_HEADLINE);
            }
        }
    }
