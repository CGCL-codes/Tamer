        private Wrapper() throws SaslException {
            try {
                byte[] serverSalt = "Digest session key to server-to-client signing key magic constant".getBytes("ISO-8859-1");
                byte[] clientSalt = "Digest session key to client-to-server signing key magic constant".getBytes("ISO-8859-1");
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] temp = new byte[MD5DigestSessionKey.length + clientSalt.length];
                System.arraycopy(MD5DigestSessionKey, 0, temp, 0, MD5DigestSessionKey.length);
                System.arraycopy(serverSalt, 0, temp, MD5DigestSessionKey.length, serverSalt.length);
                kis = md5.digest(temp);
                System.arraycopy(clientSalt, 0, temp, MD5DigestSessionKey.length, clientSalt.length);
                kic = md5.digest(temp);
                ByteUtils.getNetworkByteOrderFromInt(1, messageTypeNBO, 0, 2);
            } catch (UnsupportedEncodingException e) {
                throw new SaslException(e.getMessage(), e);
            } catch (GeneralSecurityException e) {
                throw new SaslException(e.getMessage(), e);
            }
        }
