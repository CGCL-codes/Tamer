    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("usage: main <cert_file_name>");
            System.exit(1);
        }
        System.out.println("Creating server CA test certificate");
        Security.addProvider(new IAIK());
        KeyPair kp = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "IAIK");
            generator.initialize(512);
            kp = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Can't create RSA/512 key pair: " + e);
            System.exit(1);
        } catch (NoSuchProviderException e) {
            System.out.println("Can't create RSA/512 key pair: " + e);
            System.exit(1);
        }
        Name issuer = new Name();
        issuer.addRDN(ObjectID.country, getEntry("country", "US"));
        issuer.addRDN(ObjectID.organization, getEntry("company", "Sun Microsystems Laboratories"));
        issuer.addRDN(ObjectID.organizationalUnit, getEntry("division", "Brazil Project"));
        issuer.addRDN(ObjectID.commonName, getEntry("server name", "foo.bar.com"));
        X509Certificate cert = new X509Certificate();
        try {
            cert.setSerialNumber(new BigInteger(20, new Random()));
            cert.setSubjectDN(issuer);
            cert.setIssuerDN(issuer);
            cert.setPublicKey(kp.getPublic());
            GregorianCalendar date = new GregorianCalendar();
            date.add(Calendar.DATE, -1);
            cert.setValidNotBefore(date.getTime());
            date.add(Calendar.MONTH, Integer.parseInt(getEntry("time of validity (months)", "6")));
            cert.setValidNotAfter(date.getTime());
            cert.addExtension(new NetscapeCertType(NetscapeCertType.SSL_CA | NetscapeCertType.SSL_SERVER | NetscapeCertType.S_MIME_CA | NetscapeCertType.OBJECT_SIGNING_CA));
            cert.addExtension(new NetscapeSSLServerName(getEntry("host name of server", "*.eng.sun.com")));
            String comment = getEntry("A comment for the certificate user", "");
            if (!comment.equals("")) {
                cert.addExtension(new NetscapeComment(comment));
            }
            cert.sign(AlgorithmID.md5WithRSAEncryption, kp.getPrivate());
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = cert;
            EncryptedPrivateKeyInfo epki = new EncryptedPrivateKeyInfo((PrivateKeyInfo) kp.getPrivate());
            epki.encrypt(getPassword("Certificate password"), AlgorithmID.pbeWithMD5AndDES_CBC, null);
            new KeyAndCertificate(epki, chain).saveTo(args[0], ASN1.PEM);
        } catch (Exception e) {
            System.out.println("OOPS: " + e);
            e.printStackTrace();
        }
        System.out.println("Saved server CA test certificate to: " + args[0]);
    }
