    private void stylesheetCommand(HttpServletRequest request, HttpServletResponse response, DataSourceConfiguration dsnConfig, String queryString) throws BadCommandArgumentsException, IOException, BadStylesheetException {
        if (queryString != null && queryString.trim().length() > 0) {
            throw new BadCommandArgumentsException("Arguments have been passed to the stylesheet command, which does not expect any.");
        }
        String stylesheetFileName;
        if (dsnConfig.getStyleSheet() != null && dsnConfig.getStyleSheet().trim().length() > 0) {
            stylesheetFileName = dsnConfig.getStyleSheet().trim();
        } else if (DATA_SOURCE_MANAGER.getServerConfiguration().getGlobalConfiguration().getDefaultStyleSheet() != null && DATA_SOURCE_MANAGER.getServerConfiguration().getGlobalConfiguration().getDefaultStyleSheet().trim().length() > 0) {
            stylesheetFileName = DATA_SOURCE_MANAGER.getServerConfiguration().getGlobalConfiguration().getDefaultStyleSheet().trim();
        } else {
            throw new BadStylesheetException("This data source has not defined a stylesheet.");
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getServletContext().getResourceAsStream(RESOURCE_FOLDER + stylesheetFileName)));
            if (reader.ready()) {
                writeHeader(request, response, XDasStatus.STATUS_200_OK, true);
                writer = getResponseWriter(request, response);
                while (reader.ready()) {
                    writer.write(reader.readLine());
                }
            } else {
                throw new BadStylesheetException("A problem has occurred reading in the stylesheet from the open stream");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }
