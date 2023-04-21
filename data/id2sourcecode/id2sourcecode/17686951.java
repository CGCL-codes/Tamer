    public void prepareDocumentForEncryption(PDDocument doc) throws CryptographyException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            PDEncryptionDictionary dictionary = doc.getEncryptionDictionary();
            dictionary.setFilter(FILTER);
            dictionary.setLength(this.keyLength);
            dictionary.setVersion(2);
            dictionary.setSubFilter(SUBFILTER);
            byte[][] recipientsField = new byte[policy.getRecipientsNumber()][];
            byte[] seed = new byte[20];
            KeyGenerator key = KeyGenerator.getInstance("AES");
            key.init(192, new SecureRandom());
            SecretKey sk = key.generateKey();
            System.arraycopy(sk.getEncoded(), 0, seed, 0, 20);
            Iterator it = policy.getRecipientsIterator();
            int i = 0;
            while (it.hasNext()) {
                PublicKeyRecipient recipient = (PublicKeyRecipient) it.next();
                X509Certificate certificate = recipient.getX509();
                int permission = recipient.getPermission().getPermissionBytesForPublicKey();
                byte[] pkcs7input = new byte[24];
                byte one = (byte) (permission);
                byte two = (byte) (permission >>> 8);
                byte three = (byte) (permission >>> 16);
                byte four = (byte) (permission >>> 24);
                System.arraycopy(seed, 0, pkcs7input, 0, 20);
                pkcs7input[20] = four;
                pkcs7input[21] = three;
                pkcs7input[22] = two;
                pkcs7input[23] = one;
                DERObject obj = createDERForRecipient(pkcs7input, certificate);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DEROutputStream k = new DEROutputStream(baos);
                k.writeObject(obj);
                recipientsField[i] = baos.toByteArray();
                i++;
            }
            dictionary.setRecipients(recipientsField);
            int sha1InputLength = seed.length;
            for (int j = 0; j < dictionary.getRecipientsLength(); j++) {
                COSString string = dictionary.getRecipientStringAt(j);
                sha1InputLength += string.getBytes().length;
            }
            byte[] sha1Input = new byte[sha1InputLength];
            System.arraycopy(seed, 0, sha1Input, 0, 20);
            int sha1InputOffset = 20;
            for (int j = 0; j < dictionary.getRecipientsLength(); j++) {
                COSString string = dictionary.getRecipientStringAt(j);
                System.arraycopy(string.getBytes(), 0, sha1Input, sha1InputOffset, string.getBytes().length);
                sha1InputOffset += string.getBytes().length;
            }
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] mdResult = md.digest(sha1Input);
            this.encryptionKey = new byte[this.keyLength / 8];
            System.arraycopy(mdResult, 0, this.encryptionKey, 0, this.keyLength / 8);
            doc.setEncryptionDictionary(dictionary);
            doc.getDocument().setEncryptionDictionary(dictionary.encryptionDictionary);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptographyException(ex);
        } catch (NoSuchProviderException ex) {
            throw new CryptographyException(ex);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CryptographyException(e);
        }
    }
