    private static byte[] ChallengeHash(final byte[] PeerChallenge, final byte[] AuthenticatorChallenge, final byte[] UserName) {
        byte Challenge[] = new byte[8];
        IMessageDigest md = HashFactory.getInstance("SHA-1");
        md.update(PeerChallenge, 0, 16);
        md.update(AuthenticatorChallenge, 0, 16);
        md.update(UserName, 0, UserName.length);
        System.arraycopy(md.digest(), 0, Challenge, 0, 8);
        return Challenge;
    }
