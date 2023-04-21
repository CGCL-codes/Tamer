    protected String doIt() throws java.lang.Exception {
        trxName = Trx.createTrxName();
        Trx trx = Trx.get(trxName, true);
        trx.start();
        if (m_AD_Client_ID == 0 || m_C_Element_ID == 0 || m_MElement == null || m_MElement.getC_Element_ID() == 0) {
            return "Falta alguno de los datos para la importacion (Compañia o Elemento)";
        }
        m_AD_Tree_ID = m_MElement.getAD_Tree_ID();
        StringBuffer sql = null;
        int no = 0;
        if (m_deleteOldImported) {
            sql = new StringBuffer("DELETE I_ReportLineSet " + "WHERE I_IsImported='Y'").append(clientCheck);
            no = DB.executeUpdate(sql.toString(), trxName);
            log.log(Level.FINER, "ImportReportLineSetLines.doIt", "Delete Old Imported =" + no);
        }
        sql = new StringBuffer("UPDATE I_ReportLineSet " + "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append(")," + " AD_Org_ID = COALESCE (AD_Org_ID, 0)," + " IsActive = COALESCE (IsActive, 'Y')," + " Created = COALESCE (Created, SysDate)," + " CreatedBy = COALESCE (CreatedBy, 0)," + " Updated = COALESCE (Updated, SysDate)," + " UpdatedBy = COALESCE (UpdatedBy, 0)," + " I_ErrorMsg = NULL," + " I_IsImported = 'N' " + "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
        no = DB.executeUpdate(sql.toString(), trxName);
        log.log(Level.FINER, "ImportReportLineSet.doIt", "Reset=" + no);
        sql = new StringBuffer("UPDATE I_ReportLineSet " + "SET SeqNo=I_ReportLineSet_ID " + "WHERE SeqNo IS NULL" + " AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), trxName);
        log.log(Level.FINER, "ImportReportLine.doIt", "Set SeqNo Default=" + no);
        sql = new StringBuffer("UPDATE I_ReportLine " + "SET IsSummary='N' " + "WHERE IsSummary IS NULL OR IsSummary NOT IN ('Y','N')" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), trxName);
        log.log(Level.FINER, "ImportReportLine.doIt", "Set IsSummary Default=" + no);
        sql = new StringBuffer("UPDATE I_ReportLine " + "SET IsPrinted='Y' " + "WHERE IsPrinted IS NULL OR IsPrinted NOT IN ('Y','N')" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), trxName);
        log.log(Level.FINER, "ImportReportLine.doIt", "Set IsPrinted Default=" + no);
        sql = new StringBuffer("UPDATE I_ReportLine " + "SET LineType='D' " + "WHERE LineType IS NULL OR LineType NOT IN ('S','C','D')" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), trxName);
        log.log(Level.FINER, "ImportReportLine.doIt", "Set LineType Default=" + no);
        sql = new StringBuffer("UPDATE I_ReportLine " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid CalculationType, ' " + "WHERE AmountType IS NOT NULL AND UPPER(AmountType) NOT IN ('BP','CP','DP','QP', 'BY','CY','DY','QY', 'BT','CT','DT','QT')" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), trxName);
        log.log(Level.FINER, "ImportReportLine.doIt", "Invalid AmountType=" + no);
        sql = new StringBuffer("UPDATE I_ReportLine " + "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid CalculationType, ' " + "WHERE PostingType IS NOT NULL AND PostingType NOT IN ('A','B','E','S')" + " AND I_IsImported<>'Y'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), trxName);
        log.log(Level.FINER, "ImportReportLine.doIt", "Invalid PostingType=" + no);
        sql = new StringBuffer("UPDATE I_ReportLine i " + "SET PA_ReportLineSet_ID=(SELECT PA_ReportLineSet_ID FROM PA_ReportLineSet r" + " WHERE i.Name=r.Name  AND ROWNUM=1) " + "WHERE PA_ReportLineSet_ID IS NULL " + " AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), trxName);
        log.log(Level.FINER, "ImportReportLine.doIt", "Set PA_ReportLine_ID=" + no);
        sql = new StringBuffer("UPDATE I_ReportLine i " + "SET PA_ReportLine_ID=(SELECT PA_ReportLine_ID FROM PA_ReportLine r" + " WHERE i.Name=r.Name  AND ROWNUM=1) " + "WHERE PA_ReportLine_ID IS NULL AND PA_ReportLineSet_ID IS NOT NULL" + " AND I_IsImported='N'").append(clientCheck);
        no = DB.executeUpdate(sql.toString(), trxName);
        log.log(Level.FINER, "ImportReportLine.doIt", "Set PA_ReportLine_ID=" + no);
        if (importReportLineSet() == false) {
            trx.rollback();
            trx.close();
            return "No se ha podido crear el conjunto de lineas de informe";
        }
        if (importReportLines() == false) {
            trx.rollback();
            trx.close();
            return "No se han podido crear las lineas del informe";
        }
        if (importReportLineSource() == false) {
            trx.rollback();
            trx.close();
            return "No se han podido crear las lineas fuente del informe";
        }
        trx.commit();
        trx.close();
        return "";
    }
