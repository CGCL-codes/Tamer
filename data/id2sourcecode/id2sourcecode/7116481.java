        EncryptOutputStream(Hessian2Output out) throws IOException {
            try {
                _out = out;
                KeyGenerator keyGen = KeyGenerator.getInstance(_algorithm);
                if (_secureRandom != null) keyGen.init(_secureRandom);
                SecretKey sharedKey = keyGen.generateKey();
                _out = out;
                _out.startEnvelope(X509Encryption.class.getName());
                PublicKey publicKey = _cert.getPublicKey();
                byte[] encoded = publicKey.getEncoded();
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(encoded);
                byte[] fingerprint = md.digest();
                String keyAlgorithm = publicKey.getAlgorithm();
                Cipher keyCipher = Cipher.getInstance(keyAlgorithm);
                if (_secureRandom != null) keyCipher.init(Cipher.WRAP_MODE, _cert, _secureRandom); else keyCipher.init(Cipher.WRAP_MODE, _cert);
                byte[] encKey = keyCipher.wrap(sharedKey);
                _out.writeInt(4);
                _out.writeString("algorithm");
                _out.writeString(_algorithm);
                _out.writeString("fingerprint");
                _out.writeBytes(fingerprint);
                _out.writeString("key-algorithm");
                _out.writeString(keyAlgorithm);
                _out.writeString("key");
                _out.writeBytes(encKey);
                _bodyOut = _out.getBytesOutputStream();
                _cipher = Cipher.getInstance(_algorithm);
                if (_secureRandom != null) _cipher.init(Cipher.ENCRYPT_MODE, sharedKey, _secureRandom); else _cipher.init(Cipher.ENCRYPT_MODE, sharedKey);
                _cipherOut = new CipherOutputStream(_bodyOut, _cipher);
            } catch (RuntimeException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
