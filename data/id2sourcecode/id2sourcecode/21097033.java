    private void updateCOMSODFile(X509Certificate newCertificate) {
        if (!updateCOMSODfiles || sodFile == null || comFile == null) {
            return;
        }
        try {
            String digestAlg = sodFile.getDigestAlgorithm();
            X509Certificate cert = newCertificate != null ? newCertificate : sodFile.getDocSigningCertificate();
            String signatureAlg = cert.getSigAlgName();
            byte[] signature = sodFile.getEncryptedDigest();
            Map<Integer, byte[]> dgHashes = new TreeMap<Integer, byte[]>();
            List<Short> dgFids = getFileList();
            if (dgFids.size() < 4) {
                return;
            }
            comFile.getTagList().clear();
            Collections.sort(dgFids);
            MessageDigest digest = MessageDigest.getInstance(digestAlg);
            for (Short fid : dgFids) {
                if (fid != DrivingLicenseService.EF_COM && fid != DrivingLicenseService.EF_SOD) {
                    byte[] data = getFileBytes(fid);
                    byte tag = data[0];
                    dgHashes.put(DrivingLicenseFile.lookupDataGroupNumberByTag(tag), digest.digest(data));
                    comFile.insertTag(new Integer(tag));
                }
            }
            if (signer != null) {
                signer.setCertificate(cert);
                sodFile = new SODFile(digestAlg, signatureAlg, dgHashes, signer, cert);
            } else {
                sodFile = new SODFile(digestAlg, signatureAlg, dgHashes, signature, cert);
            }
            updateSOIS();
            putFile(DrivingLicenseService.EF_SOD, sodFile.getEncoded());
            putFile(DrivingLicenseService.EF_COM, comFile.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
