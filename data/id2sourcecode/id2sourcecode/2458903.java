    public boolean createEntities(int C_Country_ID, String City, int C_Region_ID, int C_Currency_ID) {
        if (m_as == null) {
            log.severe("No AcctountingSChema");
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        log.info("C_Country_ID=" + C_Country_ID + ", City=" + City + ", C_Region_ID=" + C_Region_ID);
        m_info.append("\n----\n");
        String defaultName = Msg.translate(m_lang, "Standard");
        String defaultEntry = "'" + defaultName + "',";
        StringBuffer sqlCmd = null;
        int no = 0;
        int C_Channel_ID = getNextID(getAD_Client_ID(), "C_Channel");
        sqlCmd = new StringBuffer("INSERT INTO C_Channel ");
        sqlCmd.append("(C_Channel_ID,Name,");
        sqlCmd.append(m_stdColumns).append(") VALUES (");
        sqlCmd.append(C_Channel_ID).append(",").append(defaultEntry);
        sqlCmd.append(m_stdValues).append(")");
        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
        if (no != 1) {
            log.log(Level.SEVERE, "Channel NOT inserted");
        }
        int C_SalesRegion_ID = getNextID(getAD_Client_ID(), "C_SalesRegion");
        sqlCmd = new StringBuffer("INSERT INTO C_SalesRegion ");
        sqlCmd.append("(C_SalesRegion_ID,").append(m_stdColumns).append(",");
        sqlCmd.append(" Value,Name,IsSummary) VALUES (");
        sqlCmd.append(C_SalesRegion_ID).append(",").append(m_stdValues).append(", ");
        sqlCmd.append(defaultEntry).append(defaultEntry).append("'N')");
        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
        if (no == 1) {
            m_info.append(Msg.translate(m_lang, "C_SalesRegion_ID")).append("=").append(defaultName).append("\n");
        } else {
            log.log(Level.SEVERE, "SalesRegion NOT inserted");
        }
        if (m_hasSRegion) {
            sqlCmd = new StringBuffer("UPDATE C_AcctSchema_Element SET ");
            sqlCmd.append("C_SalesRegion_ID=").append(C_SalesRegion_ID);
            sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as.getC_AcctSchema_ID());
            sqlCmd.append(" AND ElementType='SR'");
            no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
            if (no != 1) {
                log.log(Level.SEVERE, "AcctSchema ELement SalesRegion NOT updated");
            }
        }
        MBPGroup bpg = new MBPGroup(m_ctx, 0, m_trx.getTrxName());
        bpg.setValue(defaultName);
        bpg.setName(defaultName);
        bpg.setIsDefault(true);
        if (bpg.save()) {
            m_info.append(Msg.translate(m_lang, "C_BP_Group_ID")).append("=").append(defaultName).append("\n");
            Env.setContext(m_ctx, "#C_BP_Group_ID", bpg.getID());
        } else {
            log.log(Level.SEVERE, "BP Group NOT inserted");
        }
        MBPartner bp = new MBPartner(m_ctx, 0, m_trx.getTrxName());
        bp.setValue(defaultName);
        bp.setName(defaultName);
        bp.setC_BP_Group_ID(bpg.getC_BP_Group_ID());
        bp.setIsCustomer(true);
        bp.setIsVendor(true);
        if (bp.save()) {
            m_info.append(Msg.translate(m_lang, "C_BPartner_ID")).append("=").append(defaultName).append("\n");
        } else {
            log.log(Level.SEVERE, "BPartner NOT inserted");
        }
        MLocation bpLoc = new MLocation(m_ctx, C_Country_ID, C_Region_ID, City, m_trx.getTrxName());
        bpLoc.save();
        MBPartnerLocation bpl = new MBPartnerLocation(bp);
        bpl.setC_Location_ID(bpLoc.getC_Location_ID());
        if (!bpl.save()) {
            log.log(Level.SEVERE, "BP_Location (Standard) NOT inserted");
        }
        sqlCmd = new StringBuffer("UPDATE C_AcctSchema_Element SET ");
        sqlCmd.append("C_BPartner_ID=").append(bp.getC_BPartner_ID());
        sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as.getC_AcctSchema_ID());
        sqlCmd.append(" AND ElementType='BP'");
        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
        if (no != 1) {
            log.log(Level.SEVERE, "AcctSchema Element BPartner NOT updated");
        }
        createPreference("C_BPartner_ID", String.valueOf(bp.getC_BPartner_ID()), 143);
        if (!this.createData(C_Country_ID, C_Region_ID, City, defaultEntry, defaultName, bpg, bp, C_Currency_ID)) {
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        C_Cycle_ID = getNextID(getAD_Client_ID(), "C_Cycle");
        sqlCmd = new StringBuffer("INSERT INTO C_Cycle ");
        sqlCmd.append("(C_Cycle_ID,").append(m_stdColumns).append(",");
        sqlCmd.append(" Name,C_Currency_ID) VALUES (");
        sqlCmd.append(C_Cycle_ID).append(",").append(m_stdValues).append(", ");
        sqlCmd.append(defaultEntry).append(C_Currency_ID).append(")");
        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
        if (no != 1) {
            log.log(Level.SEVERE, "Cycle NOT inserted");
        }
        int C_Campaign_ID = getNextID(getAD_Client_ID(), "C_Campaign");
        sqlCmd = new StringBuffer("INSERT INTO C_Campaign ");
        sqlCmd.append("(C_Campaign_ID,C_Channel_ID,").append(m_stdColumns).append(",");
        sqlCmd.append(" Value,Name,Costs) VALUES (");
        sqlCmd.append(C_Campaign_ID).append(",").append(C_Channel_ID).append(",").append(m_stdValues).append(",");
        sqlCmd.append(defaultEntry).append(defaultEntry).append("0)");
        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
        if (no == 1) {
            m_info.append(Msg.translate(m_lang, "C_Campaign_ID")).append("=").append(defaultName).append("\n");
        } else {
            log.log(Level.SEVERE, "Campaign NOT inserted");
        }
        if (m_hasMCampaign) {
            sqlCmd = new StringBuffer("UPDATE C_AcctSchema_Element SET ");
            sqlCmd.append("C_Campaign_ID=").append(C_Campaign_ID);
            sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as.getC_AcctSchema_ID());
            sqlCmd.append(" AND ElementType='MC'");
            no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
            if (no != 1) {
                log.log(Level.SEVERE, "AcctSchema ELement Campaign NOT updated");
            }
        }
        int C_Project_ID = getNextID(getAD_Client_ID(), "C_Project");
        sqlCmd = new StringBuffer("INSERT INTO C_Project ");
        sqlCmd.append("(C_Project_ID,").append(m_stdColumns).append(",");
        sqlCmd.append(" Value,Name,C_Currency_ID,IsSummary) VALUES (");
        sqlCmd.append(C_Project_ID).append(",").append(getM_stdValuesOrg()).append(", ");
        sqlCmd.append(defaultEntry).append(defaultEntry).append(C_Currency_ID).append(",'N')");
        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
        if (no == 1) {
            m_info.append(Msg.translate(m_lang, "C_Project_ID")).append("=").append(defaultName).append("\n");
        } else {
            log.log(Level.SEVERE, "Project NOT inserted");
        }
        if (m_hasProject) {
            sqlCmd = new StringBuffer("UPDATE C_AcctSchema_Element SET ");
            sqlCmd.append("C_Project_ID=").append(C_Project_ID);
            sqlCmd.append(" WHERE C_AcctSchema_ID=").append(m_as.getC_AcctSchema_ID());
            sqlCmd.append(" AND ElementType='PJ'");
            no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
            if (no != 1) {
                log.log(Level.SEVERE, "AcctSchema ELement Project NOT updated");
            }
        }
        MCashBook cb = new MCashBook(m_ctx, 0, m_trx.getTrxName());
        cb.setName(defaultName);
        cb.setC_Currency_ID(C_Currency_ID);
        if (cb.save()) {
            m_info.append(Msg.translate(m_lang, "C_CashBook_ID")).append("=").append(defaultName).append("\n");
        } else {
            log.log(Level.SEVERE, "CashBook NOT inserted");
        }
        if (!createLocaleEntities()) {
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        this.commitAndCloseTrx();
        return true;
    }
