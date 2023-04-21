    @Override
    public void saveFile(final File tgtFile, final Element dlgElement) {
        final String filePath = (null == tgtFile) ? null : tgtFile.getAbsolutePath();
        if ((null == filePath) || (filePath.length() <= 0)) return;
        try {
            final Document doc = (null == _dstPanel) ? null : _dstPanel.getDocument();
            if (null == doc) return;
            if (tgtFile.exists()) {
                if (JOptionPane.showConfirmDialog(this, "Overwrite existing file ?", "File already exist", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            }
            final Transformer t = getSavedFileTransformer(tgtFile);
            OutputStream out = null;
            try {
                out = new FileOutputStream(tgtFile);
                t.transform(new DOMSource(doc), new StreamResult(out));
            } finally {
                FileUtil.closeAll(out);
            }
            _dstPanel.setFilePath(filePath);
            JOptionPane.showMessageDialog(this, "File successfully written", filePath, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            getLogger().error("saveFile(" + filePath + ") " + e.getClass().getName() + ": " + e.getMessage(), e);
            BaseOptionPane.showMessageDialog(this, e);
        }
    }
