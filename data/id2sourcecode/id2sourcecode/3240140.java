    public void exportXml(File directory, IDatabase database, ProgressMonitor progressMonitor) throws IOException, SQLException {
        if (directory == null) {
            throw new IllegalArgumentException("directory == null");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("directory input is not a directory");
        }
        if (database == null) {
            throw new IllegalArgumentException("database == null");
        }
        long start0 = System.currentTimeMillis();
        copyStyleSheet(directory);
        if (progressMonitor != null) {
            if (progressMonitor.isCanceled()) {
                return;
            }
            notifyProgress(progressMonitor, "Copied style sheet", 0);
        }
        if (IS_PROFILING) {
            logElapsedTime("Copy style sheet file", start0);
        }
        long start1 = System.currentTimeMillis();
        List<GenericData> dataList = database.executeQuery(SELECT);
        if (LOGGER.isLoggable(Level.FINEST)) {
            for (int i = 0; i < dataList.size(); i++) {
                GenericData data = dataList.get(i);
                LOGGER.log(Level.FINEST, i + " " + data.toString());
            }
        }
        if (progressMonitor != null) {
            if (progressMonitor.isCanceled()) {
                return;
            }
            notifyProgress(progressMonitor, "Performed database select", 1);
        }
        if (IS_PROFILING) {
            logElapsedTime("Database select", start1);
        }
        int offset = 1;
        XMLUtilities xmlUtils = new XMLUtilities();
        TransformUtilities transformUtils = new TransformUtilities();
        for (int i = 0; i < SORT_COLUMNS.length; i++) {
            String columnName = SORT_COLUMNS[i];
            long start2 = System.currentTimeMillis();
            if (progressMonitor != null) {
                if (progressMonitor.isCanceled()) {
                    return;
                }
                notifyProgress(progressMonitor, columnName + ": Generating XML", offset + (3 * i));
            }
            String xml = generateXml(dataList, columnName);
            LOGGER.finest("xml = " + xml);
            if (IS_PROFILING) {
                logElapsedTime(columnName + ": Generate XML", start2);
            }
            long start3 = System.currentTimeMillis();
            if (progressMonitor != null) {
                if (progressMonitor.isCanceled()) {
                    return;
                }
                notifyProgress(progressMonitor, columnName + ": Transforming XML to HTML", offset + (3 * i) + 1);
            }
            DOMResult domResult = transformUtils.transform(xml, XSLT);
            Node node = domResult.getNode();
            String html = xmlUtils.convertToString(node, true, false);
            if (html.startsWith(XMLUtilities.XML_HEADER)) {
                html = html.substring(XMLUtilities.XML_HEADER.length());
                html = HTML_HEADER + html;
            }
            if (IS_PROFILING) {
                logElapsedTime(columnName + ": Transform XML to HTML", start3);
            }
            long start4 = System.currentTimeMillis();
            StringBuffer filename = new StringBuffer();
            filename.append("videos_").append(columnName).append(".html");
            if (progressMonitor != null) {
                if (progressMonitor.isCanceled()) {
                    return;
                }
                notifyProgress(progressMonitor, columnName + ": Writing file " + filename.toString(), offset + (3 * i) + 2);
            }
            File toFile = new File(directory, filename.toString());
            FILE_UTILITIES.writeFile(toFile, html);
            if (IS_PROFILING) {
                logElapsedTime(columnName + ": Write HTML to file", start4);
            }
        }
        if (progressMonitor != null) {
            notifyProgress(progressMonitor, "Done", getMaxProgress());
        }
        if (IS_PROFILING) {
            logElapsedTime("Export XML", start0);
        }
    }
