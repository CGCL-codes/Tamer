    public byte[] toFIXBytes() {
        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            if (_IOIid != null) {
                bs.write((String.valueOf(TAG_IOIid) + ES + _IOIid + SOH).getBytes());
            }
            if (_IOITransType != null) {
                bs.write((String.valueOf(TAG_IOITransType) + ES + _IOITransType + SOH).getBytes());
            }
            if (_IOIRefID != null) {
                bs.write((String.valueOf(TAG_IOIRefID) + ES + _IOIRefID + SOH).getBytes());
            }
            if (_Symbol != null) {
                bs.write((String.valueOf(TAG_Symbol) + ES + _Symbol + SOH).getBytes());
            }
            if (_SymbolSfx != null) {
                bs.write((String.valueOf(TAG_SymbolSfx) + ES + _SymbolSfx + SOH).getBytes());
            }
            if (_SecurityID != null) {
                bs.write((String.valueOf(TAG_SecurityID) + ES + _SecurityID + SOH).getBytes());
            }
            if (_IDSource != null) {
                bs.write((String.valueOf(TAG_IDSource) + ES + _IDSource + SOH).getBytes());
            }
            if (_SecurityType != null) {
                bs.write((String.valueOf(TAG_SecurityType) + ES + _SecurityType + SOH).getBytes());
            }
            if (_MaturityMonthYear != null) {
                bs.write((String.valueOf(TAG_MaturityMonthYear) + ES + _MaturityMonthYear + SOH).getBytes());
            }
            if (_MaturityDay != null) {
                bs.write((String.valueOf(TAG_MaturityDay) + ES + _MaturityDay + SOH).getBytes());
            }
            if (_PutOrCall != null) {
                bs.write((String.valueOf(TAG_PutOrCall) + ES + _PutOrCall + SOH).getBytes());
            }
            if (_StrikePrice != null) {
                bs.write((String.valueOf(TAG_StrikePrice) + ES + _StrikePrice + SOH).getBytes());
            }
            if (_OptAttribute != null) {
                bs.write((String.valueOf(TAG_OptAttribute) + ES + _OptAttribute + SOH).getBytes());
            }
            if (_ContractMultiplier != null) {
                bs.write((String.valueOf(TAG_ContractMultiplier) + ES + _ContractMultiplier + SOH).getBytes());
            }
            if (_CouponRate != null) {
                bs.write((String.valueOf(TAG_CouponRate) + ES + _CouponRate + SOH).getBytes());
            }
            if (_SecurityExchange != null) {
                bs.write((String.valueOf(TAG_SecurityExchange) + ES + _SecurityExchange + SOH).getBytes());
            }
            if (_Issuer != null) {
                bs.write((String.valueOf(TAG_Issuer) + ES + _Issuer + SOH).getBytes());
            }
            if (_EncodedIssuerLen != null) {
                bs.write((String.valueOf(TAG_EncodedIssuerLen) + ES + _EncodedIssuerLen + SOH).getBytes());
            }
            if (_EncodedIssuer != null) {
                bs.write((String.valueOf(TAG_EncodedIssuer) + ES + _EncodedIssuer + SOH).getBytes());
            }
            if (_SecurityDesc != null) {
                bs.write((String.valueOf(TAG_SecurityDesc) + ES + _SecurityDesc + SOH).getBytes());
            }
            if (_EncodedSecurityDescLen != null) {
                bs.write((String.valueOf(TAG_EncodedSecurityDescLen) + ES + _EncodedSecurityDescLen + SOH).getBytes());
            }
            if (_EncodedSecurityDesc != null) {
                bs.write((String.valueOf(TAG_EncodedSecurityDesc) + ES + _EncodedSecurityDesc + SOH).getBytes());
            }
            if (_Side != null) {
                bs.write((String.valueOf(TAG_Side) + ES + _Side + SOH).getBytes());
            }
            if (_IOIShares != null) {
                bs.write((String.valueOf(TAG_IOIShares) + ES + _IOIShares + SOH).getBytes());
            }
            if (_Price != null) {
                bs.write((String.valueOf(TAG_Price) + ES + _Price + SOH).getBytes());
            }
            if (_Currency != null) {
                bs.write((String.valueOf(TAG_Currency) + ES + _Currency + SOH).getBytes());
            }
            if (_ValidUntilTime != null) {
                bs.write((String.valueOf(TAG_ValidUntilTime) + ES + _ValidUntilTime + SOH).getBytes());
            }
            if (_IOIQltyInd != null) {
                bs.write((String.valueOf(TAG_IOIQltyInd) + ES + _IOIQltyInd + SOH).getBytes());
            }
            if (_IOINaturalFlag != null) {
                bs.write((String.valueOf(TAG_IOINaturalFlag) + ES + _IOINaturalFlag + SOH).getBytes());
            }
            if (_NoIOIQualifiers != null) {
                bs.write((String.valueOf(TAG_NoIOIQualifiers) + ES + _NoIOIQualifiers + SOH).getBytes());
            }
            if (_IOIQualifierSeq != null) {
                bs.write(_IOIQualifierSeq.toFIXBytes());
            }
            if (_Text != null) {
                bs.write((String.valueOf(TAG_Text) + ES + _Text + SOH).getBytes());
            }
            if (_EncodedTextLen != null) {
                bs.write((String.valueOf(TAG_EncodedTextLen) + ES + _EncodedTextLen + SOH).getBytes());
            }
            if (_EncodedText != null) {
                bs.write((String.valueOf(TAG_EncodedText) + ES + _EncodedText + SOH).getBytes());
            }
            if (_TransactTime != null) {
                bs.write((String.valueOf(TAG_TransactTime) + ES + _TransactTime + SOH).getBytes());
            }
            if (_URLLink != null) {
                bs.write((String.valueOf(TAG_URLLink) + ES + _URLLink + SOH).getBytes());
            }
            if (_NoRoutingIDs != null) {
                bs.write((String.valueOf(TAG_NoRoutingIDs) + ES + _NoRoutingIDs + SOH).getBytes());
            }
            if (_RoutingIDSeq != null) {
                bs.write(_RoutingIDSeq.toFIXBytes());
            }
            if (_SpreadToBenchmark != null) {
                bs.write((String.valueOf(TAG_SpreadToBenchmark) + ES + _SpreadToBenchmark + SOH).getBytes());
            }
            if (_Benchmark != null) {
                bs.write((String.valueOf(TAG_Benchmark) + ES + _Benchmark + SOH).getBytes());
            }
            byte[] t = bs.toByteArray();
            bs.close();
            return t;
        } catch (IOException ie) {
            ie.printStackTrace();
            return new byte[0];
        }
    }
