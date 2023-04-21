    protected Document exportPairs(final File f) {
        if (f.exists()) {
            final int nRes = JOptionPane.showConfirmDialog(this, "File already exists - overwrite ?", "Overwrite confirmation", JOptionPane.YES_NO_OPTION);
            if (nRes != JOptionPane.YES_OPTION) return null;
        }
        final Collection<? extends Map.Entry<? extends File, ? extends File>> pl = (null == _pairsList) ? null : _pairsList.getFilePairs(false);
        if ((null == pl) || (pl.size() <= 0)) {
            JOptionPane.showMessageDialog(this, "No pair(s) available", "No pair(s) available", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            final Document doc = createExportDocument(pl);
            final Source src = new DOMSource(doc);
            final Transformer t = DOMUtils.getDefaultXmlTransformer();
            OutputStream out = null;
            try {
                out = new FileOutputStream(f);
                t.transform(src, new StreamResult(out));
                return doc;
            } finally {
                FileUtil.closeAll(out);
            }
        } catch (Exception e) {
            _logger.error("exportPairs(" + f + ") " + e.getClass().getName() + ": " + e.getMessage(), e);
            BaseOptionPane.showMessageDialog(this, e);
            return null;
        }
    }
