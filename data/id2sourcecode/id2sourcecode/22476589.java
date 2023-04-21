    private void checkRecordPosition(int recNum) throws RecordsetException {
        if (recNum < 0 || recNum > _data.size() - 1) {
            StringBuffer errMsg = new StringBuffer();
            errMsg.append("Invalid record position: " + recNum + "; ");
            if (recNum == -1) errMsg.append("After creating a Recordset you must move to a valid record using next(), first(), last() or setRecordNumber() methods before attempting read/write operations with any record of this Recordset; ");
            errMsg.append("This Recordset contains " + _data.size() + " record(s); Set the record position between 0 and N-1 where N is the number of records.");
            throw new RecordsetException(errMsg.toString());
        }
    }
