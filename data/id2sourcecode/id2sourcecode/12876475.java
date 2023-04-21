    private static byte[] computeAddressHash() {
        try {
            final byte[] addr = InetAddress.getLocalHost().getAddress();
            byte[] addrHash;
            final int addrHashLength = 8;
            final MessageDigest md = MessageDigest.getInstance("SHA");
            final ByteArrayOutputStream sink = new ByteArrayOutputStream(64);
            final DataOutputStream out = new DataOutputStream(new DigestOutputStream(sink, md));
            out.write(addr, 0, addr.length);
            out.flush();
            final byte digest[] = md.digest();
            final int hashlength = Math.min(addrHashLength, digest.length);
            addrHash = new byte[hashlength];
            System.arraycopy(digest, 0, addrHash, 0, hashlength);
            return addrHash;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
