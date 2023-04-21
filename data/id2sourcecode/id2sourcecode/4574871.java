    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("==================== DO GET METHOD START=========================");
        PrintWriter out = response.getWriter();
        String retrieve = (String) request.getParameter("retrieve");
        if (retrieve != null) {
            System.out.println("Retrieving: " + retrieve);
            if (retrieve.equals("DATA")) out.print(DATA); else if (retrieve.equals("ENCODED_AUTHENTICATED_ATTRIBUTES")) {
                ExternalSignatureSignerInfoGenerator gen = buildSignerInfoGenerator();
                sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
                byte[] certificate = decoder.decodeBuffer(request.getParameter("certificate"));
                java.security.cert.CertificateFactory cf;
                try {
                    cf = java.security.cert.CertificateFactory.getInstance("X.509");
                    java.io.ByteArrayInputStream bais1 = new java.io.ByteArrayInputStream(certificate);
                    java.security.cert.X509Certificate javaCert = (java.security.cert.X509Certificate) cf.generateCertificate(bais1);
                    gen.setCertificate(javaCert);
                } catch (CertificateException e1) {
                    e1.printStackTrace();
                }
                byte[] bytesToSign = getAuthenticatedAttributesBytes(gen);
                String attrPrintout = getAuthenticatedAttributesPrintout(bytesToSign);
                System.out.println("Authenticated Attributes printout follows:\n" + attrPrintout);
                byte[] digestBytes = null;
                java.security.MessageDigest md;
                try {
                    md = java.security.MessageDigest.getInstance(CMSSignedDataGenerator.DIGEST_SHA256);
                    md.update(bytesToSign);
                    digestBytes = md.digest();
                } catch (NoSuchAlgorithmException e) {
                    System.out.println(e);
                }
                System.out.println("Encapsulating digest in digestInfo ...");
                byte[] digestInfoBytes = encapsulateInDigestInfo(CMSSignedDataGenerator.DIGEST_SHA256, digestBytes);
                System.out.println(formatAsString(digestInfoBytes, " "));
                String storeKey = formatAsString(digestInfoBytes, "");
                System.out.println("Saving SignerInfoGenerator with key: " + storeKey);
                System.out.println("The key the string representation of digestInfo!");
                SignerInfoGeneratorItem s = new SignerInfoGeneratorItem(gen, attrPrintout);
                this.signerInfoGeneratorTable.put(storeKey, s);
                System.out.println("Returning digestInfo to  client ...");
                out.print(base64Encode(digestInfoBytes));
            } else if (retrieve.equals("AUTHENTICATED_ATTRIBUTES_PRINTOUT")) {
                String base64Hash = (String) request.getParameter("encodedhash");
                if (base64Hash != null) {
                    sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
                    byte[] hash = decoder.decodeBuffer(base64Hash);
                    SignerInfoGeneratorItem s = (SignerInfoGeneratorItem) this.signerInfoGeneratorTable.get(formatAsString(hash, ""));
                    out.print(s.getAttrPrintout());
                }
            } else out.println("Error: value '" + retrieve + "' for required parameter 'retrive' not expected.");
        } else out.println("Error: required parameter 'retrive' not found.");
        out.flush();
        System.out.println("==================== DO GET METHOD END=========================");
    }
