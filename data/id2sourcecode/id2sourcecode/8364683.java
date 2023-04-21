    private CMSEnvelopedData generate(CMSProcessable content, String encryptionOID, KeyGenerator keyGen, Provider provider) throws NoSuchAlgorithmException, CMSException {
        Provider encProvider = keyGen.getProvider();
        ASN1EncodableVector recipientInfos = new ASN1EncodableVector();
        AlgorithmIdentifier encAlgId;
        SecretKey encKey;
        ASN1OctetString encContent;
        try {
            Cipher cipher = CMSEnvelopedHelper.INSTANCE.getSymmetricCipher(encryptionOID, encProvider);
            AlgorithmParameters params;
            encKey = keyGen.generateKey();
            params = generateParameters(encryptionOID, encKey, encProvider);
            cipher.init(Cipher.ENCRYPT_MODE, encKey, params, rand);
            if (params == null) {
                params = cipher.getParameters();
            }
            encAlgId = getAlgorithmIdentifier(encryptionOID, params);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            CipherOutputStream cOut = new CipherOutputStream(bOut, cipher);
            content.write(cOut);
            cOut.close();
            encContent = new BERConstructedOctetString(bOut.toByteArray());
        } catch (InvalidKeyException e) {
            throw new CMSException("key invalid in message.", e);
        } catch (NoSuchPaddingException e) {
            throw new CMSException("required padding not supported.", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new CMSException("algorithm parameters invalid.", e);
        } catch (IOException e) {
            throw new CMSException("exception decoding algorithm parameters.", e);
        }
        Iterator it = recipientInfs.iterator();
        while (it.hasNext()) {
            RecipientInf recipient = (RecipientInf) it.next();
            try {
                recipientInfos.add(recipient.toRecipientInfo(encKey, rand, provider));
            } catch (IOException e) {
                throw new CMSException("encoding error.", e);
            } catch (InvalidKeyException e) {
                throw new CMSException("key inappropriate for algorithm.", e);
            } catch (GeneralSecurityException e) {
                throw new CMSException("error making encrypted content.", e);
            }
        }
        EncryptedContentInfo eci = new EncryptedContentInfo(PKCSObjectIdentifiers.data, encAlgId, encContent);
        ContentInfo contentInfo = new ContentInfo(PKCSObjectIdentifiers.envelopedData, new EnvelopedData(null, new DERSet(recipientInfos), eci, null));
        return new CMSEnvelopedData(contentInfo);
    }
