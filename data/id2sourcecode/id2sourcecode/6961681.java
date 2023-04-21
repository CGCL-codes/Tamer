    void verifySecurity(DrivingLicenseService service) {
        if (dg13file != null) {
            PublicKey k = dg13file.getPublicKey();
            try {
                boolean result = service.doAA(k);
                if (result) {
                    statusBar.setAAOK();
                } else {
                    statusBar.setAAFail("wrong signature");
                }
            } catch (CardServiceException cse) {
                statusBar.setAAFail(cse.getMessage());
            }
        } else {
            statusBar.setAANotChecked();
        }
        List<Integer> comDGList = new ArrayList<Integer>();
        for (Integer tag : comFile.getTagList()) {
            comDGList.add(DrivingLicenseFile.lookupDataGroupNumberByTag(tag));
        }
        Collections.sort(comDGList);
        Map<Integer, byte[]> hashes = sodFile.getDataGroupHashes();
        List<Integer> tagsOfHashes = new ArrayList<Integer>();
        tagsOfHashes.addAll(hashes.keySet());
        Collections.sort(tagsOfHashes);
        if (!tagsOfHashes.equals(comDGList)) {
            statusBar.setDIFail("\"Jeroen van Beek sanity check\" failed!");
        } else {
            try {
                String digestAlgorithm = sodFile.getDigestAlgorithm();
                MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
                for (int dgNumber : hashes.keySet()) {
                    short fid = DrivingLicenseFile.lookupFIDByTag(DrivingLicenseFile.lookupTagByDataGroupNumber(dgNumber));
                    byte[] storedHash = hashes.get(dgNumber);
                    digest.reset();
                    InputStream dgIn = null;
                    Exception exc = null;
                    try {
                        dgIn = dl.getInputStream(fid);
                    } catch (Exception ex) {
                        exc = ex;
                    }
                    if (dgIn == null && dl.hasEAP() && !dl.wasEAPPerformed() && dl.getEAPFiles().contains(fid)) {
                        continue;
                    } else {
                        if (exc != null) throw exc;
                    }
                    byte[] buf = new byte[4096];
                    while (true) {
                        int bytesRead = dgIn.read(buf);
                        if (bytesRead < 0) {
                            break;
                        }
                        digest.update(buf, 0, bytesRead);
                    }
                    byte[] computedHash = digest.digest();
                    if (!Arrays.equals(storedHash, computedHash)) {
                        statusBar.setDIFail("Authentication of DG" + dgNumber + " failed");
                    }
                }
                statusBar.setDIOK("Hash alg. " + digestAlgorithm);
            } catch (Exception e) {
                statusBar.setDIFail(e.getMessage());
            }
        }
        try {
            X509Certificate docSigningCert = sodFile.getDocSigningCertificate();
            if (sodFile.checkDocSignature(docSigningCert)) {
                statusBar.setDSOK("sig. alg. " + sodFile.getDigestEncryptionAlgorithm());
            } else {
                statusBar.setDSFail("DS Signature incorrect");
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusBar.setDSFail(e.getMessage());
        }
    }
