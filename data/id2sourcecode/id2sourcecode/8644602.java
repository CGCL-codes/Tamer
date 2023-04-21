    public String verify(Document doc) {
        boolean isVerified = false;
        long start = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer();
        try {
            org.apache.xml.security.Init.init();
            Constants.setSignatureSpecNSprefix("dsig");
            Element sigElement = null;
            sb.append("<verifyinfo>");
            NodeList nodes = doc.getElementsByTagNameNS(org.apache.xml.security.utils.Constants.SignatureSpecNS, "Signature");
            if (nodes.getLength() != 0) {
                System.out.println("Found " + nodes.getLength() + " Signature  elements.");
                for (int i = 0; i < nodes.getLength(); i++) {
                    sigElement = (Element) nodes.item(i);
                    XMLSignature signature = new XMLSignature(sigElement, "");
                    KeyInfo ki = signature.getKeyInfo();
                    try {
                        SignedInfo xmlsiginfo = signature.getSignedInfo();
                        String ref = OAIUtil.xmlEncode(xmlsiginfo.item(0).getURI());
                        String digest = this.getDigest(xmlsiginfo);
                        sb.append("<sig url=\"" + ref + "\" digest=\"" + digest + "\"");
                        if (ki != null) {
                            if (ki.containsX509Data()) {
                                System.out.println("Could find a X509Data element in the  KeyInfo");
                            }
                            KeyInfo kinfo = signature.getKeyInfo();
                            X509Certificate cert = null;
                            if (kinfo.containsRetrievalMethod()) {
                                RetrievalMethod m = kinfo.itemRetrievalMethod(0);
                                URL url = new URL(m.getURI());
                                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                                cert = (X509Certificate) cf.generateCertificate(url.openStream());
                            } else {
                                cert = signature.getKeyInfo().getX509Certificate();
                            }
                            if (cert != null) {
                                isVerified = signature.checkSignatureValue(cert);
                                System.out.println("The XML signature is " + (isVerified ? "valid (good)" : "invalid !!!!! (bad)"));
                                if (isVerified) {
                                    sb.append(" status=\"0\"> ");
                                } else {
                                    sb.append(" status=\"1\" >");
                                }
                                sb.append(isVerified ? "valid (good)" : "invalid !!!!! (bad)");
                            } else {
                                System.out.println("Did not find a Certificate");
                                PublicKey pk = signature.getKeyInfo().getPublicKey();
                                if (pk != null) {
                                    isVerified = signature.checkSignatureValue(pk);
                                    if (isVerified) {
                                        sb.append(" status=\"0\" >");
                                    } else {
                                        sb.append(" status=\"1\" >");
                                    }
                                    System.out.println("The XML signature is " + (isVerified ? "valid (good)" : "invalid !!!!! (bad)"));
                                    sb.append(isVerified ? "valid (good)" : "invalid !!!!! (bad)");
                                } else {
                                    sb.append(" status=\"2\" >");
                                    sb.append("Did not find a public key");
                                    System.out.println("Did not find a public key, so I can't check the signature");
                                }
                            }
                        } else {
                            sb.append(" status=\"2\" >");
                            System.out.println("Did not find a KeyInfo");
                            sb.append("Did not find a KeyInfo");
                        }
                    } catch (Exception e) {
                        sb.append(" status=\"2\" >");
                        sb.append(OAIUtil.xmlEncode(e.getMessage()));
                    }
                    sb.append("</sig>");
                }
            }
            long end = System.currentTimeMillis();
            double elapsed = end - start;
            System.out.println("verified:" + elapsed);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            sb.append(OAIUtil.xmlEncode(e.getMessage()));
        }
        sb.append("</verifyinfo>");
        this.info = sb.toString();
        System.out.println(sb.toString());
        return sb.toString();
    }
