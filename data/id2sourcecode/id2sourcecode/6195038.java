    protected void exportFile(CmsFile file) throws CmsImportExportException, SAXException, IOException {
        String source = trimResourceName(getCms().getSitePath(file));
        I_CmsReport report = getReport();
        m_exportCount++;
        report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_SUCCESSION_1, String.valueOf(m_exportCount)), I_CmsReport.FORMAT_NOTE);
        report.print(Messages.get().container(Messages.RPT_EXPORT_0), I_CmsReport.FORMAT_NOTE);
        report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_ARGUMENT_1, getCms().getSitePath(file)));
        report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_DOTS_0));
        if (!m_exportedResources.contains(file.getResourceId())) {
            ZipEntry entry = new ZipEntry(source);
            entry.setTime(file.getDateLastModified());
            getExportZipStream().putNextEntry(entry);
            getExportZipStream().write(file.getContents());
            getExportZipStream().closeEntry();
            m_exportedResources.add(file.getResourceId());
            appendResourceToManifest(file, true);
        } else {
            appendResourceToManifest(file, false);
        }
        if (LOG.isInfoEnabled()) {
            LOG.info(Messages.get().getBundle().key(Messages.LOG_EXPORTING_OK_2, String.valueOf(m_exportCount), source));
        }
        report.println(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_OK_0), I_CmsReport.FORMAT_OK);
    }
