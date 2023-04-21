    public Collection<? extends ProfileData> parseWireFile() throws IOException {
        InputStream isRaw = this.urlData.openStream();
        InputStreamReader isRdr = new InputStreamReader(isRaw);
        BufferedReader isBuf = new BufferedReader(isRdr);
        this.colProjData = new LinkedList<ProfileData>();
        if (!this.processTimeStamp(isBuf)) throw new IOException("SnsProFileParser#parse() - could not read time stamp");
        if (!this.processDeviceData(isBuf)) throw new IOException("SnsProFileParser#parse() - could not read profile data");
        if (!this.processPvLoggerId(isBuf)) throw new IOException("SnsProFileParser#parse() - could not read PV Logger Id");
        isBuf.close();
        for (ProfileData datDevice : this.colProjData) datDevice.setPvLoggerId(this.getPvLoggerId());
        return this.colProjData;
    }
