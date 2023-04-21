    @Override
    public void saveFile(File f, Element dlgElement) {
        if (null == f) return;
        if (f.exists()) {
            final int nRes = JOptionPane.showConfirmDialog(this, "File already exists - overwrite ?", "Confirm file override", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (nRes != JOptionPane.YES_OPTION) return;
        }
        try {
            PrettyPrintTransformer.DEFAULT.transform(_docTree.getDocument(), f);
        } catch (Exception e) {
            getLogger().error("saveFile(" + f + ") " + e.getClass().getName() + ": " + e.getMessage());
            BaseOptionPane.showMessageDialog(this, e);
        }
    }
