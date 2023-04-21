    private void open() throws OperatorException {
        getErrors().clear();
        close();
        InputStream in;
        try {
            URL url = new URL(configuration.getCsvFile());
            try {
                in = url.openStream();
            } catch (IOException e) {
                throw new UserError(operator, 301, e, configuration.getCsvFile());
            }
        } catch (MalformedURLException e) {
            try {
                in = new FileInputStream(configuration.getCsvFile());
            } catch (FileNotFoundException e1) {
                throw new UserError(operator, 301, e1, configuration.getCsvFile());
            }
        }
        reader = new LineReader(in, configuration.getEncoding());
        parser = new LineParser(configuration);
        try {
            readNext();
        } catch (IOException e) {
            throw new UserError(operator, e, 321, configuration.getCsvFile(), e.toString());
        }
        if (next == null) {
            errors.add(new ParsingError(1, -1, ErrorCode.FILE_SYNTAX_ERROR, "No valid line found."));
            columnNames = new String[0];
            valueTypes = new int[0];
        } else {
            columnNames = new String[next.length + 1];
            for (int i = 0; i < next.length + 1; i++) {
                columnNames[i] = "att" + (i + 1);
            }
            valueTypes = new int[next.length + 1];
            Arrays.fill(valueTypes, Ontology.NOMINAL);
            currentRow = -1;
            batch = -1;
        }
    }
