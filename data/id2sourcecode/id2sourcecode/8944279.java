    public void check(Certificate cert, Collection<String> unresolvedCritExts) throws CertPathValidatorException {
        remainingCerts--;
        try {
            X509Certificate responderCert = null;
            boolean seekResponderCert = false;
            X500Principal responderSubjectName = null;
            X500Principal responderIssuerName = null;
            BigInteger responderSerialNumber = null;
            boolean seekIssuerCert = true;
            X509CertImpl issuerCertImpl = null;
            X509CertImpl currCertImpl = X509CertImpl.toImpl((X509Certificate) cert);
            if (onlyEECert && currCertImpl.getBasicConstraints() != -1) {
                if (DEBUG != null) {
                    DEBUG.println("Skipping revocation check, not end entity cert");
                }
                return;
            }
            String[] properties = getOCSPProperties();
            URL url = getOCSPServerURL(currCertImpl, properties);
            if (properties[1] != null) {
                responderSubjectName = new X500Principal(properties[1]);
            } else if (properties[2] != null && properties[3] != null) {
                responderIssuerName = new X500Principal(properties[2]);
                String value = stripOutSeparators(properties[3]);
                responderSerialNumber = new BigInteger(value, 16);
            } else if (properties[2] != null || properties[3] != null) {
                throw new CertPathValidatorException("Must specify both ocsp.responderCertIssuerName and " + "ocsp.responderCertSerialNumber properties");
            }
            if (responderSubjectName != null || responderIssuerName != null) {
                seekResponderCert = true;
            }
            if (remainingCerts < certs.length) {
                issuerCertImpl = X509CertImpl.toImpl((X509Certificate) (certs[remainingCerts]));
                seekIssuerCert = false;
                if (!seekResponderCert) {
                    responderCert = certs[remainingCerts];
                    if (DEBUG != null) {
                        DEBUG.println("Responder's certificate is the same " + "as the issuer of the certificate being validated");
                    }
                }
            }
            if (seekIssuerCert || seekResponderCert) {
                if (DEBUG != null && seekResponderCert) {
                    DEBUG.println("Searching trust anchors for responder's " + "certificate");
                }
                Iterator anchors = pkixParams.getTrustAnchors().iterator();
                if (!anchors.hasNext()) {
                    throw new CertPathValidatorException("Must specify at least one trust anchor");
                }
                X500Principal certIssuerName = currCertImpl.getIssuerX500Principal();
                while (anchors.hasNext() && (seekIssuerCert || seekResponderCert)) {
                    TrustAnchor anchor = (TrustAnchor) anchors.next();
                    X509Certificate anchorCert = anchor.getTrustedCert();
                    X500Principal anchorSubjectName = anchorCert.getSubjectX500Principal();
                    if (dump) {
                        System.out.println("Issuer DN is " + certIssuerName);
                        System.out.println("Subject DN is " + anchorSubjectName);
                    }
                    if (seekIssuerCert && certIssuerName.equals(anchorSubjectName)) {
                        issuerCertImpl = X509CertImpl.toImpl(anchorCert);
                        seekIssuerCert = false;
                        if (!seekResponderCert && responderCert == null) {
                            responderCert = anchorCert;
                            if (DEBUG != null) {
                                DEBUG.println("Responder's certificate is the" + " same as the issuer of the certificate " + "being validated");
                            }
                        }
                    }
                    if (seekResponderCert) {
                        if ((responderSubjectName != null && responderSubjectName.equals(anchorSubjectName)) || (responderIssuerName != null && responderSerialNumber != null && responderIssuerName.equals(anchorCert.getIssuerX500Principal()) && responderSerialNumber.equals(anchorCert.getSerialNumber()))) {
                            responderCert = anchorCert;
                            seekResponderCert = false;
                        }
                    }
                }
                if (issuerCertImpl == null) {
                    throw new CertPathValidatorException("No trusted certificate for " + currCertImpl.getIssuerDN());
                }
                if (seekResponderCert) {
                    if (DEBUG != null) {
                        DEBUG.println("Searching cert stores for responder's " + "certificate");
                    }
                    X509CertSelector filter = null;
                    if (responderSubjectName != null) {
                        filter = new X509CertSelector();
                        filter.setSubject(responderSubjectName.getName());
                    } else if (responderIssuerName != null && responderSerialNumber != null) {
                        filter = new X509CertSelector();
                        filter.setIssuer(responderIssuerName.getName());
                        filter.setSerialNumber(responderSerialNumber);
                    }
                    if (filter != null) {
                        List<CertStore> certStores = pkixParams.getCertStores();
                        for (CertStore certStore : certStores) {
                            Iterator i = certStore.getCertificates(filter).iterator();
                            if (i.hasNext()) {
                                responderCert = (X509Certificate) i.next();
                                seekResponderCert = false;
                                break;
                            }
                        }
                    }
                }
            }
            if (seekResponderCert) {
                throw new CertPathValidatorException("Cannot find the responder's certificate " + "(set using the OCSP security properties).");
            }
            OCSPRequest ocspRequest = new OCSPRequest(currCertImpl, issuerCertImpl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (DEBUG != null) {
                DEBUG.println("connecting to OCSP service at: " + url);
            }
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "application/ocsp-request");
            byte[] bytes = ocspRequest.encodeBytes();
            CertId certId = ocspRequest.getCertId();
            con.setRequestProperty("Content-length", String.valueOf(bytes.length));
            OutputStream out = con.getOutputStream();
            out.write(bytes);
            out.flush();
            if (DEBUG != null && con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                DEBUG.println("Received HTTP error: " + con.getResponseCode() + " - " + con.getResponseMessage());
            }
            InputStream in = con.getInputStream();
            int contentLength = con.getContentLength();
            if (contentLength == -1) {
                contentLength = Integer.MAX_VALUE;
            }
            byte[] response = new byte[contentLength];
            int total = 0;
            int count = 0;
            while (count != -1 && total < contentLength) {
                count = in.read(response, total, response.length - total);
                total += count;
            }
            in.close();
            out.close();
            OCSPResponse ocspResponse = new OCSPResponse(response, pkixParams, responderCert);
            if (!certId.equals(ocspResponse.getCertId())) {
                throw new CertPathValidatorException("Certificate in the OCSP response does not match the " + "certificate supplied in the OCSP request.");
            }
            SerialNumber serialNumber = currCertImpl.getSerialNumberObject();
            int certOCSPStatus = ocspResponse.getCertStatus(serialNumber);
            if (DEBUG != null) {
                DEBUG.println("Status of certificate (with serial number " + serialNumber.getNumber() + ") is: " + OCSPResponse.certStatusToText(certOCSPStatus));
            }
            if (certOCSPStatus == OCSPResponse.CERT_STATUS_REVOKED) {
                throw new CertificateRevokedException("Certificate has been revoked", cp, remainingCerts - 1);
            } else if (certOCSPStatus == OCSPResponse.CERT_STATUS_UNKNOWN) {
                throw new CertPathValidatorException("Certificate's revocation status is unknown", null, cp, remainingCerts - 1);
            }
        } catch (CertificateRevokedException cre) {
            throw cre;
        } catch (CertPathValidatorException cpve) {
            throw cpve;
        } catch (Exception e) {
            throw new CertPathValidatorException(e);
        }
    }
