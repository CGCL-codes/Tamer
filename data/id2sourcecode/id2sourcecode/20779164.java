    protected OutputStream open(OutputStream out, String encryptionOID, SecretKey encKey, AlgorithmParameters params, ASN1EncodableVector recipientInfos, Provider provider) throws NoSuchAlgorithmException, CMSException {
        try {
            BERSequenceGenerator cGen = new BERSequenceGenerator(out);
            cGen.addObject(CMSObjectIdentifiers.envelopedData);
            BERSequenceGenerator envGen = new BERSequenceGenerator(cGen.getRawOutputStream(), 0, true);
            envGen.addObject(getVersion());
            if (_berEncodeRecipientSet) {
                envGen.getRawOutputStream().write(new BERSet(recipientInfos).getEncoded());
            } else {
                envGen.getRawOutputStream().write(new DERSet(recipientInfos).getEncoded());
            }
            Cipher cipher = CMSEnvelopedHelper.INSTANCE.getSymmetricCipher(encryptionOID, provider);
            cipher.init(Cipher.ENCRYPT_MODE, encKey, params, rand);
            BERSequenceGenerator eiGen = new BERSequenceGenerator(envGen.getRawOutputStream());
            eiGen.addObject(PKCSObjectIdentifiers.data);
            if (params == null) {
                params = cipher.getParameters();
            }
            AlgorithmIdentifier encAlgId = getAlgorithmIdentifier(encryptionOID, params);
            eiGen.getRawOutputStream().write(encAlgId.getEncoded());
            BEROctetStringGenerator octGen = new BEROctetStringGenerator(eiGen.getRawOutputStream(), 0, false);
            CipherOutputStream cOut;
            if (_bufferSize != 0) {
                cOut = new CipherOutputStream(octGen.getOctetOutputStream(new byte[_bufferSize]), cipher);
            } else {
                cOut = new CipherOutputStream(octGen.getOctetOutputStream(), cipher);
            }
            return new CmsEnvelopedDataOutputStream(cOut, cGen, envGen, eiGen);
        } catch (InvalidKeyException e) {
            throw new CMSException("key invalid in message.", e);
        } catch (NoSuchPaddingException e) {
            throw new CMSException("required padding not supported.", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new CMSException("algorithm parameters invalid.", e);
        } catch (IOException e) {
            throw new CMSException("exception decoding algorithm parameters.", e);
        }
    }
