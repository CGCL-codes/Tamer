    private OTPDigest(MessageDigest md, String mdaName) {
        super();
        this.md = md;
        this.digestName = mdaName;
        digestLength = md.digest().length;
    }
