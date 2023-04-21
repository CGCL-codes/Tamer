    public void createIndexingThread(CmsObject cms, IndexWriter writer, CmsResource res, CmsSearchIndex index, I_CmsReport report) {
        boolean excludeFromIndex = false;
        try {
            excludeFromIndex = Boolean.valueOf(cms.readPropertyObject(res, CmsPropertyDefinition.PROPERTY_SEARCH_EXCLUDE, true).getValue()).booleanValue();
        } catch (CmsException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.get().getBundle().key(Messages.LOG_UNABLE_TO_READ_PROPERTY_1, res.getRootPath()));
            }
        }
        if (!excludeFromIndex) {
            List locales = OpenCms.getLocaleManager().getDefaultLocales(cms, res);
            Locale match = OpenCms.getLocaleManager().getFirstMatchingLocale(Collections.singletonList(index.getLocale()), locales);
            excludeFromIndex = (match == null);
        }
        I_CmsDocumentFactory documentType = null;
        if (!excludeFromIndex) {
            documentType = index.getDocumentFactory(res);
        }
        if (documentType == null) {
            m_startedCounter++;
            m_returnedCounter++;
            if (report != null) {
                report.println(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_SKIPPED_0), I_CmsReport.FORMAT_NOTE);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.get().getBundle().key(Messages.LOG_SKIPPED_1, res.getRootPath()));
            }
            return;
        }
        CmsIndexingThread thread = new CmsIndexingThread(cms, writer, res, documentType, index, report);
        m_startedCounter++;
        thread.start();
        try {
            thread.join(m_timeout);
        } catch (InterruptedException e) {
        }
        if (thread.isAlive()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(Messages.get().getBundle().key(Messages.LOG_INDEXING_TIMEOUT_1, res.getRootPath()));
            }
            if (report != null) {
                report.println();
                report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_FAILED_0), I_CmsReport.FORMAT_WARNING);
                report.println(Messages.get().container(Messages.RPT_SEARCH_INDEXING_TIMEOUT_1, res.getRootPath()), I_CmsReport.FORMAT_WARNING);
            }
            m_abandonedCounter++;
            thread.interrupt();
        } else {
            m_returnedCounter++;
        }
    }
