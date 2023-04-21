    @Override
    public String decrypt(HashMap credentialsMap, String input) throws SkceWSException {
        String base64key = null;
        String token = null;
        boolean USE_ZIPINPUTSTREAM = false;
        ByteArrayInputStream bais = null;
        BufferedInputStream ctis = null;
        BufferedInputStream xmlis = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        ZipInputStream zis = null;
        String algorithm = null;
        String transform = null;
        String hosturl = null;
        String olddigest = null;
        String digesttype = null;
        int xmlfilesize = 0;
        String ctxtfilename = null;
        ZipEntry entry = null;
        String entryname = null;
        byte[] xmldoc = null;
        SecretKey sk = null;
        SecretKeyObject sko = null;
        byte[] ivbytes = null;
        IvParameterSpec ivspec = null;
        Cipher cipher = null;
        try {
            File inpf = new File(input);
            if (!inpf.exists()) {
                Common.log(Level.SEVERE, "SKCE-ERR-4001", input);
                throw new SkceWSException("No such file: " + input);
            } else if (!inpf.isFile()) {
                Common.log(Level.SEVERE, "SKCE-ERR-4002", input);
                throw new SkceWSException("Not a file: " + input);
            } else if (!inpf.canRead()) {
                Common.log(Level.SEVERE, "SKCE-ERR-4003", input);
                throw new SkceWSException("Not a readable file: " + input);
            } else if (inpf.length() <= 0) {
                Common.log(Level.SEVERE, "SKCE-ERR-4004", "HOLA" + input);
                throw new SkceWSException("No data in file: " + input);
            }
            fis = new FileInputStream(input);
            Common.log(Level.INFO, "SKCE-MSG-4002", input);
            if (inpf.length() >= CUTOFF_SIZE) {
                USE_ZIPINPUTSTREAM = true;
                Common.log(Level.INFO, "SKCE-MSG-4003", new long[] { inpf.length(), CUTOFF_SIZE });
            } else {
                Common.log(Level.INFO, "SKCE-MSG-4004", new long[] { inpf.length(), CUTOFF_SIZE });
            }
            if (USE_ZIPINPUTSTREAM) {
                zis = new ZipInputStream(new BufferedInputStream(fis, BUFFER_SIZE));
                while ((entry = zis.getNextEntry()) != null) {
                    entryname = entry.getName();
                    if (entryname.endsWith(XMLENC_FILE_EXTENSION)) {
                        Common.log(Level.INFO, "SKCE-MSG-4005", entryname);
                        int count;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
                        byte[] data = new byte[BUFFER_SIZE];
                        while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
                            baos.write(data, 0, count);
                        }
                        xmldoc = baos.toByteArray();
                        break;
                    }
                }
                bais = new ByteArrayInputStream(xmldoc);
                Common.log(Level.INFO, "SKCE-MSG-4006", new String[] { Integer.toString(xmldoc.length), input });
                zis.close();
                fis.close();
            } else {
                ZipFile zipfile = new ZipFile(input);
                Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipfile.entries();
                while (entries.hasMoreElements()) {
                    entry = entries.nextElement();
                    if (entry.getName().endsWith(XMLENC_FILE_EXTENSION)) {
                        xmlfilesize = (int) entry.getSize();
                        xmlis = new BufferedInputStream(zipfile.getInputStream(entry), BUFFER_SIZE);
                    } else if (entry.getName().endsWith(CIPHERTEXT_FILE_EXTENSION)) {
                        ctxtfilename = entry.getName();
                        ctis = new BufferedInputStream(zipfile.getInputStream(entry), BUFFER_SIZE);
                    }
                }
                xmldoc = new byte[(int) xmlfilesize];
                int n = xmlis.read(xmldoc);
                bais = new ByteArrayInputStream(xmldoc);
                Common.log(Level.INFO, "SKCE-MSG-4006", new String[] { Integer.toString(n), input });
                fis.close();
            }
            Common.log(Level.FINE, "SKCE-MSG-5000", "DocumentBuilderFactory()");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(bais);
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new SKCENamespaceContext());
            InputSource is = new InputSource(bais);
            String did = getXPathElement(bais, is, xpath, "//skles:DomainId");
            if (did == null) {
                Common.log(Level.SEVERE, "SKCE-ERR-4011", "");
                throw new SkceWSException("SKCE-ERR-4011: Invalid input - could not find DomainID in XML file");
            }
            Common.log(Level.INFO, "SKCE-MSG-4008", did);
            Domain domain = null;
            if (credentialsMap != null) domain = (Domain) credentialsMap.get(did);
            if (domain != null) {
                String username = domain.getUsername();
                String password = domain.getPassword();
                algorithm = getXPathAttribute(bais, is, xpath, "//xenc:EncryptionMethod/@Algorithm");
                Common.log(Level.INFO, "SKCE-MSG-4007", algorithm);
                if (algorithm == null) {
                    algorithm = DEFAULT_ALGORITHM;
                    transform = DEFAULT_TRANSFORM;
                    ivbytes = new byte[DEFAULT_IV_SIZE];
                } else if (algorithm.startsWith("http://www.w3.org/2001/04/xmlenc#aes")) {
                    cipher = Cipher.getInstance(AES_TRANSFORM);
                    transform = AES_TRANSFORM;
                    ivbytes = new byte[16];
                } else if (algorithm.equalsIgnoreCase("http://www.w3.org/2001/04/xmlenc#tripledes-cbc")) {
                    cipher = Cipher.getInstance(DESEDE_TRANSFORM);
                    transform = DESEDE_TRANSFORM;
                    ivbytes = new byte[8];
                } else {
                    Common.log(Level.SEVERE, "SKCE-ERR-4005", algorithm);
                    throw new SkceWSException("SKCE-ERR-4005: Invalid input - algorithm is not AES or DESede:" + algorithm);
                }
                token = getXPathElement(bais, is, xpath, "//ds:KeyName");
                Common.log(Level.INFO, "SKCE-MSG-4009", token);
                hosturl = getXPathAttribute(bais, is, xpath, "//ds:RetrievalMethod/@URI");
                Common.log(Level.INFO, "SKCE-MSG-4010", hosturl);
                String uri = getXPathAttribute(bais, is, xpath, "//xenc:CipherReference/@URI");
                Common.log(Level.INFO, "SKCE-MSG-4011", uri);
                olddigest = getXPathElement(bais, is, xpath, "//ds:DigestValue");
                digesttype = getXPathAttribute(bais, is, xpath, "//ds:DigestMethod/@Algorithm");
                Common.log(Level.INFO, "SKCE-MSG-4012", new String[] { olddigest, digesttype });
                URL baseUrl = com.strongauth.strongkeylite.web.EncryptionService.class.getResource(".");
                URL url = new URL(baseUrl, hosturl);
                EncryptionService cryptosvc = new EncryptionService(url);
                Encryption port = cryptosvc.getEncryptionPort();
                Common.log(Level.INFO, "SKCE-MSG-4013", hosturl);
                base64key = port.decrypt(Long.parseLong(did), username, password, token);
                if (base64key != null) {
                    byte[] enckey = Base64.decode(base64key);
                    sk = new SecretKeySpec(enckey, algorithm);
                    sko = new SecretKeyObject(sk, algorithm, transform);
                    Common.putSymmetricKey(token, sko);
                    Common.log(Level.INFO, "SKCE-MSG-4014", token);
                }
                String path = Util.createUniqueDir(ENGINE_OUT_FOLDER, true);
                String target = path + CommonWS.fs + uri.substring(0, uri.indexOf(CIPHERTEXT_FILE_EXTENSION));
                File outf = new File(target);
                if (outf.exists()) {
                    Common.log(Level.SEVERE, "SKCE-ERR-4010", target);
                    throw new SkceWSException("SKCE-ERR-4010: Target output file already exists; aborting: " + target);
                }
                Common.log(Level.INFO, "SKCE-MSG-4015", target);
                File parent = outf.getParentFile();
                if (parent != null) {
                    if (parent.getFreeSpace() < (inpf.length())) {
                        Common.log(Level.SEVERE, "SKCE-ERR-4008", parent.getFreeSpace());
                        throw new SkceWSException("SKCE-ERR-4008: Insufficient estimated space to create output file: " + parent.getFreeSpace());
                    }
                }
                if (USE_ZIPINPUTSTREAM) {
                    zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(input), BUFFER_SIZE));
                    while ((entry = zis.getNextEntry()) != null) {
                        entryname = entry.getName();
                        if (entryname.endsWith(CIPHERTEXT_FILE_EXTENSION)) {
                            Common.log(Level.INFO, "SKCE-MSG-4016", entryname);
                            break;
                        }
                    }
                    zis.read(ivbytes, 0, ivbytes.length);
                    ivspec = new IvParameterSpec(ivbytes);
                    Common.log(Level.INFO, "SKCE-MSG-4017", ivbytes.length);
                } else {
                    ctis.read(ivbytes, 0, ivbytes.length);
                    ivspec = new IvParameterSpec(ivbytes);
                    Common.log(Level.INFO, "SKCE-MSG-4017", ivbytes.length);
                }
                cipher.init(Cipher.DECRYPT_MODE, sk, ivspec);
                MessageDigest digest = null;
                if (digesttype.equalsIgnoreCase("http://www.w3.org/2001/04/xmlenc#sha256")) {
                    digest = MessageDigest.getInstance("SHA-256");
                } else if (digesttype.equalsIgnoreCase("http://www.w3.org/2001/04/xmlenc#sha512")) {
                    digest = MessageDigest.getInstance("SHA-512");
                }
                Common.log(Level.INFO, "SKCE-MSG-4018", digesttype);
                int count = 0;
                int total = 0;
                byte[] data = new byte[BUFFER_SIZE];
                byte[] plaintext;
                String newdigest;
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outf), BUFFER_SIZE);
                if (USE_ZIPINPUTSTREAM) {
                    while ((count = zis.read(data)) != -1) {
                        plaintext = cipher.update(data, 0, count);
                        digest.update(plaintext, 0, plaintext.length);
                        bos.write(plaintext);
                        total += count;
                    }
                } else {
                    while ((count = ctis.read(data)) != -1) {
                        plaintext = cipher.update(data, 0, count);
                        digest.update(plaintext, 0, plaintext.length);
                        bos.write(plaintext);
                        total += count;
                    }
                }
                plaintext = cipher.doFinal();
                bos.write(plaintext);
                bos.flush();
                bos.close();
                Common.log(Level.INFO, "SKCE-MSG-4019", total);
                newdigest = new String(Base64.encode(digest.digest(plaintext)));
                if (!olddigest.equalsIgnoreCase(newdigest)) {
                    throw new SkceWSException("Decryption did not work correctly - digests do not match\n" + "Old digest: " + olddigest + '\n' + "New digest: " + newdigest);
                }
                Common.log(Level.INFO, "SKCE-MSG-4020", new String[] { Long.toString(outf.length()), outf.getName() });
                return outf.getAbsolutePath();
            } else {
                Common.log(Level.SEVERE, "SKCE-ERR-4012", did);
                throw new SkceWSException("Service NOT available for domain " + did);
            }
        } catch (SAXException ex) {
            throw new SkceWSException(ex);
        } catch (ParserConfigurationException ex) {
            throw new SkceWSException(ex);
        } catch (FileNotFoundException ex) {
            throw new SkceWSException(ex);
        } catch (BadPaddingException ex) {
            throw new SkceWSException(ex);
        } catch (IllegalBlockSizeException ex) {
            throw new SkceWSException(ex);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new SkceWSException(ex);
        } catch (InvalidKeyException ex) {
            throw new SkceWSException(ex);
        } catch (IOException ex) {
            throw new SkceWSException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new SkceWSException(ex);
        } catch (NoSuchPaddingException ex) {
            throw new SkceWSException(ex);
        } catch (StrongKeyLiteException_Exception ex) {
            throw new SkceWSException(ex);
        } catch (GeneralSecurityException ex) {
            throw new SkceWSException(ex);
        } catch (Exception ex) {
            throw new SkceWSException(ex);
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException ex) {
                throw new SkceWSException(ex);
            }
        }
    }
