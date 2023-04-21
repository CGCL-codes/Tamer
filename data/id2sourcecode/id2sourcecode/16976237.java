    public long insertInProcessMsg(String thePckId, String theAppNum, String theAppType, String theDocCode, String theMsgTxt) throws JDistException {
        String IN_PROCESS_MSGS = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_TABLENAME_PRP);
        String SEQNUM = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_COLUMN_SEQNUM_PRP);
        String PXIDOCID = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_COLUMN_PXIDOCID_PRP);
        String APPNUM = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_COLUMN_APPNUM_PRP);
        String APPTYPE = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_COLUMN_APPTYPE_PRP);
        String DOCCODE = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_COLUMN_DOCCODE_PRP);
        String MSGTXT = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_COLUMN_MSGTXT_PRP);
        String STATUS = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_COLUMN_STATUS_PRP);
        String UPDATETS = checkGetProperty(getSysParam().getProperties(), JDistSysParam.DB_INPROCESS_COLUMN_UPDATETS_PRP);
        int myStatus = 0;
        long mySeqNum = 0;
        try {
            setAutoCommit(false);
            createStatement();
            mySeqNum = retrieveSeqNum();
            if (mySeqNum == 0) {
                try {
                    getConnection().rollback();
                } catch (SQLException se2) {
                    throw new JDistException(JDistException.ERR_DB_SQL, se2.getMessage());
                }
                throw new JDistException(JDistException.ERR_DB_SQL, "No sequence number found");
            }
            Timestamp myTimeStamp = new Timestamp(System.currentTimeMillis());
            String mySqlQuery = "INSERT INTO " + IN_PROCESS_MSGS + "(" + SEQNUM + "," + PXIDOCID + "," + APPNUM + "," + APPTYPE + "," + DOCCODE + "," + MSGTXT + "," + STATUS + "," + UPDATETS + ") VALUES (" + mySeqNum + ",'" + thePckId + "','" + theAppNum + "','" + theAppType + "','" + theDocCode + "','" + theMsgTxt + "'," + myStatus + ",{ts '" + myTimeStamp + "'})";
            getStatement().executeUpdate(mySqlQuery);
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
        return mySeqNum;
    }
