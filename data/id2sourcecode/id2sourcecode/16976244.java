    public void updateFailedMsg(long theSeqNum, String thePckId, String theAppNum, String theAppType, String theDocCode, String theMsgTxt, String theErrorMsg) throws JDistException {
        String IN_PROCESS_MSGS = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_TABLENAME_PRP);
        String IN_ERROR_MSGS = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INERROR_TABLENAME_PRP);
        String SEQNUM_P = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_COLUMN_SEQNUM_PRP);
        String SEQNUM_E = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INERROR_COLUMN_SEQNUM_PRP);
        String PXIDOCID_E = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INERROR_COLUMN_PXIDOCID_PRP);
        String APPNUM_E = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INERROR_COLUMN_APPNUM_PRP);
        String APPTYPE_E = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INERROR_COLUMN_APPTYPE_PRP);
        String DOCCODE_E = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INERROR_COLUMN_DOCCODE_PRP);
        String MSGTXT_E = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INERROR_COLUMN_MSGTXT_PRP);
        String ERRORMSG_E = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INERROR_COLUMN_ERRORMSG_PRP);
        String UPDATETS_E = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INERROR_COLUMN_UPDATETS_PRP);
        Timestamp myUpdateTS = new Timestamp(System.currentTimeMillis());
        String myErrorMsg;
        if ((theErrorMsg != null) && (theErrorMsg.length() > 79)) myErrorMsg = theErrorMsg.substring(0, 79); else myErrorMsg = theErrorMsg;
        String myMsg2Insert = "INSERT INTO " + IN_ERROR_MSGS + " (" + SEQNUM_E + "," + PXIDOCID_E + "," + APPNUM_E + "," + APPTYPE_E + "," + DOCCODE_E + "," + MSGTXT_E + "," + ERRORMSG_E + "," + UPDATETS_E + ") VALUES (" + theSeqNum + ",'" + thePckId + "','" + theAppNum + "','" + theAppType + "','" + theDocCode + "','" + theMsgTxt + "','" + myErrorMsg + "',{ts '" + myUpdateTS + "'})";
        String myMsg2Delete = "DELETE FROM " + IN_PROCESS_MSGS + " WHERE " + SEQNUM_P + "=" + theSeqNum;
        try {
            setAutoCommit(false);
            createStatement();
            getStatement().executeUpdate(myMsg2Insert);
            getStatement().executeUpdate(myMsg2Delete);
            getConnection().commit();
        } catch (SQLException se) {
            try {
                getConnection().rollback();
            } catch (SQLException se2) {
                throw new JDistException(JDistException.ERR_DB_SQL, se2.getMessage());
            }
            throw new JDistException(JDistException.ERR_DB_SQL, se.getMessage());
        } catch (JDistException e) {
            if (e.getReturnCode() == JDistException.ERR_DB_SQL) {
                try {
                    getConnection().rollback();
                } catch (SQLException se2) {
                    throw new JDistException(JDistException.ERR_DB_SQL, se2.getMessage());
                }
            }
            throw new JDistException(JDistException.ERR_DB_SQL, e.getMessage());
        } finally {
            try {
                setAutoCommit(true);
            } catch (JDistException je) {
            }
        }
    }
