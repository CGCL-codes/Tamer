    protected void writeTo(WcOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeShort(dwVersion);
        out.writeShort(dwRevision);
        out.writeInt(port);
        out.writeShort(sendthreads);
        out.writeShort(acceptthreads);
        out.writeInt(VerboseLogging);
        out.writeBoolean(acceptonly);
        out.writeInt(retries);
        out.writeInt(retrywait);
        out.writeString(smarthost, 52);
        out.writeInt(sizelimit);
        out.writeBoolean(localonly);
        out.writeBoolean(MAPSRBL);
        out.writeString(MAPSRBLServer, 52);
        out.writeBoolean(ESMTP);
        out.writeBoolean(reqauth);
        out.writeBoolean(VRFY);
        out.writeBoolean(AllowRelay);
        out.writeBoolean(CheckRCPT);
        out.writeBoolean(EnableBadFilter);
        out.writeBoolean(RequireMX);
        out.writeBoolean(RequireHostMatch);
    }
