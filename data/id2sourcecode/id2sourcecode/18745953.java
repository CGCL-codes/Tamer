    private byte[] singleSign(final byte[] data, final Alias alias) {
        Assert.notEmpty(data, "data");
        try {
            Store store = this.parameters.getStore();
            PrivateKeyEntry privateEntry = (PrivateKeyEntry) store.get(alias, StoreEntryType.PRIVATE_KEY);
            if (privateEntry == null) {
                throw new SignerException("Private key '" + alias.getName() + " not found in store");
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrivateKey privateKey = privateEntry.getValue();
            Certificate[] chain = privateEntry.getChain();
            X509Certificate certificate = (X509Certificate) chain[0];
            DigestType digestType = this.getDigestTypeFromSignature(certificate.getSigAlgName());
            Calendar calendar = Calendar.getInstance();
            PdfReader reader = new PdfReader(data);
            PdfStamper stamper = PdfStamper.createSignature(reader, outputStream, PDFSigner.PDF_SIGNATURE_VERSION, null, true);
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setCrypto(privateKey, chain, null, PdfSignatureAppearance.SELF_SIGNED);
            appearance.setContact(this.parameters.getContactInfo());
            appearance.setLocation(this.parameters.getLocation());
            appearance.setReason(this.parameters.getReason());
            appearance.setSignDate(calendar);
            PdfSignature signature = new PdfSignature(PdfName.ADOBE_PPKLITE, PdfName.ADBE_PKCS7_DETACHED);
            signature.setReason(appearance.getReason());
            signature.setLocation(appearance.getLocation());
            signature.setContact(appearance.getContact());
            signature.setDate(new PdfDate(appearance.getSignDate()));
            if (ConditionUtils.isNotEmpty(this.parameters.getName())) {
                signature.setName(this.parameters.getName());
            } else {
                signature.setName(BouncyCastleProviderHelper.getName(certificate.getSubjectX500Principal()));
            }
            appearance.setCryptoDictionary(signature);
            int contentSize = 0x2502;
            HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
            exc.put(PdfName.CONTENTS, new Integer(contentSize));
            appearance.preClose(exc);
            Digester digester = new BasicDigester(digestType);
            byte[] rangeStream = IOUtils.toByteArray(appearance.getRangeStream());
            byte[] hash = digester.digest(rangeStream);
            TSAClient tsc = null;
            if (this.parameters.getTimeStampClient() != null) {
                tsc = new DelegateITextTSAClient(this.parameters.getTimeStampClient());
            }
            byte[] oscp = null;
            if (ConditionUtils.isNotEmpty(chain)) {
                String oscpUrl = PdfPKCS7.getOCSPURL(certificate);
                X509Certificate parentCertificate = null;
                for (Certificate c : chain) {
                    if (!certificate.equals(c)) {
                        parentCertificate = (X509Certificate) c;
                        break;
                    }
                }
                if (parentCertificate != null) {
                    if ((oscpUrl != null) && (oscpUrl.trim().length() > 0)) {
                        OcspClient ocspClient = new OcspClientBouncyCastle(certificate, parentCertificate, oscpUrl);
                        oscp = ocspClient.getEncoded();
                    }
                }
            }
            PdfPKCS7 pkcs7 = new PdfPKCS7(privateKey, chain, null, digestType.getAlgorithm(), null, false);
            byte[] authenticatedAttributes = pkcs7.getAuthenticatedAttributeBytes(hash, calendar, oscp);
            pkcs7.update(authenticatedAttributes, 0, authenticatedAttributes.length);
            pkcs7.setLocation(this.parameters.getLocation());
            pkcs7.setReason(this.parameters.getReason());
            if (tsc == null) {
                pkcs7.setSignDate(calendar);
            }
            byte[] encodedPkcs7 = pkcs7.getEncodedPKCS7(hash, calendar, tsc, oscp);
            byte[] output = new byte[(contentSize - 2) / 2];
            System.arraycopy(encodedPkcs7, 0, output, 0, encodedPkcs7.length);
            PdfDictionary newDictionary = new PdfDictionary();
            PdfString content = new PdfString(output);
            content.setHexWriting(true);
            newDictionary.put(PdfName.CONTENTS, content);
            appearance.close(newDictionary);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new SignerException(e);
        }
    }
