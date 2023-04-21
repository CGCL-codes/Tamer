    public int init(byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, NoConfirmAttributeException {
        try {
            if (state == STATE_INIT) {
                return STATE_INIT;
            }
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(key);
            byte[] confirmAttribute = md.digest();
            String confAttB64 = Base64.encodeBytes(confirmAttribute);
            String pruefsumme = m_document.getRootElement().getAttributeValue(ATT_CONFIRM);
            if (pruefsumme == null) {
                throw new NoConfirmAttributeException("No \"" + ATT_CONFIRM + "\"-Attribute");
            }
            if (!pruefsumme.equals(confAttB64)) {
                state = STATE_WRONG_KEY;
                ++countWrongKey;
                return state;
            }
            Key k = new SecretKeySpec(key, "DES");
            cipherEnc = Cipher.getInstance("DES");
            cipherEnc.init(Cipher.ENCRYPT_MODE, k);
            cipherDec = Cipher.getInstance("DES");
            cipherDec.init(Cipher.DECRYPT_MODE, k);
            state = STATE_INIT;
            return state;
        } catch (InvalidKeyException e) {
            state = STATE_UNINIT;
            return state;
        } catch (NoSuchAlgorithmException e) {
            throw e;
        } catch (NoSuchPaddingException e) {
            throw e;
        }
    }
