    @Override
    public PrivateKeyHolder build() {
        try {
            long time = System.currentTimeMillis();
            String subject = this.subject;
            KeyPair keyPair = this.keyPair;
            SignatureType signType = this.signType;
            String issuer = this.issuerName;
            BigInteger serialNumber = this.serialNumber;
            Date notBefore = this.notBefore;
            Date notAfter = this.notAfter;
            X509Certificate certificate = null;
            PrivateKey privateKey = null;
            if (keyPair == null) {
                keyPair = KeyPairGenerator.getInstance(KeyPairType.RSA.getAlgorithm()).generateKeyPair();
            }
            if (signType == null) {
                signType = SignatureType.SHA1_RSA;
            }
            if (issuer == null) {
                issuer = BouncyCastleCertificateBuilder.DEFAULT_ISSUER;
            }
            if (serialNumber == null) {
                serialNumber = BigInteger.valueOf(time);
            }
            if (notBefore == null) {
                DateBuilder dateBuilder = new DateBuilder(time);
                dateBuilder.removeDays(1);
                notBefore = dateBuilder.getDate();
            }
            if (notAfter == null) {
                DateBuilder dateBuilder = new DateBuilder(time);
                dateBuilder.addYears(1);
                notAfter = dateBuilder.getDate();
            }
            if (this.v3) {
                JcaX509v3CertificateBuilder builder = null;
                if (this.issuerCertificate != null) {
                    builder = new JcaX509v3CertificateBuilder((X509Certificate) this.issuerCertificate, serialNumber, notBefore, notAfter, BouncyCastleProviderHelper.toX500Principal(subject), keyPair.getPublic());
                } else {
                    builder = new JcaX509v3CertificateBuilder(BouncyCastleProviderHelper.toX500Name(issuer), serialNumber, notBefore, notAfter, BouncyCastleProviderHelper.toX500Name(subject), keyPair.getPublic());
                }
                JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(signType.getAlgorithm());
                contentSignerBuilder.setProvider(BouncyCastleProviderHelper.PROVIDER_NAME);
                ContentSigner contentSigner = contentSignerBuilder.build(keyPair.getPrivate());
                if (this.keyUsage.size() > 0) {
                    int usage = 0;
                    for (KeyUsageType keyUsage : this.keyUsage) {
                        usage = usage | this.toKeyUsage(keyUsage);
                    }
                    org.bouncycastle.asn1.x509.KeyUsage ku = new org.bouncycastle.asn1.x509.KeyUsage(usage);
                    builder.addExtension(X509Extension.keyUsage, false, ku);
                }
                if (this.extendedKeyUsage.size() > 0) {
                    Vector<DERObject> vector = new Vector<DERObject>();
                    for (ExtendedKeyUsageType keyUsageType : this.extendedKeyUsage) {
                        KeyPurposeId keyPurposeId = this.toExtendedKeyUsage(keyUsageType);
                        if (keyPurposeId != null) {
                            vector.add(keyPurposeId);
                        }
                    }
                    if (vector.size() > 0) {
                        org.bouncycastle.asn1.x509.ExtendedKeyUsage extendedKeyUsage = new org.bouncycastle.asn1.x509.ExtendedKeyUsage(vector);
                        builder.addExtension(X509Extension.extendedKeyUsage, true, extendedKeyUsage);
                    } else {
                        org.bouncycastle.asn1.x509.ExtendedKeyUsage extendedKeyUsage = new org.bouncycastle.asn1.x509.ExtendedKeyUsage(KeyPurposeId.anyExtendedKeyUsage);
                        builder.addExtension(X509Extension.extendedKeyUsage, false, extendedKeyUsage);
                    }
                } else {
                    org.bouncycastle.asn1.x509.ExtendedKeyUsage extendedKeyUsage = new org.bouncycastle.asn1.x509.ExtendedKeyUsage(KeyPurposeId.anyExtendedKeyUsage);
                    builder.addExtension(X509Extension.extendedKeyUsage, false, extendedKeyUsage);
                }
                GeneralNames subjectAltName = new GeneralNames(new GeneralName(GeneralName.rfc822Name, subject));
                builder.addExtension(X509Extension.subjectAlternativeName, false, subjectAltName);
                SubjectKeyIdentifierStructure subjectKeyIdentifierStructure = new SubjectKeyIdentifierStructure(keyPair.getPublic());
                builder.addExtension(X509Extension.subjectKeyIdentifier, false, subjectKeyIdentifierStructure);
                X509CertificateHolder holder = builder.build(contentSigner);
                certificate = (X509Certificate) SecurityUtils.getCertificateFromFile(holder.getEncoded(), CertificateType.X509);
                privateKey = keyPair.getPrivate();
            } else {
                JcaX509v1CertificateBuilder builder = new JcaX509v1CertificateBuilder(BouncyCastleProviderHelper.toX500Name(issuer), serialNumber, notBefore, notAfter, BouncyCastleProviderHelper.toX500Name(subject), keyPair.getPublic());
                JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder(signType.getAlgorithm());
                contentSignerBuilder.setProvider(BouncyCastleProviderHelper.PROVIDER_NAME);
                ContentSigner contentSigner = contentSignerBuilder.build(keyPair.getPrivate());
                X509CertificateHolder holder = builder.build(contentSigner);
                certificate = (X509Certificate) SecurityUtils.getCertificateFromFile(holder.getEncoded(), CertificateType.X509);
                privateKey = keyPair.getPrivate();
            }
            PrivateKeyHolder privateKeyHolder = new PrivateKeyHolder(privateKey, new Certificate[] { certificate });
            return privateKeyHolder;
        } catch (Exception e) {
            throw new CertificateException(e);
        }
    }
