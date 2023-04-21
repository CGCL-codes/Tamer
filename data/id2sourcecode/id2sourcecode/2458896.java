    public boolean createAccounting(KeyNamePair currency, boolean hasProduct, boolean hasBPartner, boolean hasProject, boolean hasMCampaign, boolean hasSRegion, File AccountingFile) {
        log.info(m_client.toString());
        m_hasProject = hasProject;
        m_hasMCampaign = hasMCampaign;
        m_hasSRegion = hasSRegion;
        m_info = new StringBuffer();
        String name = null;
        StringBuffer sqlCmd = null;
        int no = 0;
        m_calendar = new MCalendar(m_client);
        if (!m_calendar.save()) {
            String err = "Calendar NOT inserted";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        m_info.append(Msg.translate(m_lang, "C_Calendar_ID")).append("=").append(m_calendar.getName()).append("\n");
        m_currentYear = m_calendar.createYear(m_client.getLocale());
        if (m_currentYear == null) {
            log.log(Level.SEVERE, "Year NOT inserted");
        }
        name = m_clientName + " " + Msg.translate(m_lang, "Account_ID");
        MElement element = new MElement(m_client, name, MElement.ELEMENTTYPE_Account, m_AD_Tree_Account_ID);
        if (!element.save()) {
            String err = "Acct Element NOT inserted";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        int C_Element_ID = element.getC_Element_ID();
        m_info.append(Msg.translate(m_lang, "C_Element_ID")).append("=").append(name).append("\n");
        m_nap = new NaturalAccountMap(m_ctx, m_trx.getTrxName());
        String errMsg = m_nap.parseFile(AccountingFile);
        if (errMsg.length() != 0) {
            log.log(Level.SEVERE, errMsg);
            m_info.append(errMsg);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        if (m_nap.saveAccounts(getAD_Client_ID(), getAD_Org_ID(), C_Element_ID)) {
            m_info.append(Msg.translate(m_lang, "C_ElementValue_ID")).append(" # ").append(m_nap.size()).append("\n");
        } else {
            String err = "Acct Element Values NOT inserted";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        int C_ElementValue_ID = m_nap.getC_ElementValue_ID("DEFAULT_ACCT");
        log.fine("C_ElementValue_ID=" + C_ElementValue_ID);
        m_as = new MAcctSchema(m_client, currency);
        if (!m_as.save()) {
            String err = "AcctSchema NOT inserted";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        m_info.append(Msg.translate(m_lang, "C_AcctSchema_ID")).append("=").append(m_as.getName()).append("\n");
        String sql2 = null;
        if (Env.isBaseLanguage(m_lang, "AD_Reference")) {
            sql2 = "SELECT Value, Name FROM AD_Ref_List WHERE AD_Reference_ID=181";
        } else {
            sql2 = "SELECT l.Value, COALESCE(t.Name,l.Name) FROM AD_Ref_List l LEFT JOIN AD_Ref_List_Trl t on (l.AD_Ref_List_ID=t.AD_Ref_List_ID AND AD_Language = ? ) WHERE l.AD_Reference_ID=181 ";
        }
        int Element_OO = 0, Element_AC = 0, Element_PR = 0, Element_BP = 0, Element_PJ = 0, Element_MC = 0, Element_SR = 0;
        try {
            int AD_Client_ID = m_client.getAD_Client_ID();
            PreparedStatement stmt = DB.prepareStatement(sql2, m_trx.getTrxName());
            stmt.setString(1, m_lang);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ElementType = rs.getString(1);
                name = rs.getString(2);
                String IsMandatory = null;
                String IsBalanced = "N";
                int SeqNo = 0;
                int C_AcctSchema_Element_ID = 0;
                if (ElementType.equals("OO")) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    Element_OO = C_AcctSchema_Element_ID;
                    IsMandatory = "Y";
                    IsBalanced = "Y";
                    SeqNo = 10;
                } else if (ElementType.equals("AC")) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    Element_AC = C_AcctSchema_Element_ID;
                    IsMandatory = "Y";
                    SeqNo = 20;
                } else if (ElementType.equals("PR") && hasProduct) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    Element_PR = C_AcctSchema_Element_ID;
                    IsMandatory = "N";
                    SeqNo = 30;
                } else if (ElementType.equals("BP") && hasBPartner) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    Element_BP = C_AcctSchema_Element_ID;
                    IsMandatory = "N";
                    SeqNo = 40;
                } else if (ElementType.equals("PJ") && hasProject) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    Element_PJ = C_AcctSchema_Element_ID;
                    IsMandatory = "N";
                    SeqNo = 50;
                } else if (ElementType.equals("MC") && hasMCampaign) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    Element_MC = C_AcctSchema_Element_ID;
                    IsMandatory = "N";
                    SeqNo = 60;
                } else if (ElementType.equals("SR") && hasSRegion) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    Element_SR = C_AcctSchema_Element_ID;
                    IsMandatory = "N";
                    SeqNo = 70;
                }
                if (IsMandatory != null) {
                    sqlCmd = new StringBuffer("INSERT INTO C_AcctSchema_Element(");
                    sqlCmd.append(m_stdColumns).append(",C_AcctSchema_Element_ID,C_AcctSchema_ID,").append("ElementType,Name,SeqNo,IsMandatory,IsBalanced) VALUES (");
                    sqlCmd.append(m_stdValues).append(",").append(C_AcctSchema_Element_ID).append(",").append(m_as.getC_AcctSchema_ID()).append(",").append("'").append(ElementType).append("','").append(name).append("',").append(SeqNo).append(",'").append(IsMandatory).append("','").append(IsBalanced).append("')");
                    no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
                    if (no == 1) {
                        m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\n");
                    }
                    if (ElementType.equals("OO")) {
                        sqlCmd = new StringBuffer("UPDATE C_AcctSchema_Element SET Org_ID=");
                        sqlCmd.append(getAD_Org_ID()).append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
                        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
                        if (no != 1) {
                            log.log(Level.SEVERE, "Default Org in AcctSchamaElement NOT updated");
                        }
                    }
                    if (ElementType.equals("AC")) {
                        sqlCmd = new StringBuffer("UPDATE C_AcctSchema_Element SET C_ElementValue_ID=");
                        sqlCmd.append(C_ElementValue_ID).append(", C_Element_ID=").append(C_Element_ID);
                        sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
                        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
                        if (no != 1) {
                            log.log(Level.SEVERE, "Default Account in AcctSchamaElement NOT updated");
                        }
                    }
                }
            }
            rs.close();
            stmt.close();
        } catch (SQLException e1) {
            log.log(Level.SEVERE, "Elements", e1);
            m_info.append(e1.getMessage());
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        sqlCmd = new StringBuffer("INSERT INTO C_ACCTSCHEMA_GL (");
        sqlCmd.append(m_stdColumns).append(",C_ACCTSCHEMA_ID," + "USESUSPENSEBALANCING,SUSPENSEBALANCING_ACCT," + "USESUSPENSEERROR,SUSPENSEERROR_ACCT," + "USECURRENCYBALANCING,CURRENCYBALANCING_ACCT," + "RETAINEDEARNING_ACCT,INCOMESUMMARY_ACCT," + "INTERCOMPANYDUETO_ACCT,INTERCOMPANYDUEFROM_ACCT," + "PPVOFFSET_ACCT) VALUES (");
        sqlCmd.append(m_stdValues).append(",").append(m_as.getC_AcctSchema_ID()).append(",");
        sqlCmd.append("'Y',").append(getAcct("SUSPENSEBALANCING_ACCT")).append(",");
        sqlCmd.append("'Y',").append(getAcct("SUSPENSEERROR_ACCT")).append(",");
        sqlCmd.append("'Y',").append(getAcct("CURRENCYBALANCING_ACCT")).append(",");
        sqlCmd.append(getAcct("RETAINEDEARNING_ACCT")).append(",");
        sqlCmd.append(getAcct("INCOMESUMMARY_ACCT")).append(",");
        sqlCmd.append(getAcct("INTERCOMPANYDUETO_ACCT")).append(",");
        sqlCmd.append(getAcct("INTERCOMPANYDUEFROM_ACCT")).append(",");
        sqlCmd.append(getAcct("PPVOFFSET_ACCT")).append(")");
        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
        if (no != 1) {
            String err = "GL Accounts NOT inserted";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        sqlCmd = new StringBuffer("INSERT INTO C_ACCTSCHEMA_DEFAULT (");
        sqlCmd.append(m_stdColumns).append(",C_ACCTSCHEMA_ID," + "W_INVENTORY_ACCT,W_DIFFERENCES_ACCT,W_REVALUATION_ACCT,W_INVACTUALADJUST_ACCT, " + "P_REVENUE_ACCT,P_EXPENSE_ACCT,P_ASSET_ACCT,P_COGS_ACCT, " + "P_PURCHASEPRICEVARIANCE_ACCT,P_INVOICEPRICEVARIANCE_ACCT,P_TRADEDISCOUNTREC_ACCT,P_TRADEDISCOUNTGRANT_ACCT, " + "C_RECEIVABLE_ACCT,C_PREPAYMENT_ACCT, " + "V_LIABILITY_ACCT,V_LIABILITY_SERVICES_ACCT,V_PREPAYMENT_ACCT, " + "PAYDISCOUNT_EXP_ACCT,PAYDISCOUNT_REV_ACCT,WRITEOFF_ACCT, " + "UNREALIZEDGAIN_ACCT,UNREALIZEDLOSS_ACCT,REALIZEDGAIN_ACCT,REALIZEDLOSS_ACCT, " + "WITHHOLDING_ACCT,E_PREPAYMENT_ACCT,E_EXPENSE_ACCT, " + "PJ_ASSET_ACCT,PJ_WIP_ACCT," + "T_EXPENSE_ACCT,T_LIABILITY_ACCT,T_RECEIVABLES_ACCT,T_DUE_ACCT,T_CREDIT_ACCT, " + "B_INTRANSIT_ACCT,B_ASSET_ACCT,B_EXPENSE_ACCT,B_INTERESTREV_ACCT,B_INTERESTEXP_ACCT," + "B_UNIDENTIFIED_ACCT,B_SETTLEMENTGAIN_ACCT,B_SETTLEMENTLOSS_ACCT," + "B_REVALUATIONGAIN_ACCT,B_REVALUATIONLOSS_ACCT,B_PAYMENTSELECT_ACCT,B_UNALLOCATEDCASH_ACCT, " + "CH_EXPENSE_ACCT,CH_REVENUE_ACCT, " + "UNEARNEDREVENUE_ACCT,NOTINVOICEDRECEIVABLES_ACCT,NOTINVOICEDREVENUE_ACCT,NOTINVOICEDRECEIPTS_ACCT, " + "CB_ASSET_ACCT,CB_CASHTRANSFER_ACCT,CB_DIFFERENCES_ACCT,CB_EXPENSE_ACCT,CB_RECEIPT_ACCT) VALUES (");
        sqlCmd.append(m_stdValues).append(",").append(m_as.getC_AcctSchema_ID()).append(",");
        sqlCmd.append(getAcct("W_INVENTORY_ACCT")).append(",");
        sqlCmd.append(getAcct("W_DIFFERENCES_ACCT")).append(",");
        sqlCmd.append(getAcct("W_REVALUATION_ACCT")).append(",");
        sqlCmd.append(getAcct("W_INVACTUALADJUST_ACCT")).append(", ");
        sqlCmd.append(getAcct("P_REVENUE_ACCT")).append(",");
        sqlCmd.append(getAcct("P_EXPENSE_ACCT")).append(",");
        sqlCmd.append(getAcct("P_ASSET_ACCT")).append(",");
        sqlCmd.append(getAcct("P_COGS_ACCT")).append(", ");
        sqlCmd.append(getAcct("P_PURCHASEPRICEVARIANCE_ACCT")).append(",");
        sqlCmd.append(getAcct("P_INVOICEPRICEVARIANCE_ACCT")).append(",");
        sqlCmd.append(getAcct("P_TRADEDISCOUNTREC_ACCT")).append(",");
        sqlCmd.append(getAcct("P_TRADEDISCOUNTGRANT_ACCT")).append(", ");
        sqlCmd.append(getAcct("C_RECEIVABLE_ACCT")).append(",");
        sqlCmd.append(getAcct("C_PREPAYMENT_ACCT")).append(", ");
        sqlCmd.append(getAcct("V_LIABILITY_ACCT")).append(",");
        sqlCmd.append(getAcct("V_LIABILITY_SERVICES_ACCT")).append(",");
        sqlCmd.append(getAcct("V_PREPAYMENT_ACCT")).append(", ");
        sqlCmd.append(getAcct("PAYDISCOUNT_EXP_ACCT")).append(",");
        sqlCmd.append(getAcct("PAYDISCOUNT_REV_ACCT")).append(",");
        sqlCmd.append(getAcct("WRITEOFF_ACCT")).append(", ");
        sqlCmd.append(getAcct("UNREALIZEDGAIN_ACCT")).append(",");
        sqlCmd.append(getAcct("UNREALIZEDLOSS_ACCT")).append(",");
        sqlCmd.append(getAcct("REALIZEDGAIN_ACCT")).append(",");
        sqlCmd.append(getAcct("REALIZEDLOSS_ACCT")).append(", ");
        sqlCmd.append(getAcct("WITHHOLDING_ACCT")).append(",");
        sqlCmd.append(getAcct("E_PREPAYMENT_ACCT")).append(",");
        sqlCmd.append(getAcct("E_EXPENSE_ACCT")).append(", ");
        sqlCmd.append(getAcct("PJ_ASSET_ACCT")).append(",");
        sqlCmd.append(getAcct("PJ_WIP_ACCT")).append(",");
        sqlCmd.append(getAcct("T_EXPENSE_ACCT")).append(",");
        sqlCmd.append(getAcct("T_LIABILITY_ACCT")).append(",");
        sqlCmd.append(getAcct("T_RECEIVABLES_ACCT")).append(",");
        sqlCmd.append(getAcct("T_DUE_ACCT")).append(",");
        sqlCmd.append(getAcct("T_CREDIT_ACCT")).append(", ");
        sqlCmd.append(getAcct("B_INTRANSIT_ACCT")).append(",");
        sqlCmd.append(getAcct("B_ASSET_ACCT")).append(",");
        sqlCmd.append(getAcct("B_EXPENSE_ACCT")).append(",");
        sqlCmd.append(getAcct("B_INTERESTREV_ACCT")).append(",");
        sqlCmd.append(getAcct("B_INTERESTEXP_ACCT")).append(",");
        sqlCmd.append(getAcct("B_UNIDENTIFIED_ACCT")).append(",");
        sqlCmd.append(getAcct("B_SETTLEMENTGAIN_ACCT")).append(",");
        sqlCmd.append(getAcct("B_SETTLEMENTLOSS_ACCT")).append(",");
        sqlCmd.append(getAcct("B_REVALUATIONGAIN_ACCT")).append(",");
        sqlCmd.append(getAcct("B_REVALUATIONLOSS_ACCT")).append(",");
        sqlCmd.append(getAcct("B_PAYMENTSELECT_ACCT")).append(",");
        sqlCmd.append(getAcct("B_UNALLOCATEDCASH_ACCT")).append(", ");
        sqlCmd.append(getAcct("CH_EXPENSE_ACCT")).append(",");
        sqlCmd.append(getAcct("CH_REVENUE_ACCT")).append(", ");
        sqlCmd.append(getAcct("UNEARNEDREVENUE_ACCT")).append(",");
        sqlCmd.append(getAcct("NOTINVOICEDRECEIVABLES_ACCT")).append(",");
        sqlCmd.append(getAcct("NOTINVOICEDREVENUE_ACCT")).append(",");
        sqlCmd.append(getAcct("NOTINVOICEDRECEIPTS_ACCT")).append(", ");
        sqlCmd.append(getAcct("CB_ASSET_ACCT")).append(",");
        sqlCmd.append(getAcct("CB_CASHTRANSFER_ACCT")).append(",");
        sqlCmd.append(getAcct("CB_DIFFERENCES_ACCT")).append(",");
        sqlCmd.append(getAcct("CB_EXPENSE_ACCT")).append(",");
        sqlCmd.append(getAcct("CB_RECEIPT_ACCT")).append(")");
        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
        if (no != 1) {
            String err = "Default Accounts NOT inserted";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        createGLCategory("Standard", MGLCategory.CATEGORYTYPE_Manual, true);
        int GL_None = createGLCategory("Ninguna", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_GL = createGLCategory("Manual", MGLCategory.CATEGORYTYPE_Manual, false);
        int GL_ARI = createGLCategory("Factura a Cliente", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_ARR = createGLCategory("Cobro a Cliente", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_MM = createGLCategory("Gestion de Materiales", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_API = createGLCategory("Factura de Proveedor", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_APP = createGLCategory("Pago a Proveedor", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_CASH = createGLCategory("Caja/Pagos", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_M = createGLCategory("Manufactura ", MGLCategory.CATEGORYTYPE_Document, false);
        if (!this.createAllDocTypes(GL_None, GL_GL, GL_ARI, GL_ARR, GL_MM, GL_API, GL_APP, GL_CASH, GL_M, 1, -1)) {
            return false;
        }
        if (m_currentYear != null) m_currentYear.createControlsOfAllPeriods();
        return true;
    }
