    public boolean doSaveAs() throws FileNotFoundException, IOException, FileManager.Exception {
        boolean bSave = false;
        File fileDefault = m_file;
        if (fileDefault == null) {
            String strDir = System.getProperty("user.home");
            fileDefault = new File(strDir, m_strDefaultDocument);
        }
        JFileChooser dlg = new JFileChooser();
        dlg.setFileFilter(new ExtFilter(m_strExtDescription, m_strExt));
        dlg.setCurrentDirectory(fileDefault);
        boolean bUserRetry = true;
        while (bUserRetry) {
            int nResult = dlg.showDialog(m_comParent, "Save As");
            if (nResult == JFileChooser.APPROVE_OPTION) {
                m_file = dlg.getSelectedFile();
                if (m_file.getName().endsWith(m_strExt) == false) m_file = new File(m_file.getAbsolutePath() + m_strExt);
                if (m_file.exists()) {
                    int nResponse = JOptionPane.showConfirmDialog(m_comParent, "The file " + m_file.getName() + " already exists. Do you want to over-write it?", "DbCoder", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    switch(nResponse) {
                        case JOptionPane.YES_OPTION:
                            bSave = true;
                            bUserRetry = false;
                            break;
                        case JOptionPane.NO_OPTION:
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            bUserRetry = false;
                            break;
                    }
                } else {
                    bSave = true;
                    bUserRetry = false;
                }
                if (bSave) {
                    if (m_mruMenu != null) m_mruMenu.update(m_file);
                    save(m_file);
                    bSave = true;
                    m_bChanged = false;
                }
            } else bUserRetry = false;
        }
        return bSave;
    }
