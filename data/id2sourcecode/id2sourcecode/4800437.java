    protected void executeConcatenation() {
        try {
            if (_worker != null) throw new IllegalStateException("Already running");
            final String outFile = getOutputFile();
            final File oFile = ((null == outFile) || (outFile.length() <= 0)) ? null : new File(outFile);
            if ((oFile != null) && oFile.exists()) {
                final int nRes = JOptionPane.showConfirmDialog(this, "File already exists - overwrite ?", "Overwrite confirmation", JOptionPane.YES_NO_OPTION);
                if (nRes != JOptionPane.YES_OPTION) return;
            }
            _worker = new PDFConcatenator(this, (null == _tbl) ? null : _tbl.getInputFiles(), outFile);
            updateTrackedButtons("", false);
            _worker.execute();
        } catch (Exception e) {
            _logger.error("executeConcatenation()" + e.getClass().getName() + ": " + e.getMessage(), e);
            BaseOptionPane.showMessageDialog(this, e);
        }
    }
