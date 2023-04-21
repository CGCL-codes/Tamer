    public static void main(String args[]) {
        if (args.length != 5) {
            System.err.println("usage: SendSignedAndEncryptedMail <pkcs12Keystore> <password> <keyalias> <smtp server> <email address>");
            System.exit(0);
        }
        try {
            MailcapCommandMap mailcap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mailcap.addMailcap("application/pkcs7-signature;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_signature");
            mailcap.addMailcap("application/pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.pkcs7_mime");
            mailcap.addMailcap("application/x-pkcs7-signature;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_signature");
            mailcap.addMailcap("application/x-pkcs7-mime;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.x_pkcs7_mime");
            mailcap.addMailcap("multipart/signed;; x-java-content-handler=org.bouncycastle.mail.smime.handlers.multipart_signed");
            CommandMap.setDefaultCommandMap(mailcap);
            Security.addProvider(new BouncyCastleProvider());
            KeyStore keystore = KeyStore.getInstance("PKCS12", "BC");
            keystore.load(new FileInputStream(args[0]), args[1].toCharArray());
            Certificate[] chain = keystore.getCertificateChain(args[2]);
            PrivateKey privateKey = (PrivateKey) keystore.getKey(args[2], args[1].toCharArray());
            if (privateKey == null) {
                throw new Exception("cannot find private key for alias: " + args[2]);
            }
            Properties props = System.getProperties();
            props.put("mail.smtp.host", args[3]);
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage body = new MimeMessage(session);
            body.setFrom(new InternetAddress(args[4]));
            body.setRecipient(Message.RecipientType.TO, new InternetAddress(args[4]));
            body.setSubject("example encrypted message");
            body.setContent("example encrypted message", "text/plain");
            body.saveChanges();
            SMIMECapabilityVector capabilities = new SMIMECapabilityVector();
            capabilities.addCapability(SMIMECapability.dES_EDE3_CBC);
            capabilities.addCapability(SMIMECapability.rC2_CBC, 128);
            capabilities.addCapability(SMIMECapability.dES_CBC);
            ASN1EncodableVector attributes = new ASN1EncodableVector();
            attributes.add(new SMIMEEncryptionKeyPreferenceAttribute(new IssuerAndSerialNumber(new X509Name(((X509Certificate) chain[0]).getIssuerDN().getName()), ((X509Certificate) chain[0]).getSerialNumber())));
            attributes.add(new SMIMECapabilitiesAttribute(capabilities));
            SMIMESignedGenerator signer = new SMIMESignedGenerator();
            signer.addSigner(privateKey, (X509Certificate) chain[0], "DSA".equals(privateKey.getAlgorithm()) ? SMIMESignedGenerator.DIGEST_SHA1 : SMIMESignedGenerator.DIGEST_MD5, new AttributeTable(attributes), null);
            List certList = new ArrayList();
            certList.add(chain[0]);
            CertStore certs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
            signer.addCertificatesAndCRLs(certs);
            MimeMultipart mm = signer.generate(body, "BC");
            MimeMessage signedMessage = new MimeMessage(session);
            Enumeration headers = body.getAllHeaderLines();
            while (headers.hasMoreElements()) {
                signedMessage.addHeaderLine((String) headers.nextElement());
            }
            signedMessage.setContent(mm);
            signedMessage.saveChanges();
            SMIMEEnvelopedGenerator encrypter = new SMIMEEnvelopedGenerator();
            encrypter.addKeyTransRecipient((X509Certificate) chain[0]);
            MimeBodyPart encryptedPart = encrypter.generate(signedMessage, SMIMEEnvelopedGenerator.RC2_CBC, "BC");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            encryptedPart.writeTo(out);
            MimeMessage encryptedMessage = new MimeMessage(session, new ByteArrayInputStream(out.toByteArray()));
            headers = body.getAllHeaderLines();
            while (headers.hasMoreElements()) {
                String headerLine = (String) headers.nextElement();
                if (!Strings.toLowerCase(headerLine).startsWith("content-")) {
                    encryptedMessage.addHeaderLine(headerLine);
                }
            }
            Transport.send(encryptedMessage);
        } catch (SMIMEException ex) {
            ex.getUnderlyingException().printStackTrace(System.err);
            ex.printStackTrace(System.err);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
