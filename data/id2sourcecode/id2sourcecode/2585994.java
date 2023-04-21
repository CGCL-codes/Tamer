    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {
        if (this.m_term.getColumnName() == null) {
            setWarningMessage("Columns for \"term\" was not selected.");
        }
        if (inData == null || inData.length != 1 || inData[0] == null) {
            throw new IllegalArgumentException("No input data available.");
        }
        final BufferedDataTable inputTable = inData[0];
        if (inputTable.getRowCount() < 1) {
            setWarningMessage("Empty input table found");
        }
        final int columnIndex = inputTable.getDataTableSpec().findColumnIndex(this.m_term.getColumnName());
        if (columnIndex == -1) throw new IllegalArgumentException("No column data available column selected was :" + m_term.getColumnName());
        LOGGER.info("ColumnIndex=" + columnIndex);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        SAXParser parser = factory.newSAXParser();
        StringCell dbAsCell = new StringCell(m_database.getStringValue());
        int rowCountInput = inputTable.getRowCount();
        int rowCount = -1;
        int rowIndex = 0;
        DataTableSpec dataTableSpec = createTableSpec();
        BufferedDataContainer container = exec.createDataContainer(dataTableSpec);
        for (DataRow row : inputTable) {
            ++rowCount;
            DataCell keyCell = row.getCell(columnIndex);
            if (keyCell.isMissing()) {
                LOGGER.info("empty cell");
                continue;
            }
            if (!(keyCell instanceof org.knime.core.data.def.StringCell)) {
                LOGGER.info("not a String cell");
                continue;
            }
            String term = StringCell.class.cast(keyCell).getStringValue();
            if (term == null || term.length() == 0) continue;
            StringCell termAsCell = new StringCell(term);
            QueryKeyHandler.FetchSet handler = new QueryKeyHandler.FetchSet();
            URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=" + URLEncoder.encode(m_database.getStringValue(), "UTF-8") + "&term=" + URLEncoder.encode(term, "UTF-8") + "&retstart=" + m_start.getIntValue() + "&retmax=" + m_limit.getIntValue() + "&retmode=xml" + "&email=" + URLEncoder.encode(Me.MAIL, "UTF-8") + "&tool=knime");
            LOGGER.info("Calling " + url);
            InputStream in = url.openStream();
            parser.parse(in, handler);
            in.close();
            for (Integer id : handler.getPMID()) {
                LOGGER.info("found id=" + id);
                RowKey rowKey = RowKey.createRowKey(++rowIndex);
                DataCell cells[] = new DataCell[] { termAsCell, dbAsCell, new IntCell(id) };
                container.addRowToTable(new DefaultRow(rowKey, cells));
            }
            exec.checkCanceled();
            exec.setProgress(rowCount / (double) rowCountInput, "Adding row " + rowIndex);
        }
        container.close();
        return new BufferedDataTable[] { container.getTable() };
    }
