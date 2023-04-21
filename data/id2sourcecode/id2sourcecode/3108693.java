    public RuntimeException createDependencies(final Collection<? extends RepositoryEntry> rl, final boolean asAntFormat, final File outFile) {
        if (null == outFile) return new IllegalStateException("No output file");
        if ((null == rl) || (rl.size() <= 0)) return new IllegalArgumentException("No entries to create");
        if (outFile.exists()) {
            if (JOptionPane.showConfirmDialog(this, "Output file already exists - override ?", "Confirm output file overwrite", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return null;
        }
        final Document doc;
        try {
            doc = DOMUtils.createDefaultDocument();
            if (asAntFormat) createAntDependencies(doc, rl, outFile); else createMavenDependencies(doc, rl, outFile);
        } catch (Exception e) {
            _logger.error("createDependencies(" + outFile + ") " + e.getClass().getName() + " while create document: " + e.getMessage(), e);
            return ExceptionUtil.toRuntimeException(e);
        }
        try {
            PrettyPrintTransformer.DEFAULT.transform(doc, outFile);
        } catch (Exception e) {
            _logger.error("createDependencies(" + outFile + ") " + e.getClass().getName() + " while write document: " + e.getMessage(), e);
            return ExceptionUtil.toRuntimeException(e);
        }
        {
            final int nRes = JOptionPane.showConfirmDialog(this, "Open generated file ?", "Conversion complete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (JOptionPane.YES_OPTION == nRes) {
                final Desktop d = Desktop.getDesktop();
                try {
                    d.edit(outFile);
                } catch (IOException e) {
                    _logger.error("createDependencies(" + outFile + ") " + e.getClass().getName() + " while edit output file: " + e.getMessage(), e);
                    return ExceptionUtil.toRuntimeException(e);
                }
            }
        }
        return null;
    }
