    private void readConf() {
        java.net.URL configURL = this.getClass().getResource("saws.xml");
        File configFile = new File("saws.xml");
        if (!(configFile.exists())) configFile = new File(configURL.getFile());
        java.io.BufferedReader in = null;
        try {
            in = new java.io.BufferedReader(new java.io.FileReader(configFile));
        } catch (Exception e) {
            if (debugLevel >= SAWSConstant.ErrorInfo) sawsDebugLog.write(e.toString());
            System.exit(-1);
        }
        DocumentBuilder xdb = XMLParserUtils.getXMLDocBuilder();
        org.w3c.dom.Document doc = null;
        try {
            doc = xdb.parse(new InputSource(in));
        } catch (Exception e) {
            if (debugLevel >= SAWSConstant.ErrorInfo) {
                sawsDebugLog.write(e.toString() + "\nThe SAWS configuration file saws.xml could not be read." + "\nPlease check Line " + ((SAXParseException) e).getLineNumber() + " and Column " + ((SAXParseException) e).getColumnNumber() + " in saws.xml.");
            }
            this.showMessage("The SAWS configuration file saws.xml could not be read." + "\nPlease check Line " + ((SAXParseException) e).getLineNumber() + " and Column " + ((SAXParseException) e).getColumnNumber() + " in saws.xml.", SAWSTextOutputCallback.ERROR);
            System.exit(-1);
        }
        org.w3c.dom.Element root = null;
        if (doc != null) {
            root = doc.getDocumentElement();
        }
        NodeList nl4 = root.getElementsByTagName("CallbackHandler");
        org.w3c.dom.Element e5 = (org.w3c.dom.Element) nl4.item(0);
        String className = null;
        if (e5 != null) {
            className = DOMUtils.getAttribute(e5, "class");
            if (className != null) {
                this.callBackHandlerClass = className;
                String errorMessage = null;
                try {
                    if (this.callBackHandlerClass.equals("issrg.SAWS.callback.SAWSFileCallbackHandler")) {
                        String inputFile = DOMUtils.getAttribute(e5, "inputFile");
                        String outputFile = DOMUtils.getAttribute(e5, "outputFile");
                        this.callbackHandler = new SAWSFileCallbackHandler(inputFile, outputFile);
                    } else {
                        this.callbackHandler = (CallbackHandler) (Class.forName(this.callBackHandlerClass).newInstance());
                    }
                } catch (IllegalArgumentException iae) {
                    errorMessage = iae.getMessage();
                } catch (InstantiationException ie) {
                    errorMessage = "The callback handler class \"" + className + "\" could not be instantiated." + "\nPlease check the class name in the configuration file (saws.xml).";
                } catch (ClassNotFoundException cnfe) {
                    errorMessage = "The callback handler class \"" + className + "\" could not be found." + "\nPlease check the class name in the configuration file (saws.xml)" + " or the class path.";
                } catch (IllegalAccessException iae) {
                    errorMessage = "The callback handler class \"" + className + "\" could not be accessed." + "\nPlease check the permissions to run the specified class.";
                } finally {
                    if (errorMessage != null) {
                        String[] options = { "Continue", "Stop" };
                        if (debugLevel >= SAWSConstant.ErrorInfo) {
                            sawsDebugLog.write(errorMessage);
                        }
                        errorMessage = errorMessage + "\n\nSAWS can use a default callback handler. Please select \"Continue\"" + "\nto use the default handler, or select \"Stop\" to finish SAWS.";
                        int selection = this.createConfirmCallback(errorMessage, options, SAWSChoiceCallback.WARNING, "CallbackHandlerError");
                        if (selection == 1) {
                            System.exit(-1);
                        }
                    }
                }
            }
        }
        NodeList nl = root.getElementsByTagName("SAWSBasic");
        org.w3c.dom.Element e = (org.w3c.dom.Element) nl.item(0);
        encryptionKeystoreLocation = DOMUtils.getAttribute(e, "encryptionKeystoreLocation");
        rootCA = DOMUtils.getAttribute(e, "rootCA");
        if (rootCA != null) {
            File rootCAFile = new File(rootCA);
            if (!rootCAFile.exists()) {
                this.showMessage("SAWS can't find the rootCA public key certificate." + rootCA + "\n\nSAWS will stop and the SAWS administrator needs to put the root CA public key certificate" + "\nin the correct position as specified in the SAWS configuration file saws.xml.", SAWSTextOutputCallback.WARNING);
                System.exit(-1);
            }
        } else {
            this.showMessage("The rootCA public key certificate was not specified." + "\n\nSAWS will stop and the SAWS administrator needs to specify the root CA public key certificate" + "\nin the SAWS configuration file saws.xml.", SAWSTextOutputCallback.WARNING);
            System.exit(-1);
        }
        signRecordNumber = Integer.parseInt(DOMUtils.getAttribute(e, "signRecordNumber"));
        numberOfEncPasswordShares = Integer.parseInt(DOMUtils.getAttribute(e, "numberOfEncPasswordShares"));
        heartbeatInterval = Integer.parseInt(DOMUtils.getAttribute(e, "heartbeatInterval"));
        logFileRoot = DOMUtils.getAttribute(e, "logFileRoot");
        vtPKC = DOMUtils.getAttribute(e, "vtPKC");
        SAWSInterface = DOMUtils.getAttribute(e, "SAWSInterface");
        logEncryption = DOMUtils.getAttribute(e, "logEncryption");
        debugLevel = Integer.parseInt(DOMUtils.getAttribute(e, "debugLevel"));
        sawsDebugLog.write(e.toString() + "\nLog encryption: " + logEncryption);
        NodeList nl2 = root.getElementsByTagName("TPMAdvanced");
        org.w3c.dom.Element e2 = (org.w3c.dom.Element) nl2.item(0);
        signingKeystoreLocation = DOMUtils.getAttribute(e2, "signingKeystoreLocation");
        numberOfPasswordShares = Integer.parseInt(DOMUtils.getAttribute(e2, "numberOfPasswordShares"));
        trustedLocation = DOMUtils.getAttribute(e2, "trustedLocation");
        String s1 = DOMUtils.getAttribute(e2, "accumulatedHashAlgorithm");
        if (s1 != null) {
            if (s1.equalsIgnoreCase("SHA-1") || s1.equalsIgnoreCase("SHA1") || s1.equalsIgnoreCase("SHA")) {
                this.hashAlgorithm = "SHA1";
            } else if (s1.equalsIgnoreCase("MD5")) {
                this.hashAlgorithm = "MD5";
            } else if (s1.equalsIgnoreCase("SHA-256") || s1.equalsIgnoreCase("SHA256")) {
                this.hashAlgorithm = "SHA256";
            } else if (s1.equalsIgnoreCase("SHA-384") || s1.equalsIgnoreCase("SHA384")) {
                this.hashAlgorithm = "SHA384";
            } else if (s1.equalsIgnoreCase("SHA-512") || s1.equalsIgnoreCase("SHA512")) {
                this.hashAlgorithm = "SHA512";
            } else {
                this.showMessage("The hash algorithm specified in SAWS configuration file (saws.xml) is not supported." + "\n\nSAWS will stop and the SAWS administrator needs to specify the the correct algorithm," + "\nor remove the specification from the configuration file to use the default algorithm (SHA-1).", SAWSTextOutputCallback.WARNING);
                System.exit(-1);
            }
        }
        this.signingAlg = DOMUtils.getAttribute(e2, "signingAlgorithm");
        NodeList nl3 = root.getElementsByTagName("UserInfo");
        int leng = nl3.getLength();
        for (int i = 0; i < leng; i++) {
            org.w3c.dom.Element e3 = (org.w3c.dom.Element) nl3.item(i);
            String UserDNString = DOMUtils.getAttribute(e3, "userDN");
            UserDNString = issrg.utils.RFC2253NameParser.toCanonicalDN(UserDNString).toUpperCase();
            Byte UserIDByte = new Byte((byte) Integer.parseInt(DOMUtils.getAttribute(e3, "userID")));
            String userPKC = DOMUtils.getAttribute(e3, "userPKC");
            PublicKey userPK = null;
            if (userPKC != null) {
                userPK = retrievePublicKey(userPKC);
            }
            if (UserDNString != null && UserIDByte != null) {
                UserDNIDMap.put(UserDNString, UserIDByte);
            }
            if (userPK != null && UserIDByte != null) {
                UserIDPKMap.put(UserIDByte, userPK);
            }
        }
    }
