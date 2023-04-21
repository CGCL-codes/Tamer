    public DrivingLicense(File file, boolean allowInconsistent, DocumentSigner signer) throws IOException {
        this.signer = signer;
        ZipFile zipIn = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipIn.entries();
        List<ZipEntry> entryList = new ArrayList<ZipEntry>();
        boolean generateaa = false;
        boolean generateca = false;
        String signatureAlgorithm = "SHA256withRSA";
        X509Certificate docCertificate = null;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String fileName = entry.getName();
            if (fileName.equals("keyseed.bin")) {
                int size = (int) (entry.getSize() & 0x00000000FFFFFFFFL);
                if (size != 16) {
                    throw new IOException("Wrong key seed length in " + file);
                }
                keySeed = new byte[16];
                DataInputStream dataIn = new DataInputStream(zipIn.getInputStream(entry));
                dataIn.readFully(keySeed);
            } else if (fileName.equals("generateaa.key")) {
                generateaa = true;
            } else if (fileName.equals("generateca.key")) {
                generateca = true;
            } else if (fileName.equals("aaprivatekey.der")) {
                int size = (int) (entry.getSize() & 0x00000000FFFFFFFFL);
                byte[] keyData = new byte[size];
                DataInputStream dataIn = new DataInputStream(zipIn.getInputStream(entry));
                dataIn.readFully(keyData);
                try {
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    aaPrivateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(keyData));
                } catch (Exception ex) {
                    throw new IOException("Invalid RSA private key: " + ex.getMessage());
                }
            } else if (fileName.equals("caprivatekey.der")) {
                int size = (int) (entry.getSize() & 0x00000000FFFFFFFFL);
                byte[] keyData = new byte[size];
                DataInputStream dataIn = new DataInputStream(zipIn.getInputStream(entry));
                dataIn.readFully(keyData);
                try {
                    KeyFactory kf = KeyFactory.getInstance("ECDH");
                    eapPrivateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(keyData));
                } catch (Exception ex) {
                    throw new IOException("Invalid ECDH private key: " + ex.getMessage());
                }
            } else if (fileName.equals("cacert.cvcert")) {
                int size = (int) (entry.getSize() & 0x00000000FFFFFFFFL);
                byte[] data = new byte[size];
                DataInputStream dataIn = new DataInputStream(zipIn.getInputStream(entry));
                dataIn.readFully(data);
                try {
                    cvcaCertificate = (CVCertificate) CertificateParser.parseCertificate(data);
                } catch (Exception ex) {
                    throw new IOException("Invalid CVCA certificate: " + ex.getMessage());
                }
            } else if (fileName.equals("doccert.der")) {
                int size = (int) (entry.getSize() & 0x00000000FFFFFFFFL);
                byte[] data = new byte[size];
                DataInputStream dataIn = new DataInputStream(zipIn.getInputStream(entry));
                dataIn.readFully(data);
                try {
                    docCertificate = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(data));
                    signer.setCertificate(docCertificate);
                } catch (Exception ex) {
                    throw new IOException("Invalid doc certificate: " + ex.getMessage());
                }
            } else if (fileName.equals("001D.bin") || fileName.equals("001E.bin")) {
                updateCOMSODfiles = false;
                entryList.add(entry);
            } else {
                entryList.add(entry);
            }
        }
        if (docCertificate == null && updateCOMSODfiles) {
            docCertificate = generateDummyCertificate(signatureAlgorithm);
        }
        for (ZipEntry entry : entryList) {
            String fileName = entry.getName();
            int size = (int) (entry.getSize() & 0x00000000FFFFFFFFL);
            try {
                boolean eapProtection = fileName.indexOf("eap") != -1;
                int delimIndex = eapProtection ? fileName.indexOf("eap") : fileName.indexOf('.');
                if (delimIndex != 4) {
                    System.out.println("DEBUG: skipping file " + fileName + "(delimIndex == " + delimIndex + ")");
                    continue;
                }
                short fid = Hex.hexStringToShort(fileName.substring(0, delimIndex));
                if (cvcaCertificate == null) {
                    eapProtection = false;
                }
                byte[] bytes = new byte[size];
                int fileLength = bytes.length;
                fileLengths.put(fid, fileLength);
                DataInputStream dataIn = new DataInputStream(zipIn.getInputStream(entry));
                dataIn.readFully(bytes);
                rawStreams.put(fid, new ByteArrayInputStream(bytes));
                eapFlags.put(fid, eapProtection);
                totalLength += fileLength;
                if (fid == DrivingLicenseService.EF_COM) {
                    comFile = new COMFile(new ByteArrayInputStream(bytes));
                } else if (fid == DrivingLicenseService.EF_SOD) {
                    sodFile = new SODFile(new ByteArrayInputStream(bytes));
                }
            } catch (IOException ioe) {
            } catch (NumberFormatException nfe) {
            }
        }
        try {
            if (updateCOMSODfiles && docCertificate == null) {
                throw new IOException("No SOD and no doc certificate provided.");
            }
            if (generateaa && aaPrivateKey != null) {
                throw new IOException("Both AA private key and request to generate one present.");
            }
            if (generateca && eapPrivateKey != null) {
                throw new IOException("Both CA private key and request to generate one present.");
            }
            if ((generateaa || generateca) && !updateCOMSODfiles) {
                throw new IOException("COM or SOD file present and request to generate private AA or CA keys.");
            }
            if (getFileList().contains(DrivingLicenseService.EF_DG13) && aaPrivateKey == null) {
                throw new IOException("DG13 present, but no AA private key.");
            }
            if (getFileList().contains(DrivingLicenseService.EF_DG14) && eapPrivateKey == null) {
                throw new IOException("DG14 present, but no CA private key.");
            }
        } catch (IOException ioe) {
            if (allowInconsistent) {
                updateCOMSODfiles = true;
                ioe.printStackTrace();
            } else {
                throw ioe;
            }
        }
        try {
            if (generateaa) {
                Provider provider = Security.getProvider("BC");
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", provider);
                generator.initialize(new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4));
                setAAKeys(generator.generateKeyPair());
            }
            if (generateca) {
                String preferredProvider = "BC";
                Provider provider = Security.getProvider(preferredProvider);
                KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDH", provider);
                generator.initialize(new ECGenParameterSpec(DrivingLicensePersoService.EC_CURVE_NAME));
                setEAPKeys(generator.generateKeyPair());
            }
            if (comFile == null) {
                comFile = new COMFile(1, 0, new ArrayList<Integer>(), null);
            }
            if (sodFile == null) {
                String digestAlgorithm = "SHA256";
                Map<Integer, byte[]> hashes = new HashMap<Integer, byte[]>();
                MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
                hashes.put(1, digest.digest(new byte[0]));
                hashes.put(2, digest.digest(new byte[0]));
                sodFile = new SODFile(digestAlgorithm, signatureAlgorithm, hashes, this.signer, docCertificate);
            }
            updateCOMSODFile(docCertificate);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("Key generation failed: " + ex.getMessage());
        }
    }
