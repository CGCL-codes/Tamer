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
        if (m_calendar.createYear(m_client.getLocale()) == null) log.log(Level.SEVERE, "Year NOT inserted");
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
        m_nap = new NaturalAccountMap<String, MElementValue>(m_ctx, m_trx.getTrxName());
        String errMsg = m_nap.parseFile(AccountingFile);
        if (errMsg.length() != 0) {
            log.log(Level.SEVERE, errMsg);
            m_info.append(errMsg);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        if (m_nap.saveAccounts(getAD_Client_ID(), getAD_Org_ID(), C_Element_ID)) m_info.append(Msg.translate(m_lang, "C_ElementValue_ID")).append(" # ").append(m_nap.size()).append("\n"); else {
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
        if (Env.isBaseLanguage(m_lang, "AD_Reference")) sql2 = "SELECT Value, Name FROM AD_Ref_List WHERE AD_Reference_ID=181"; else sql2 = "SELECT l.Value, t.Name FROM AD_Ref_List l, AD_Ref_List_Trl t " + "WHERE l.AD_Reference_ID=181 AND l.AD_Ref_List_ID=t.AD_Ref_List_ID" + " AND t.AD_Language=" + DB.TO_STRING(m_lang);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            int AD_Client_ID = m_client.getAD_Client_ID();
            stmt = DB.prepareStatement(sql2, m_trx.getTrxName());
            rs = stmt.executeQuery();
            while (rs.next()) {
                String ElementType = rs.getString(1);
                name = rs.getString(2);
                String IsMandatory = null;
                String IsBalanced = "N";
                int SeqNo = 0;
                int C_AcctSchema_Element_ID = 0;
                if (ElementType.equals("OO")) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    IsMandatory = "Y";
                    IsBalanced = "Y";
                    SeqNo = 10;
                } else if (ElementType.equals("AC")) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    IsMandatory = "Y";
                    SeqNo = 20;
                } else if (ElementType.equals("PR") && hasProduct) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    IsMandatory = "N";
                    SeqNo = 30;
                } else if (ElementType.equals("BP") && hasBPartner) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    IsMandatory = "N";
                    SeqNo = 40;
                } else if (ElementType.equals("PJ") && hasProject) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    IsMandatory = "N";
                    SeqNo = 50;
                } else if (ElementType.equals("MC") && hasMCampaign) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    IsMandatory = "N";
                    SeqNo = 60;
                } else if (ElementType.equals("SR") && hasSRegion) {
                    C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
                    IsMandatory = "N";
                    SeqNo = 70;
                }
                if (IsMandatory != null) {
                    sqlCmd = new StringBuffer("INSERT INTO C_AcctSchema_Element(");
                    sqlCmd.append(m_stdColumns).append(",C_AcctSchema_Element_ID,C_AcctSchema_ID,").append("ElementType,Name,SeqNo,IsMandatory,IsBalanced) VALUES (");
                    sqlCmd.append(m_stdValues).append(",").append(C_AcctSchema_Element_ID).append(",").append(m_as.getC_AcctSchema_ID()).append(",").append("'").append(ElementType).append("','").append(name).append("',").append(SeqNo).append(",'").append(IsMandatory).append("','").append(IsBalanced).append("')");
                    no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
                    if (no == 1) m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\n");
                    if (ElementType.equals("OO")) {
                        sqlCmd = new StringBuffer("UPDATE C_AcctSchema_Element SET Org_ID=");
                        sqlCmd.append(getAD_Org_ID()).append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
                        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
                        if (no != 1) log.log(Level.SEVERE, "Default Org in AcctSchamaElement NOT updated");
                    }
                    if (ElementType.equals("AC")) {
                        sqlCmd = new StringBuffer("UPDATE C_AcctSchema_Element SET C_ElementValue_ID=");
                        sqlCmd.append(C_ElementValue_ID).append(", C_Element_ID=").append(C_Element_ID);
                        sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
                        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
                        if (no != 1) log.log(Level.SEVERE, "Default Account in AcctSchamaElement NOT updated");
                    }
                }
            }
        } catch (SQLException e1) {
            log.log(Level.SEVERE, "Elements", e1);
            m_info.append(e1.getMessage());
            m_trx.rollback();
            m_trx.close();
            return false;
        } finally {
            DB.close(rs, stmt);
            rs = null;
            stmt = null;
        }
        try {
            createAccountingRecord(X_C_AcctSchema_GL.Table_Name);
            createAccountingRecord(X_C_AcctSchema_Default.Table_Name);
        } catch (Exception e) {
            String err = e.getLocalizedMessage();
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        createGLCategory("Standard", MGLCategory.CATEGORYTYPE_Manual, true);
        int GL_None = createGLCategory("None", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_GL = createGLCategory("Manual", MGLCategory.CATEGORYTYPE_Manual, false);
        int GL_ARI = createGLCategory("AR Invoice", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_ARR = createGLCategory("AR Receipt", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_MM = createGLCategory("Material Management", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_API = createGLCategory("AP Invoice", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_APP = createGLCategory("AP Payment", MGLCategory.CATEGORYTYPE_Document, false);
        int GL_CASH = createGLCategory("Cash/Payments", MGLCategory.CATEGORYTYPE_Document, false);
        int ii = createDocType("GL Journal", Msg.getElement(m_ctx, "GL_Journal_ID"), MDocType.DOCBASETYPE_GLJournal, null, 0, 0, 1000, GL_GL);
        if (ii == 0) {
            String err = "Document Type not created";
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        createDocType("GL Journal Batch", Msg.getElement(m_ctx, "GL_JournalBatch_ID"), MDocType.DOCBASETYPE_GLJournal, null, 0, 0, 100, GL_GL);
        int DT_I = createDocType("AR Invoice", Msg.getElement(m_ctx, "C_Invoice_ID", true), MDocType.DOCBASETYPE_ARInvoice, null, 0, 0, 100000, GL_ARI);
        int DT_II = createDocType("AR Invoice Indirect", Msg.getElement(m_ctx, "C_Invoice_ID", true), MDocType.DOCBASETYPE_ARInvoice, null, 0, 0, 150000, GL_ARI);
        int DT_IC = createDocType("AR Credit Memo", Msg.getMsg(m_ctx, "CreditMemo"), MDocType.DOCBASETYPE_ARCreditMemo, null, 0, 0, 170000, GL_ARI);
        createDocType("AP Invoice", Msg.getElement(m_ctx, "C_Invoice_ID", false), MDocType.DOCBASETYPE_APInvoice, null, 0, 0, 0, GL_API);
        int DT_IPC = createDocType("AP CreditMemo", Msg.getMsg(m_ctx, "CreditMemo"), MDocType.DOCBASETYPE_APCreditMemo, null, 0, 0, 0, GL_API);
        createDocType("Match Invoice", Msg.getElement(m_ctx, "M_MatchInv_ID", false), MDocType.DOCBASETYPE_MatchInvoice, null, 0, 0, 390000, GL_API);
        createDocType("AR Receipt", Msg.getElement(m_ctx, "C_Payment_ID", true), MDocType.DOCBASETYPE_ARReceipt, null, 0, 0, 0, GL_ARR);
        createDocType("AP Payment", Msg.getElement(m_ctx, "C_Payment_ID", false), MDocType.DOCBASETYPE_APPayment, null, 0, 0, 0, GL_APP);
        createDocType("Allocation", "Allocation", MDocType.DOCBASETYPE_PaymentAllocation, null, 0, 0, 490000, GL_CASH);
        int DT_S = createDocType("MM Shipment", "Delivery Note", MDocType.DOCBASETYPE_MaterialDelivery, null, 0, 0, 500000, GL_MM);
        int DT_SI = createDocType("MM Shipment Indirect", "Delivery Note", MDocType.DOCBASETYPE_MaterialDelivery, null, 0, 0, 550000, GL_MM);
        int DT_VRM = createDocType("MM Vendor Return", "Vendor Returns", MDocType.DOCBASETYPE_MaterialDelivery, null, 0, 0, 590000, GL_MM);
        createDocType("MM Receipt", "Vendor Delivery", MDocType.DOCBASETYPE_MaterialReceipt, null, 0, 0, 0, GL_MM);
        int DT_RM = createDocType("MM Returns", "Customer Returns", MDocType.DOCBASETYPE_MaterialReceipt, null, 0, 0, 570000, GL_MM);
        createDocType("Purchase Order", Msg.getElement(m_ctx, "C_Order_ID", false), MDocType.DOCBASETYPE_PurchaseOrder, null, 0, 0, 800000, GL_None);
        createDocType("Match PO", Msg.getElement(m_ctx, "M_MatchPO_ID", false), MDocType.DOCBASETYPE_MatchPO, null, 0, 0, 890000, GL_None);
        createDocType("Purchase Requisition", Msg.getElement(m_ctx, "M_Requisition_ID", false), MDocType.DOCBASETYPE_PurchaseRequisition, null, 0, 0, 900000, GL_None);
        createDocType("Vendor Return Material", "Vendor Return Material Authorization", MDocType.DOCBASETYPE_PurchaseOrder, MDocType.DOCSUBTYPESO_ReturnMaterial, DT_VRM, DT_IPC, 990000, GL_MM);
        createDocType("Bank Statement", Msg.getElement(m_ctx, "C_BankStatemet_ID", true), MDocType.DOCBASETYPE_BankStatement, null, 0, 0, 700000, GL_CASH);
        createDocType("Cash Journal", Msg.getElement(m_ctx, "C_Cash_ID", true), MDocType.DOCBASETYPE_CashJournal, null, 0, 0, 750000, GL_CASH);
        createDocType("Material Movement", Msg.getElement(m_ctx, "M_Movement_ID", false), MDocType.DOCBASETYPE_MaterialMovement, null, 0, 0, 610000, GL_MM);
        createDocType("Physical Inventory", Msg.getElement(m_ctx, "M_Inventory_ID", false), MDocType.DOCBASETYPE_MaterialPhysicalInventory, null, 0, 0, 620000, GL_MM);
        createDocType("Material Production", Msg.getElement(m_ctx, "M_Production_ID", false), MDocType.DOCBASETYPE_MaterialProduction, null, 0, 0, 630000, GL_MM);
        createDocType("Project Issue", Msg.getElement(m_ctx, "C_ProjectIssue_ID", false), MDocType.DOCBASETYPE_ProjectIssue, null, 0, 0, 640000, GL_MM);
        createDocType("Binding offer", "Quotation", MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_Quotation, 0, 0, 10000, GL_None);
        createDocType("Non binding offer", "Proposal", MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_Proposal, 0, 0, 20000, GL_None);
        createDocType("Prepay Order", "Prepay Order", MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_PrepayOrder, DT_S, DT_I, 30000, GL_None);
        createDocType("Return Material", "Return Material Authorization", MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_ReturnMaterial, DT_RM, DT_IC, 30000, GL_None);
        createDocType("Standard Order", "Order Confirmation", MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_StandardOrder, DT_S, DT_I, 50000, GL_None);
        createDocType("Credit Order", "Order Confirmation", MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_OnCreditOrder, DT_SI, DT_I, 60000, GL_None);
        createDocType("Warehouse Order", "Order Confirmation", MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_WarehouseOrder, DT_S, DT_I, 70000, GL_None);
        int DT = createDocType("POS Order", "Order Confirmation", MDocType.DOCBASETYPE_SalesOrder, MDocType.DOCSUBTYPESO_POSOrder, DT_SI, DT_II, 80000, GL_None);
        createPreference("C_DocTypeTarget_ID", String.valueOf(DT), 143);
        sqlCmd = new StringBuffer("UPDATE AD_ClientInfo SET ");
        sqlCmd.append("C_AcctSchema1_ID=").append(m_as.getC_AcctSchema_ID()).append(", C_Calendar_ID=").append(m_calendar.getC_Calendar_ID()).append(" WHERE AD_Client_ID=").append(m_client.getAD_Client_ID());
        no = DB.executeUpdate(sqlCmd.toString(), m_trx.getTrxName());
        if (no != 1) {
            String err = "ClientInfo not updated";
            log.log(Level.SEVERE, err);
            m_info.append(err);
            m_trx.rollback();
            m_trx.close();
            return false;
        }
        DocumentTypeVerify.createDocumentTypes(m_ctx, getAD_Client_ID(), null, m_trx.getTrxName());
        DocumentTypeVerify.createPeriodControls(m_ctx, getAD_Client_ID(), null, m_trx.getTrxName());
        log.info("fini");
        return true;
    }
