    public static void writeReportsToOutputStream(OutputStream pOut, ReportGenerator pReportFactory, ReportFactoryParameters pReportParams, DataContainer pDataContainer) throws ReportFactoryException {
        try {
            OutputStream vOut = new BufferedOutputStream(pOut);
            if (pReportParams.isZip()) {
                vOut = new ZipOutputStream(pOut);
            }
            for (int vIndex = 0; vIndex < pReportParams.getReportIDs().length; vIndex++) {
                String vIdReport = pReportParams.getReportIDs()[vIndex];
                IReportConfig vReportConfig = pReportFactory.getConfiguration().getReport(vIdReport);
                if (vReportConfig == null) {
                    String vConfigurationName = pReportParams.getConfigurationName();
                    throw new ReportFactoryException("Unable to find configuration for report [" + vIdReport + "] in configuration [" + vConfigurationName + "]");
                }
                String vFilename = pReportParams.getFilenames()[vIndex];
                if (vFilename == null || vFilename.equals("report-" + vIdReport)) {
                    vFilename = StringUtils.defaultString(vReportConfig.getDefaultFileName(), vIdReport);
                    if (pReportParams.isDateSuffixe()) {
                        StringBuffer vNewFilename = new StringBuffer();
                        vNewFilename.append(vFilename.substring(0, vFilename.lastIndexOf(".")));
                        vNewFilename.append("_").append((new Date()).getTime());
                        vNewFilename.append(vFilename.lastIndexOf("."));
                        vFilename = vNewFilename.toString();
                    }
                }
                if (pReportParams.isZip()) {
                    ZipEntry vZipEntry = new ZipEntry(vFilename);
                    ((ZipOutputStream) vOut).putNextEntry(vZipEntry);
                }
                if (pDataContainer != null) {
                    pReportFactory.process(vIdReport, pDataContainer, pReportParams.getParameterValues(), vOut);
                } else {
                    pReportFactory.process(vIdReport, pReportParams.getParameterValues(), vOut);
                }
                if (pReportParams.isZip()) {
                    ((ZipOutputStream) vOut).closeEntry();
                    if (logger.isDebugEnabled()) {
                        logger.debug("closing zip entry [" + vFilename + "]...");
                    }
                }
            }
            if (pReportParams.isZip()) {
                ((ZipOutputStream) vOut).finish();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ReportFactoryException(e.getMessage(), e);
        }
    }
