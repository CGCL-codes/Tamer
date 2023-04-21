    public void decryptDocument(PDDocument doc, DecryptionMaterial decryptionMaterial) throws CryptographyException, IOException {
        this.document = doc;
        PDEncryptionDictionary dictionary = doc.getEncryptionDictionary();
        if (dictionary.getLength() != 0) {
            this.keyLength = dictionary.getLength();
        }
        if (!(decryptionMaterial instanceof PublicKeyDecryptionMaterial)) {
            throw new CryptographyException("Provided decryption material is not compatible with the document");
        }
        PublicKeyDecryptionMaterial material = (PublicKeyDecryptionMaterial) decryptionMaterial;
        try {
            boolean foundRecipient = false;
            byte[] envelopedData = null;
            byte[][] recipientFieldsBytes = new byte[dictionary.getRecipientsLength()][];
            int recipientFieldsLength = 0;
            for (int i = 0; i < dictionary.getRecipientsLength(); i++) {
                COSString recipientFieldString = dictionary.getRecipientStringAt(i);
                byte[] recipientBytes = recipientFieldString.getBytes();
                CMSEnvelopedData data = new CMSEnvelopedData(recipientBytes);
                Iterator recipCertificatesIt = data.getRecipientInfos().getRecipients().iterator();
                while (recipCertificatesIt.hasNext()) {
                    RecipientInformation ri = (RecipientInformation) recipCertificatesIt.next();
                    if (ri.getRID().match(material.getCertificate()) && !foundRecipient) {
                        foundRecipient = true;
                        envelopedData = ri.getContent(material.getPrivateKey(), "BC");
                    }
                }
                recipientFieldsBytes[i] = recipientBytes;
                recipientFieldsLength += recipientBytes.length;
            }
            if (!foundRecipient || envelopedData == null) {
                throw new CryptographyException("The certificate matches no recipient entry");
            }
            if (envelopedData.length != 24) {
                throw new CryptographyException("The enveloped data does not contain 24 bytes");
            }
            byte[] accessBytes = new byte[4];
            System.arraycopy(envelopedData, 20, accessBytes, 0, 4);
            currentAccessPermission = new AccessPermission(accessBytes);
            currentAccessPermission.setReadOnly();
            byte[] sha1Input = new byte[recipientFieldsLength + 20];
            System.arraycopy(envelopedData, 0, sha1Input, 0, 20);
            int sha1InputOffset = 20;
            for (int i = 0; i < recipientFieldsBytes.length; i++) {
                System.arraycopy(recipientFieldsBytes[i], 0, sha1Input, sha1InputOffset, recipientFieldsBytes[i].length);
                sha1InputOffset += recipientFieldsBytes[i].length;
            }
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] mdResult = md.digest(sha1Input);
            encryptionKey = new byte[this.keyLength / 8];
            System.arraycopy(mdResult, 0, encryptionKey, 0, this.keyLength / 8);
            proceedDecryption();
        } catch (CMSException e) {
            throw new CryptographyException(e);
        } catch (KeyStoreException e) {
            throw new CryptographyException(e);
        } catch (NoSuchProviderException e) {
            throw new CryptographyException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(e);
        }
    }
