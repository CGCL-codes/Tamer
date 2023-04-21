    SignerInfo verify(PKCS7 block, byte[] data) throws NoSuchAlgorithmException, SignatureException {
        try {
            ContentInfo content = block.getContentInfo();
            if (data == null) {
                data = content.getContentBytes();
            }
            String digestAlgname = getDigestAlgorithmId().getName();
            if (digestAlgname.equalsIgnoreCase("SHA")) digestAlgname = "SHA1";
            byte[] dataSigned;
            if (authenticatedAttributes == null) {
                dataSigned = data;
            } else {
                ObjectIdentifier contentType = (ObjectIdentifier) authenticatedAttributes.getAttributeValue(PKCS9Attribute.CONTENT_TYPE_OID);
                if (contentType == null || !contentType.equals(content.contentType)) return null;
                byte[] messageDigest = (byte[]) authenticatedAttributes.getAttributeValue(PKCS9Attribute.MESSAGE_DIGEST_OID);
                if (messageDigest == null) return null;
                MessageDigest md = MessageDigest.getInstance(digestAlgname);
                byte[] computedMessageDigest = md.digest(data);
                if (messageDigest.length != computedMessageDigest.length) return null;
                for (int i = 0; i < messageDigest.length; i++) {
                    if (messageDigest[i] != computedMessageDigest[i]) return null;
                }
                dataSigned = authenticatedAttributes.getDerEncoding();
            }
            String encryptionAlgname = getDigestEncryptionAlgorithmId().getName();
            if (encryptionAlgname.equalsIgnoreCase("SHA1withDSA")) encryptionAlgname = "DSA";
            String algname = digestAlgname + "with" + encryptionAlgname;
            Signature sig = Signature.getInstance(algname);
            X509Certificate cert = getCertificate(block);
            if (cert == null) {
                return null;
            }
            if (cert.hasUnsupportedCriticalExtension()) {
                throw new SignatureException("Certificate has unsupported " + "critical extension(s)");
            }
            boolean[] keyUsageBits = cert.getKeyUsage();
            if (keyUsageBits != null) {
                KeyUsageExtension keyUsage;
                try {
                    keyUsage = new KeyUsageExtension(keyUsageBits);
                } catch (IOException ioe) {
                    throw new SignatureException("Failed to parse keyUsage extension");
                }
                boolean digSigAllowed = ((Boolean) keyUsage.get(KeyUsageExtension.DIGITAL_SIGNATURE)).booleanValue();
                boolean nonRepuAllowed = ((Boolean) keyUsage.get(KeyUsageExtension.NON_REPUDIATION)).booleanValue();
                if (!digSigAllowed && !nonRepuAllowed) {
                    throw new SignatureException("Key usage restricted: cannot be used for digital signatures");
                }
            }
            PublicKey key = cert.getPublicKey();
            sig.initVerify(key);
            sig.update(dataSigned);
            if (sig.verify(encryptedDigest)) {
                return this;
            }
        } catch (IOException e) {
            throw new SignatureException("IO error verifying signature:\n" + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new SignatureException("InvalidKey: " + e.getMessage());
        }
        return null;
    }
