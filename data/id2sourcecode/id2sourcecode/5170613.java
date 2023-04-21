    private String getDataTruncationMessage(DataTruncation ex) {
        StringBuilder buf = new StringBuilder();
        buf.append("Data Truncation error occured on").append(ex.getRead() ? " a read " : " a write ").append(" of column ").append(ex.getIndex()).append("Data was ").append(ex.getDataSize()).append(" bytes long and ").append(ex.getTransferSize()).append(" bytes were transferred.");
        return buf.toString();
    }
