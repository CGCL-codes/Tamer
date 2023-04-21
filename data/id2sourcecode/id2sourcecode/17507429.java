    public CmsIndexingThread(CmsObject cms, IndexWriter writer, CmsResource res, I_CmsDocumentFactory documentType, CmsSearchIndex index, I_CmsReport report) {
        super("OpenCms: Indexing '" + res.getName() + "'");
        m_cms = cms;
        m_writer = writer;
        m_res = res;
        m_documentType = documentType;
        m_index = index;
        m_report = report;
    }
