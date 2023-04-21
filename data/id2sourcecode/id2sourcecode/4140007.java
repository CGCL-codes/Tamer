    public ISDN_Setup(XL_RFSWithData original, ICB_ISDN_Raw icb) {
        this.icb = icb;
        this.span = original.getSpan();
        this.channel = original.getChannel();
        this.cgpn = (IE_CallingPartyNumber) icb.getField(Q931.IE.CALLING_PARTY_NUMBER);
        this.cdpn = (IE_CalledPartyNumber) icb.getField(Q931.IE.CALLED_PARTY_NUMBER);
    }
