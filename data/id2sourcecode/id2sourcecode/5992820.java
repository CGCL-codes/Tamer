    @Override
    protected void addPanelBuilderComponents(PanelBuilder panelBuilder) {
        panelBuilder.add(new JLabel(Language.translateStatic("QUESTION_ACCEPTCERTIFICATE")), getCellConstraints(1, 1, 6));
        panelBuilder.addSeparator(Language.translateStatic("CERTIFICATE"), getCellConstraints(1, 3, 7));
        panelBuilder.add(new JLabel("sha1"), getCellConstraints(2, 5));
        byte[] encoded = new byte[0];
        try {
            encoded = x509Certificate.getEncoded();
        } catch (CertificateEncodingException e) {
        }
        try {
            panelBuilder.add(new JLabel(toHexString(MessageDigest.getInstance("SHA1").digest(encoded))), getCellConstraints(4, 5));
        } catch (NoSuchAlgorithmException e) {
            panelBuilder.add(new JLabel(Language.translateStatic("ERROR_SHA1NOTFOUND")), getCellConstraints(4, 5));
        }
        panelBuilder.add(new JLabel("md5"), getCellConstraints(2, 7));
        try {
            panelBuilder.add(new JLabel(toHexString(MessageDigest.getInstance("MD5").digest(encoded))), getCellConstraints(4, 7));
        } catch (NoSuchAlgorithmException e) {
            panelBuilder.add(new JLabel(Language.translateStatic("ERROR_MD5NOTFOUND")), getCellConstraints(4, 7));
        }
        panelBuilder.addSeparator(Language.translateStatic("CERTIFICATE_OWNER"), getCellConstraints(1, 9, 7));
        if (subjectValues != null && subjectValues.length > 0) {
            for (int n = 0, i = subjectValues.length; n < i; n++) {
                int index = subjectValues[n].indexOf('=');
                panelBuilder.add(new JLabel(Language.translateStatic("CERTIFICATE_" + subjectValues[n].substring(0, index).trim())), getCellConstraints(2, 11 + 2 * n));
                panelBuilder.add(new JLabel(subjectValues[n].substring(index + 1).trim().replaceAll("\\\"", "")), getCellConstraints(4, 11 + 2 * n));
            }
        } else {
        }
        panelBuilder.addSeparator(Language.translateStatic("CERTIFICATE_ISSUER"), getCellConstraints(1, 11 + skip, 7));
        if (issuerValues != null && issuerValues.length > 0) {
            for (int n = 0, i = issuerValues.length; n < i; n++) {
                int index = issuerValues[n].indexOf('=');
                if (index > -1) {
                    panelBuilder.add(new JLabel(Language.translateStatic(("CERTIFICATE_" + issuerValues[n].substring(0, index).trim()).trim())), getCellConstraints(2, 13 + skip + 2 * n));
                    panelBuilder.add(new JLabel(issuerValues[n].substring(index + 1).trim().replaceAll("\\\"", "")), getCellConstraints(4, 13 + skip + 2 * n));
                }
            }
        } else {
        }
        panelBuilder.add(new JLabel(Language.translateStatic("HINT_CERTIFICATERESTART")), getCellConstraints(1, 15 + skip + issuerSkip, 6));
    }
