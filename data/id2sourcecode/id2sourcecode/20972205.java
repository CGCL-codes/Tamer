    public byte[] toFIXBytes() {
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            if (_EmailThreadID != null) {
                bs.write((String.valueOf(TAG_EmailThreadID) + ES + _EmailThreadID + SOH).getBytes());
            }
            if (_EmailType != null) {
                bs.write((String.valueOf(TAG_EmailType) + ES + _EmailType + SOH).getBytes());
            }
            if (_OrigTime != null) {
                bs.write((String.valueOf(TAG_OrigTime) + ES + _OrigTime + SOH).getBytes());
            }
            if (_Subject != null) {
                bs.write((String.valueOf(TAG_Subject) + ES + _Subject + SOH).getBytes());
            }
            if (_EncodedSubjectLen != null) {
                bs.write((String.valueOf(TAG_EncodedSubjectLen) + ES + _EncodedSubjectLen + SOH).getBytes());
            }
            if (_EncodedSubject != null) {
                bs.write((String.valueOf(TAG_EncodedSubject) + ES + _EncodedSubject + SOH).getBytes());
            }
            if (_NoRelatedSym != null) {
                bs.write((String.valueOf(TAG_NoRelatedSym) + ES + _NoRelatedSym + SOH).getBytes());
            }
            if (_RelatdSymSeq != null) {
                bs.write(_RelatdSymSeq.toFIXBytes());
            }
            if (_OrderID != null) {
                bs.write((String.valueOf(TAG_OrderID) + ES + _OrderID + SOH).getBytes());
            }
            if (_ClOrdID != null) {
                bs.write((String.valueOf(TAG_ClOrdID) + ES + _ClOrdID + SOH).getBytes());
            }
            if (_LinesOfText != null) {
                bs.write((String.valueOf(TAG_LinesOfText) + ES + _LinesOfText + SOH).getBytes());
            }
            if (_LinesOfTextSeq != null) {
                bs.write(_LinesOfTextSeq.toFIXBytes());
            }
            if (_RawDataLength != null) {
                bs.write((String.valueOf(TAG_RawDataLength) + ES + _RawDataLength + SOH).getBytes());
            }
            if (_RawData != null) {
                bs.write((String.valueOf(TAG_RawData) + ES + _RawData + SOH).getBytes());
            }
            byte[] t = bs.toByteArray();
            bs.close();
            return t;
        } catch (IOException ie) {
            ie.printStackTrace();
            return new byte[0];
        }
    }
