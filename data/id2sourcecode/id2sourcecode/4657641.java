    public SecureEventHeap(String sourceName, String machine, int port, SecureEventHeap oldHeap, String deviceName, String applicationName) {
        boolean debug = false;
        synchronized (staticLock) {
            if (firstOne) {
                try {
                    if (debug) System.out.println("THis is the FIRST ONE!!! WOOHOO!!!");
                    firstOne = false;
                    Security.addProvider(new com.sun.crypto.provider.SunJCE());
                    Security.insertProviderAt(new BouncyCastleProvider(), 2);
                    KeyPairGenerator kg = KeyPairGenerator.getInstance("DSA");
                    kg.initialize(1024);
                    KeyPair kp = kg.generateKeyPair();
                    ks = KeyStore.getInstance("JKS");
                    ks.load(new FileInputStream("default.ks"), "iSec123".toCharArray());
                    if (debug) System.out.println("ks loaded");
                    X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
                    certGen.reset();
                    certGen.setSubjectDN(new X509Principal("CN=" + getApplicationName()));
                    certGen.setIssuerDN(new X509Principal("CN=" + sourceName + ", " + "OU=" + applicationName + ", " + "O=" + deviceName));
                    certGen.setPublicKey(kp.getPublic());
                    certGen.setSerialNumber(new BigInteger(128, new SecureRandom()));
                    certGen.setSignatureAlgorithm("SHA1withDSA");
                    GregorianCalendar date = new GregorianCalendar();
                    date.add(Calendar.DATE, -1);
                    certGen.setNotBefore(date.getTime());
                    date.add(Calendar.DATE, 1);
                    date.add(Calendar.HOUR_OF_DAY, 24);
                    certGen.setNotAfter(date.getTime());
                    if (debug) System.out.println("cert generator initialized");
                    java.security.cert.Certificate selfcert = certGen.generateX509Certificate(kp.getPrivate());
                    Socket sock = new Socket("localhost", 2000);
                    (new ObjectOutputStream(sock.getOutputStream())).writeObject(selfcert);
                    ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                    Object receivedObject;
                    java.security.cert.Certificate[] certs = null;
                    while ((receivedObject = ois.readObject()) != null) {
                        if (receivedObject instanceof java.security.cert.Certificate[]) {
                            certs = (java.security.cert.Certificate[]) receivedObject;
                            if (debug) System.out.println("Certificate Chain Received!");
                            if (debug) System.out.println(certs[0]);
                            if (debug) System.out.println(certs[1]);
                            break;
                        } else {
                            if (debug) System.out.println(receivedObject.toString());
                            break;
                        }
                    }
                    sock.close();
                    ks.setKeyEntry("default", kp.getPrivate(), "iSec123".toCharArray(), certs);
                    ks.store(new FileOutputStream("default.ks"), "iSec123".toCharArray());
                    synchronized (instanceLock) {
                        String DN = ((X509Certificate) certs[1]).getSubjectDN().toString();
                        person = DN.substring(3, DN.indexOf(','));
                    }
                    if (debug) System.out.println("ks is " + ks);
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    kmf = KeyManagerFactory.getInstance("SunX509");
                    kmf.init(ks, "iSec123".toCharArray());
                    KeyManager kms[] = kmf.getKeyManagers();
                    if (debug) System.out.println("kmf has " + kms.length + " key managers... ");
                    String aliases[] = ((X509KeyManager) kms[0]).getClientAliases("DSA", null);
                    if (debug) System.out.println("kms[0] has " + aliases.length + " aliases");
                    for (int i = 0; i < aliases.length; i++) {
                        if (debug) System.out.println("\t\t" + i + ": " + aliases[i]);
                        java.security.cert.Certificate tempcerts[] = ((X509KeyManager) kms[0]).getCertificateChain(aliases[i]);
                        for (int j = 0; j < tempcerts.length; j++) {
                            if (debug) System.out.println("\t\t\t cert " + j + ":" + tempcerts[j]);
                        }
                        if (debug) System.out.println("--- end of certs for alias ---" + aliases[i] + "\n\n\n\n");
                    }
                    tmf = TrustManagerFactory.getInstance("SunX509");
                    tmf.init(ks);
                    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                    socketFactory = sslContext.getSocketFactory();
                    if (debug) System.out.println("\n\n ----- Certificate verification -----");
                    java.security.cert.Certificate checkcerts[] = ks.getCertificateChain("default");
                    for (int i = 0; i < checkcerts.length; i++) {
                        if (debug) System.out.println("cert number " + i + " is " + checkcerts[i]);
                    }
                } catch (Exception e) {
                    if (debug) System.out.println("Error in SecureEventHeap constructor: " + e.toString());
                }
            }
        }
        synchronized (staticLock) {
            if (device == null) {
                if (deviceName != null) device = deviceName; else {
                    createDeviceName();
                }
            }
            if (application == null) {
                if (applicationName != null) application = applicationName; else {
                    createApplicationName();
                }
            }
        }
        if (sourceName != null && oldHeap != null) {
            machine = oldHeap.machine;
            port = oldHeap.port;
        }
        this.machine = machine;
        this.port = port;
        if (this.port == -1) this.port = EHEAP2_DEFAULT_SECURE_PORT;
        try {
            theServer = connect(this.machine, this.port);
        } catch (EventHeapException e) {
            System.out.println("exception caught when secure event heap trying to connect to server: \n" + e.toString());
            e.printStackTrace();
        }
        sessionID = new Integer((int) (Math.random() * (double) Integer.MAX_VALUE));
        if (sourceName == null) synchronized (staticLock) {
            source = application;
        } else source = sourceName;
        source += "_" + (int) (Math.random() * (double) Integer.MAX_VALUE);
    }
