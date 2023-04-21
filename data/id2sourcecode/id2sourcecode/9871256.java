    private boolean signAndPublish() {
        String before = "";
        String after = "";
        try {
            if (jCHBefore.isSelected()) new SimpleDateFormat("yyyy.MM.dd").parse(textBeforeDate.getText());
            if (jCHAfter.isSelected()) new SimpleDateFormat("yyyy.MM.dd").parse(textAfterDate.getText());
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
            boolean v2wanted = true;
            java.math.BigInteger ACSerialNumber;
            try {
                ACSerialNumber = new java.math.BigInteger("0");
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                return false;
            }
            ACSerialNumber = ACSerialNumber.abs();
            issrg.ac.AttCertValidityPeriod validity_period = new issrg.ac.AttCertValidityPeriod(issrg.ac.Util.buildGeneralizedTime(before), issrg.ac.Util.buildGeneralizedTime(after));
            PMIXMLPolicy policy = new PMIXMLPolicy(xmlPolicy);
            issrg.ac.Attribute attr = new issrg.ac.Attribute(PMIXMLPolicy.PMI_XML_POLICY_ATTRIBUTE_OID, policy);
            Vector attributes = new Vector();
            attributes.add(attr);
            issrg.ac.Extensions extensions = new issrg.ac.Extensions(new Vector());
            issrg.ac.AttCertIssuer issuer;
            SimpleSigningUtility signingUtility = new SimpleSigningUtility();
            try {
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
                issrg.ac.V2Form signer = new issrg.ac.V2Form(issrg.ac.Util.buildGeneralNames(subjectDN), null, null);
                issuer = new issrg.ac.AttCertIssuer(null, signer);
                iaik.asn1.structures.GeneralNames hn = issrg.ac.Util.buildGeneralNames(subjectDN);
                issrg.ac.Holder holder = new issrg.ac.Holder(null, hn, null);
                byte[] bt = signerPKC.getSigAlgParams();
                ASN1Object algParams = bt == null ? null : iaik.asn1.DerCoder.decode(bt);
                AlgorithmID signatureAlg = new AlgorithmID(new iaik.asn1.ObjectID(signerPKC.getSigAlgOID()), algParams);
                issrg.ac.AttributeCertificateInfo aci = new issrg.ac.AttributeCertificateInfo(new issrg.ac.AttCertVersion(v2wanted ? issrg.ac.AttCertVersion.V2 : issrg.ac.AttCertVersion.DEFAULT), holder, issuer, signatureAlg, ACSerialNumber, validity_period, attributes, null, extensions);
                AttributeCertificate cert = null;
                byte[] b = aci.getEncoded();
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(b);
                byte[] result = md.digest();
                BigInteger bi = new BigInteger(result);
                bi = bi.abs();
                ACSerialNumber = new java.math.BigInteger((bi.toString(16)), 16);
                aci = new issrg.ac.AttributeCertificateInfo(new issrg.ac.AttCertVersion(v2wanted ? issrg.ac.AttCertVersion.V2 : issrg.ac.AttCertVersion.DEFAULT), holder, issuer, signatureAlg, ACSerialNumber, validity_period, attributes, null, extensions);
                b = aci.getEncoded();
                try {
                    cert = new issrg.ac.AttributeCertificate(aci, signatureAlg, new BIT_STRING(signingUtility.sign(b)));
                } catch (Throwable e) {
                    throw new ACCreationException(e.getMessage(), e);
                } finally {
                    signingUtility.logout(null, env);
                }
                if (cert != null) {
                    SavingUtility su = null;
                    if (jRBfile.isSelected()) {
                        su = new issrg.acm.DiskSavingUtility();
                    } else {
                        su = new issrg.acm.extensions.LDAPSavingUtility();
                    }
                    boolean redo;
                    do {
                        redo = false;
                        try {
                            su.save(jf, cert.getEncoded(), env);
                        } catch (issrg.ac.ACCreationException acce) {
                            issrg.utils.Util.bewail(acce.getMessage(), acce, this);
                            redo = javax.swing.JOptionPane.showConfirmDialog(this, "Try to save again?", "Confirm", javax.swing.JOptionPane.OK_CANCEL_OPTION) == javax.swing.JOptionPane.OK_OPTION;
                        }
                    } while (redo);
                }
            } catch (iaik.asn1.CodingException ce) {
                throw new ACCreationException(ce.getMessage(), ce);
            } catch (issrg.security.SecurityException se) {
                throw new ACCreationException(se.getMessage(), se);
            }
        } catch (Exception ge) {
            JOptionPane.showMessageDialog(jf, ge.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
