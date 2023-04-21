        public void addSpectrumRWValue(final StringBuffer readValue, final StringBuffer writeValue, final int dimX, final Timestamp timestamp) throws SQLException, ArchivingException {
            if (currentBulkSize == 0) {
                queryBuffer.append("(?, ?, ?, ?)");
            } else {
                queryBuffer.append(" ,(?, ?, ?, ?)");
            }
            addSpectrum(readValue, writeValue, dimX, timestamp, true);
        }
