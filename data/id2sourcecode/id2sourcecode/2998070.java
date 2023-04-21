    private long transformFiles(final File srcFile, final File dstFile) {
        if (srcFile.isDirectory() || dstFile.isDirectory()) throw new IllegalStateException("Source/Destination is a directory");
        if ((srcFile == dstFile) || srcFile.equals(dstFile)) {
            final int nRes = JOptionPane.showConfirmDialog(getMainFrame(), "Target file= " + dstFile.getAbsolutePath() + " already exists - overwrite ?", "Target overwrite confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (nRes != JOptionPane.YES_OPTION) {
                if (JOptionPane.CANCEL_OPTION == nRes) return Long.MIN_VALUE;
                return (-1L);
            }
            if (_logger.isDebugEnabled()) _logger.debug("transformFiles(" + srcFile + ")");
        } else {
            if (_logger.isDebugEnabled()) _logger.debug("transformFiles(" + srcFile + ") => " + dstFile);
        }
        final Document doc;
        final long xStart = System.currentTimeMillis();
        try {
            final DocumentBuilderFactory docFactory = getDocumentsFactory();
            final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            if (null == (doc = docBuilder.parse(srcFile))) throw new IllegalStateException("No " + Document.class.getSimpleName() + " parsed");
        } catch (Exception e) {
            final long xEnd = System.currentTimeMillis(), xDuration = xEnd - xStart;
            _logger.error("transformFiles(" + srcFile + ") => (" + dstFile + ") " + e.getClass().getName() + " while loading source file: " + e.getMessage());
            JOptionPane.showMessageDialog(getMainFrame(), "Failed to parse input file: " + e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            return (0L - xDuration - 1L);
        }
        try {
            Arranger.ARRANGER.transform(doc, dstFile);
        } catch (Exception e) {
            final long xEnd = System.currentTimeMillis(), xDuration = xEnd - xStart;
            _logger.error("transformFiles(" + srcFile + ") => (" + dstFile + ") " + e.getClass().getName() + " while transforming: " + e.getMessage());
            JOptionPane.showMessageDialog(getMainFrame(), "Failed to transform: " + e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            return (0L - xDuration - 1L);
        }
        final long xEnd = System.currentTimeMillis(), xDuration = xEnd - xStart;
        if (_logger.isDebugEnabled()) _logger.debug("transformFiles(" + srcFile + ") => (" + dstFile + ") completed in " + xDuration + " msec.");
        JOptionPane.showMessageDialog(getMainFrame(), "Execution completed in " + xDuration + " msec.", "Done", JOptionPane.INFORMATION_MESSAGE);
        return xDuration;
    }
