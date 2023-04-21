    private boolean signAndPublish() {
        String before = "";
        String after = "";
        try {
            if (!jCHBefore.isSelected()) new SimpleDateFormat("yyyy.MM.dd").parse(textBeforeDate.getText());
            if (!jCHAfter.isSelected()) new SimpleDateFormat("yyyy.MM.dd").parse(textAfterDate.getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(jf, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!jCHBefore.isSelected()) {
            before = textBeforeDate.getText() + " " + spinnerBeforeH.getValue() + ":" + spinnerBeforeM.getValue() + ":" + spinnerBeforeS.getValue();
        }
        if (!jCHAfter.isSelected()) {
            after = after + textAfterDate.getText() + " " + spinnerAfterH.getValue() + ":" + spinnerAfterM.getValue() + ":" + spinnerAfterS.getValue();
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            BigInteger bi = new BigInteger(md.digest(new java.util.GregorianCalendar().getTime().toString().getBytes()));
            java.math.BigInteger ACSerialNumber = new java.math.BigInteger((bi.abs().toString(16)).substring(0, 14), 16);
            issrg.ac.Generalized_Time btt = null;
            issrg.ac.Generalized_Time att = null;
            if (!jCHBefore.isSelected()) btt = Util.buildGeneralizedTime(before);
            if (!jCHAfter.isSelected()) att = Util.buildGeneralizedTime(after);
            issrg.ac.AttCertValidityPeriod validity_period = new issrg.ac.AttCertValidityPeriod(btt, att);
            PMIXMLPolicy policy = new PMIXMLPolicy(xmlPolicy);
            issrg.ac.Attribute attr = new issrg.ac.Attribute(PMIXMLPolicy.PMI_XML_POLICY_ATTRIBUTE_OID, policy);
            Vector attributes = new Vector();
            attributes.add(attr);
            issrg.ac.Extensions extensions = new issrg.ac.Extensions(new Vector());
            issrg.ac.AttCertIssuer issuer;
            SimpleSigningUtility signingUtility = new SimpleSigningUtility();
            Node root = PEApplication.configComponent.DOM.getElementsByTagName("ApplicationSettings").item(0);
            Element signingKey = ((Element) NodeVector.getChildElements(root).item(0));
            try {
                env.remove(DefaultSecurity.DEFAULT_FILE_STRING);
                env.put(DefaultSecurity.DEFAULT_FILE_STRING, signingKey.getAttribute("FileName"));
                signingUtility.login(jf, env);
                java.security.cert.X509Certificate signerPKC = signingUtility.getVerificationCertificate();
                String subjectDN;
                if (signerPKC instanceof iaik.x509.X509Certificate) {
                    try {
                        subjectDN = ((iaik.asn1.structures.Name) signerPKC.getSubjectDN()).getRFC2253String();
                    } catch (iaik.utils.RFC2253NameParserException rnpe) {
                        throw new ACCreationException("Failed to decode DNs", rnpe);
                    }
                } else {
                    subjectDN = signerPKC.getSubjectDN().getName();
                }
                iaik.asn1.structures.GeneralNames hn = issrg.ac.Util.buildGeneralNames(subjectDN);
                issrg.ac.V2Form signer = new issrg.ac.V2Form(hn, null, null);
                issuer = new issrg.ac.AttCertIssuer(null, signer);
                issrg.ac.Holder holder = new issrg.ac.Holder(null, hn, null);
                byte[] bt = signerPKC.getSigAlgParams();
                ASN1Object algParams = bt == null ? null : iaik.asn1.DerCoder.decode(bt);
                AlgorithmID signatureAlg = new AlgorithmID(new iaik.asn1.ObjectID(signingUtility.getSigningAlgorithmID()));
                issrg.ac.AttributeCertificateInfo aci = new issrg.ac.AttributeCertificateInfo(new issrg.ac.AttCertVersion(issrg.ac.AttCertVersion.V2), holder, issuer, signatureAlg, ACSerialNumber, validity_period, attributes, null, extensions);
                AttributeCertificate cert = null;
                byte[] b = aci.getEncoded();
                try {
                    cert = new issrg.ac.AttributeCertificate(aci, signatureAlg, new BIT_STRING(signingUtility.sign(b)));
                } catch (Throwable e) {
                    throw new ACCreationException(e.getMessage(), e);
                } finally {
                    signingUtility.logout(null, env);
                }
                if (cert != null) {
                    issrg.acm.SavingUtility su = null;
                    if (jRBfile.isSelected()) {
                        su = new issrg.acm.DiskSavingUtility();
                    } else if (jRBWebDAV.isSelected()) {
                        Node webdavCon = PEApplication.configComponent.DOM.getElementsByTagName("WebDAVConfiguration").item(0);
                        Element selectedConnection = ((Element) NodeVector.getChildElements(webdavCon).item(availableConnectionsWebDAV.getSelectedIndex()));
                        String host = selectedConnection.getAttribute("Host");
                        String port = selectedConnection.getAttribute("Port");
                        String protocol = selectedConnection.getAttribute("Protocol");
                        String p12filename = selectedConnection.getAttribute("P12Filename");
                        String p12password = selectedConnection.getAttribute("P12Password");
                        setWebDAVParameters(host, port, protocol, p12filename, p12password);
                        su = new issrg.acm.extensions.WebDAVSavingUtility(WebDAVSavingUtility.policyPrefix + xmlPolicyName);
                    } else {
                        Node ldapCon = PEApplication.configComponent.DOM.getElementsByTagName("LDAPConfiguration").item(0);
                        Element selectedConnection = ((Element) NodeVector.getChildElements(ldapCon).item(availableConnections.getSelectedIndex()));
                        String host = "ldap://" + selectedConnection.getAttribute("Host");
                        if (selectedConnection.getAttribute("Port") != null) host += ":" + selectedConnection.getAttribute("Port");
                        if (selectedConnection.getAttribute("BaseDN") != null) host += "/" + selectedConnection.getAttribute("BaseDN");
                        if (selectedConnection.getAttribute("Login") == null || selectedConnection.getAttribute("Login").equals("")) setLDAPParameters(host, "", selectedConnection.getAttribute("ACType")); else setLDAPParameters(host, selectedConnection.getAttribute("Login"), selectedConnection.getAttribute("ACType"));
                        su = new issrg.acm.extensions.LDAPSavingUtility();
                    }
                    boolean redo;
                    do {
                        redo = false;
                        try {
                            env.put(EnvironmentalVariables.HOLDER_NAME_STRING, subjectDN);
                            su.save(jf, cert.getEncoded(), env);
                        } catch (ACCreationException acce) {
                            issrg.utils.Util.bewail(acce.getMessage(), acce, this);
                            redo = javax.swing.JOptionPane.showConfirmDialog(this, "Try to save again?", "Confirm", javax.swing.JOptionPane.OK_CANCEL_OPTION) == javax.swing.JOptionPane.OK_OPTION;
                        }
                    } while (redo);
                }
            } catch (iaik.asn1.CodingException ce) {
                throw new ACCreationException(ce.getMessage(), ce);
            } catch (issrg.security.SecurityException se) {
                se.printStackTrace();
                throw new ACCreationException(se.getMessage(), se);
            }
        } catch (Exception ge) {
            JOptionPane.showMessageDialog(jf, ge.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
