    public void updateFields() {
        try {
            HashMap par = new HashMap();
            par.put("ID", m_sID);
            par.put("PID", m_sParentID);
            par.put("VER", m_sVersion);
            par.put("ATTR", m_attrs);
            par.put("TYPE", m_sTypeID);
            par.put("NAME", m_sName);
            par.put("NUM", m_sNumber);
            if (m_sDocFile != null) {
                FileInputStream fp = new FileInputStream(m_sDocFile);
                byte bFileData[] = new byte[(int) fp.getChannel().size()];
                fp.read(bFileData);
                fp.close();
                par.put("DATA", bFileData);
                par.put("EXT", m_sDocFile.substring(m_sDocFile.lastIndexOf('.'), m_sDocFile.length()));
            }
            Any param = ClientMain.getInstance().createAny();
            param.insert_Value(par);
            ClientMain.getInstance().sessionRequest("setDocAttr", param);
            m_method.invoke(m_object);
            dispose();
        } catch (Exception ex) {
            Errors.showError(ex);
        }
    }
