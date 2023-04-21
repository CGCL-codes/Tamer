    private void writeSpecialFormat(ExampleSet exampleSet, File dataFile, int fractionDigits, boolean automaticLineBreak, boolean quoteNominal, boolean zipped, boolean append, Charset encoding) throws OperatorException {
        String format = getParameterAsString(PARAMETER_SPECIAL_FORMAT);
        if (format == null) throw new UserError(this, 201, new Object[] { "special_format", "format", "special_format" });
        ExampleFormatter formatter;
        try {
            formatter = ExampleFormatter.compile(format, exampleSet, fractionDigits, quoteNominal);
        } catch (FormatterException e) {
            throw new UserError(this, 901, format, e.getMessage());
        }
        OutputStream out = null;
        PrintWriter writer = null;
        try {
            if (zipped) {
                out = new GZIPOutputStream(new FileOutputStream(dataFile, append));
            } else {
                out = new FileOutputStream(dataFile, append);
            }
            writer = new PrintWriter(new OutputStreamWriter(out, encoding));
            Iterator<Example> reader = exampleSet.iterator();
            while (reader.hasNext()) {
                if (automaticLineBreak) writer.println(formatter.format(reader.next())); else writer.print(formatter.format(reader.next()));
            }
        } catch (IOException e) {
            throw new UserError(this, 303, dataFile, e.getMessage());
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    getLogger().log(Level.WARNING, "Cannot close stream to file " + dataFile, e);
                }
            }
        }
    }
