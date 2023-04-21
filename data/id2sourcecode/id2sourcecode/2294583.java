    protected String doIt() throws Exception {
        m_C_Project_ID = getRecord_ID();
        log.info("doIt - C_Project_ID=" + m_C_Project_ID + ", C_ProjectType_ID=" + m_C_ProjectType_ID);
        MProject project = new MProject(getCtx(), m_C_Project_ID, get_TrxName());
        if ((project.getC_Project_ID() == 0) || (project.getC_Project_ID() != m_C_Project_ID)) {
            throw new IllegalArgumentException("Project not found C_Project_ID=" + m_C_Project_ID);
        }
        if (project.getC_ProjectType_ID_Int() > 0) {
            throw new IllegalArgumentException("Project already has Type (Cannot overwrite) " + project.getC_ProjectType_ID());
        }
        MProjectType type = new MProjectType(getCtx(), m_C_ProjectType_ID, get_TrxName());
        if ((type.getC_ProjectType_ID() == 0) || (type.getC_ProjectType_ID() != m_C_ProjectType_ID)) {
            throw new IllegalArgumentException("Project Type not found C_ProjectType_ID=" + m_C_ProjectType_ID);
        }
        project.setProjectType(type);
        if (!project.save()) {
            throw new Exception("@Error@");
        }
        return "@OK@";
    }
