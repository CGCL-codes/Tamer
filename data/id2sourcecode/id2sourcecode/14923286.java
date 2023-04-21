    public int showSaveDialog(Component parent) {
        m_DialogType = SAVER_DIALOG;
        m_CurrentConverter = null;
        initGUI(SAVER_DIALOG);
        boolean acceptAll = isAcceptAllFileFilterUsed();
        FileFilter currentFilter = getFileFilter();
        File currentFile = getSelectedFile();
        setAcceptAllFileFilterUsed(false);
        setFileFilter(currentFilter);
        setSelectedFile(currentFile);
        int result = super.showSaveDialog(parent);
        if (result == APPROVE_OPTION) {
            if (getFileFilter() instanceof ExtensionFileFilter) {
                String filename = getSelectedFile().getAbsolutePath();
                String[] extensions = ((ExtensionFileFilter) getFileFilter()).getExtensions();
                if (!filename.endsWith(extensions[0])) {
                    filename += extensions[0];
                    setSelectedFile(new File(filename));
                }
            }
        }
        currentFilter = getFileFilter();
        currentFile = getSelectedFile();
        setAcceptAllFileFilterUsed(acceptAll);
        setFileFilter(currentFilter);
        setSelectedFile(currentFile);
        m_DialogType = UNHANDLED_DIALOG;
        removePropertyChangeListener(m_Listener);
        if ((result == APPROVE_OPTION) && (getOverwriteWarning()) && (getSelectedFile().exists())) {
            int retVal = JOptionPane.showConfirmDialog(parent, "The file '" + getSelectedFile() + "' already exists - overwrite it?");
            if (retVal == JOptionPane.OK_OPTION) result = APPROVE_OPTION; else if (retVal == JOptionPane.NO_OPTION) result = showSaveDialog(parent); else result = CANCEL_OPTION;
        }
        if (result == APPROVE_OPTION) {
            m_LastFilter = getFileFilter();
            configureCurrentConverter(SAVER_DIALOG);
            if (m_CheckBoxOptions.isSelected()) {
                m_EditorResult = JFileChooser.CANCEL_OPTION;
                m_Editor.setValue(m_CurrentConverter);
                PropertyDialog pd;
                if (PropertyDialog.getParentDialog(this) != null) pd = new PropertyDialog(PropertyDialog.getParentDialog(this), m_Editor); else pd = new PropertyDialog(PropertyDialog.getParentFrame(this), m_Editor);
                pd.setVisible(true);
                result = m_EditorResult;
            }
        }
        return result;
    }
