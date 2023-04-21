    public static String getGuid(InetAddress ip, int port, PeerType type) throws IntegrityException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256", "BC");
            md.update(ip.getAddress());
            md.update((byte) ((port & 0xff00) >> 8));
            md.update((byte) (port & 0xff));
            byte[] hash = md.digest(peerTypeToString(type).getBytes());
            BigInteger x = new BigInteger(hash);
            if (x.signum() == -1) {
                x = MAX_VALUE_INTEGER.add(x);
            }
            return x.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new IntegrityException("error creating guid: SHA-256 algorithm not found", e);
        } catch (NoSuchProviderException e) {
            throw new IntegrityException("error creating guid: BouncyCastle provider not found", e);
        }
    }
