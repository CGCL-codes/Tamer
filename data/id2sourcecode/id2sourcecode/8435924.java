    @Override
    public void filter(final Map<String, String> mailHeaders, final String sBody, final Mail mail) {
        final Map<String, String> fields = new LinkedHashMap<String, String>();
        fields.put("v", "1");
        fields.put("a", "rsa-sha256");
        fields.put("c", "relaxed/simple");
        fields.put("s", this.dnsSelector);
        fields.put("d", this.domain);
        fields.put("i", Format.extractAddress(mail.sFrom));
        fields.put("q", "dns/txt");
        fields.put("t", String.valueOf(System.currentTimeMillis() / 1000));
        final List<String> foundHeaders = new LinkedList<String>();
        final StringBuilder sbHeader = new StringBuilder(1024);
        for (String header : this.headers) {
            if (mailHeaders.containsKey(header)) {
                foundHeaders.add(header);
                sbHeader.append(relaxedHeader(header, mailHeaders.get(header))).append(Sendmail.CRLF);
            }
        }
        final StringBuilder hField = new StringBuilder();
        for (final String header : foundHeaders) {
            if (hField.length() > 0) hField.append(':');
            hField.append(header);
        }
        fields.put("h", hField.toString());
        final String sCanonBody = simpleBody(sBody);
        fields.put("l", String.valueOf(sCanonBody.length()));
        fields.put("bh", Utils.base64Encode(this.digester.digest(sCanonBody.getBytes())));
        fields.put("b", "");
        final String DKIM = "DKIM-Signature";
        final StringBuilder sbValue = new StringBuilder(256);
        for (Map.Entry<String, String> me : fields.entrySet()) {
            if (sbValue.length() > 0) sbValue.append("; ");
            sbValue.append(me.getKey()).append('=').append(me.getValue());
        }
        final String sKeyValue = sbValue.toString();
        sbHeader.append(relaxedHeader(DKIM, sKeyValue));
        final String sHeaders = sbHeader.toString();
        try {
            this.signer.update(sHeaders.getBytes());
            byte[] signedSignature = this.signer.sign();
            mailHeaders.put(DKIM, sKeyValue + Utils.base64Encode(signedSignature));
        } catch (SignatureException se) {
        }
    }
