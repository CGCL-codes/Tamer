    private byte[] addSignatureToPDFDocument(PDFSignerParameters params, byte[] pdfbytes, byte[] password) throws IOException, DocumentException, CryptoTokenOfflineException, SignServerException, IllegalRequestException {
        Collection<Certificate> certs = this.getSigningCertificateChain();
        if (certs == null) {
            throw new SignServerException("Null certificate chain. This signer needs a certificate.");
        }
        Certificate[] certChain = (Certificate[]) certs.toArray(new Certificate[0]);
        PrivateKey privKey = this.getCryptoToken().getPrivateKey(ICryptoToken.PURPOSE_SIGN);
        PdfReader reader = new PdfReader(pdfbytes, password);
        boolean appendMode = true;
        if (reader.getCertificationLevel() != PdfSignatureAppearance.NOT_CERTIFIED && params.getCertification_level() != PdfSignatureAppearance.NOT_CERTIFIED) {
            throw new IllegalRequestException("Will not certify an already certified document");
        }
        if (reader.getCertificationLevel() == PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED || reader.getCertificationLevel() == PdfSignatureAppearance.CERTIFIED_FORM_FILLING) {
            throw new IllegalRequestException("Will not sign a certified document where signing is not allowed");
        }
        Permissions currentPermissions = Permissions.fromInt(reader.getPermissions());
        if (params.getSetPermissions() != null && params.getRemovePermissions() != null) {
            throw new SignServerException("Signer " + workerId + " missconfigured. Only one of " + SET_PERMISSIONS + " and " + REMOVE_PERMISSIONS + " should be specified.");
        }
        Permissions newPermissions;
        if (params.getSetPermissions() != null) {
            newPermissions = params.getSetPermissions();
        } else if (params.getRemovePermissions() != null) {
            newPermissions = currentPermissions.withRemoved(params.getRemovePermissions());
        } else {
            newPermissions = null;
        }
        Permissions rejectPermissions = Permissions.fromSet(params.getRejectPermissions());
        byte[] userPassword = reader.computeUserPassword();
        int cryptoMode = reader.getCryptoMode();
        if (LOG.isDebugEnabled()) {
            StringBuilder buff = new StringBuilder();
            buff.append("Current permissions: ").append(currentPermissions).append("\n").append("Remove permissions: ").append(params.getRemovePermissions()).append("\n").append("Reject permissions: ").append(rejectPermissions).append("\n").append("New permissions: ").append(newPermissions).append("\n").append("userPassword: ").append(userPassword == null ? "null" : "yes").append("\n").append("ownerPassword: ").append(password == null ? "no" : (isUserPassword(reader, password) ? "no" : "yes")).append("\n").append("setOwnerPassword: ").append(params.getSetOwnerPassword() == null ? "no" : "yes").append("\n").append("cryptoMode: ").append(cryptoMode);
            LOG.debug(buff.toString());
        }
        if (appendMode && (newPermissions != null || params.getSetOwnerPassword() != null)) {
            appendMode = false;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Changing appendMode to false to be able to change permissions");
            }
        }
        ByteArrayOutputStream fout = new ByteArrayOutputStream();
        PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0', null, appendMode);
        PdfSignatureAppearance sap = stp.getSignatureAppearance();
        if (newPermissions != null || params.getSetOwnerPassword() != null) {
            if (cryptoMode < 0) {
                cryptoMode = PdfWriter.STANDARD_ENCRYPTION_128;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Setting default encryption algorithm");
                }
            }
            if (newPermissions == null) {
                newPermissions = currentPermissions;
            }
            if (params.getSetOwnerPassword() != null) {
                password = params.getSetOwnerPassword().getBytes("ISO-8859-1");
            } else if (isUserPassword(reader, password)) {
                password = new byte[16];
                random.nextBytes(password);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Setting random owner password");
                }
            }
            stp.setEncryption(userPassword, password, newPermissions.asInt(), cryptoMode);
            currentPermissions = newPermissions;
        }
        if (rejectPermissions.asInt() != 0) {
            if (cryptoMode < 0 || currentPermissions.containsAnyOf(rejectPermissions)) {
                throw new IllegalRequestException("Document contains permissions not allowed by this signer");
            }
        }
        CRL[] crlList = null;
        if (params.isEmbed_crl()) {
            crlList = getCrlsForChain(this.getSigningCertificateChain());
        }
        sap.setCrypto(null, certChain, crlList, PdfSignatureAppearance.SELF_SIGNED);
        if (params.isAdd_visible_signature()) {
            int signaturePage = getPageNumberForSignature(reader, params);
            sap.setVisibleSignature(new com.lowagie.text.Rectangle(params.getVisible_sig_rectangle_llx(), params.getVisible_sig_rectangle_lly(), params.getVisible_sig_rectangle_urx(), params.getVisible_sig_rectangle_ury()), signaturePage, null);
            if (params.isUse_custom_image()) {
                sap.setAcro6Layers(true);
                PdfTemplate n2 = sap.getLayer(2);
                params.getCustom_image().setAbsolutePosition(0, 0);
                n2.addImage(params.getCustom_image());
            }
        }
        sap.setCertificationLevel(params.getCertification_level());
        PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
        dic.setReason(params.getReason());
        dic.setLocation(params.getLocation());
        dic.setDate(new PdfDate(Calendar.getInstance()));
        sap.setCryptoDictionary(dic);
        TSAClient tsc = null;
        if (params.isUse_timestamp()) {
            tsc = new TSAClientBouncyCastle(params.getTsa_url(), params.getTsa_username(), params.getTsa_password());
        }
        int contentEstimated = 15000;
        HashMap exc = new HashMap();
        exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
        sap.preClose(exc);
        PdfPKCS7 sgn;
        try {
            sgn = new PdfPKCS7(privKey, certChain, crlList, "SHA1", null, false);
        } catch (InvalidKeyException e) {
            throw new SignServerException("Error constructing PKCS7 package", e);
        } catch (NoSuchProviderException e) {
            throw new SignServerException("Error constructing PKCS7 package", e);
        } catch (NoSuchAlgorithmException e) {
            throw new SignServerException("Error constructing PKCS7 package", e);
        }
        InputStream data = sap.getRangeStream();
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new SignServerException("Error creating SHA1 digest", e);
        }
        byte buf[] = new byte[8192];
        int n;
        while ((n = data.read(buf)) > 0) {
            messageDigest.update(buf, 0, n);
        }
        byte hash[] = messageDigest.digest();
        Calendar cal = Calendar.getInstance();
        byte[] ocsp = null;
        if (params.isEmbed_ocsp_response() && certChain.length >= 2) {
            String url;
            try {
                url = PdfPKCS7.getOCSPURL((X509Certificate) certChain[0]);
                if (url != null && url.length() > 0) {
                    ocsp = new OcspClientBouncyCastle((X509Certificate) certChain[0], (X509Certificate) certChain[1], url).getEncoded();
                }
            } catch (CertificateParsingException e) {
                throw new SignServerException("Error getting OCSP URL from certificate", e);
            }
        }
        byte sh[] = sgn.getAuthenticatedAttributeBytes(hash, cal, ocsp);
        try {
            sgn.update(sh, 0, sh.length);
        } catch (SignatureException e) {
            throw new SignServerException("Error calculating signature", e);
        }
        byte[] encodedSig = sgn.getEncodedPKCS7(hash, cal, tsc, ocsp);
        if (contentEstimated + 2 < encodedSig.length) {
            throw new SignServerException("Not enough space");
        }
        byte[] paddedSig = new byte[contentEstimated];
        System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
        PdfDictionary dic2 = new PdfDictionary();
        dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
        sap.close(dic2);
        reader.close();
        fout.close();
        return fout.toByteArray();
    }
