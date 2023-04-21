        public OverloadedChecksum(Krb5Context context, Checksum checksum, EncryptionKey key) throws GSSException, KrbException, IOException {
            int pos = 0;
            checksumBytes = checksum.getBytes();
            if ((checksumBytes[0] != CHECKSUM_FIRST_BYTES[0]) || (checksumBytes[1] != CHECKSUM_FIRST_BYTES[1]) || (checksumBytes[2] != CHECKSUM_FIRST_BYTES[2]) || (checksumBytes[3] != CHECKSUM_FIRST_BYTES[3])) {
                throw new GSSException(GSSException.FAILURE, -1, "Incorrect checksum");
            }
            ChannelBinding localBindings = context.getChannelBinding();
            if (localBindings != null) {
                byte[] remoteBindingBytes = new byte[CHECKSUM_BINDINGS_SIZE];
                System.arraycopy(checksumBytes, 4, remoteBindingBytes, 0, CHECKSUM_BINDINGS_SIZE);
                byte[] noBindings = new byte[CHECKSUM_BINDINGS_SIZE];
                if (!Arrays.equals(noBindings, remoteBindingBytes)) {
                    byte[] localBindingsBytes = computeChannelBinding(localBindings);
                    if (!Arrays.equals(localBindingsBytes, remoteBindingBytes)) {
                        throw new GSSException(GSSException.BAD_BINDINGS, -1, "Bytes mismatch!");
                    }
                } else {
                    throw new GSSException(GSSException.BAD_BINDINGS, -1, "Token missing ChannelBinding!");
                }
            }
            flags = readLittleEndian(checksumBytes, 20, 4);
            if ((flags & CHECKSUM_DELEG_FLAG) > 0) {
                int credLen = readLittleEndian(checksumBytes, 26, 2);
                byte[] credBytes = new byte[credLen];
                System.arraycopy(checksumBytes, 28, credBytes, 0, credLen);
                CipherHelper cipherHelper = context.getCipherHelper(key);
                if (useNullKey(cipherHelper)) {
                    delegCreds = new KrbCred(credBytes, EncryptionKey.NULL_KEY).getDelegatedCreds()[0];
                } else {
                    delegCreds = new KrbCred(credBytes, key).getDelegatedCreds()[0];
                }
            }
        }
