    public Key createGroupPublicKey(MembershipListObject ml) throws ContentEncodingException, IOException, InvalidKeyException {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance(_groupManager.getGroupKeyAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            if (_groupManager.getGroupKeyAlgorithm().equals(GroupAccessControlManager.DEFAULT_GROUP_KEY_ALGORITHM)) {
                if (Log.isLoggable(Log.FAC_ACCESSCONTROL, Level.SEVERE)) {
                    Log.severe(Log.FAC_ACCESSCONTROL, "Cannot find default group public key algorithm: " + GroupAccessControlManager.DEFAULT_GROUP_KEY_ALGORITHM + ": " + e.getMessage());
                }
                throw new RuntimeException("Cannot find default group public key algorithm: " + GroupAccessControlManager.DEFAULT_GROUP_KEY_ALGORITHM + ": " + e.getMessage());
            }
            throw new InvalidKeyException("Specified group public key algorithm " + _groupManager.getGroupKeyAlgorithm() + " not found. " + e.getMessage());
        }
        kpg.initialize(GroupAccessControlManager.DEFAULT_GROUP_KEY_LENGTH);
        KeyPair pair = kpg.generateKeyPair();
        _groupPublicKey = new PublicKeyObject(GroupAccessControlProfile.groupPublicKeyName(_groupNamespace, _groupFriendlyName), pair.getPublic(), SaveType.REPOSITORY, _handle);
        _groupPublicKey.save();
        _groupPublicKey.updateInBackground(true);
        stopPrivateKeyDirectoryEnumeration();
        _privKeyDirectory = null;
        PrincipalKeyDirectory newPrivateKeyDirectory = privateKeyDirectory(_groupManager.getAccessManager());
        Key privateKeyWrappingKey = WrappedKey.generateNonceKey();
        try {
            newPrivateKeyDirectory.addPrivateKeyBlock(pair.getPrivate(), privateKeyWrappingKey);
        } catch (InvalidKeyException e) {
            if (Log.isLoggable(Log.FAC_ACCESSCONTROL, Level.WARNING)) {
                Log.warning(Log.FAC_ACCESSCONTROL, "Unexpected -- InvalidKeyException wrapping key with keys we just generated! " + e.getMessage());
            }
            throw e;
        }
        updateGroupPublicKey(privateKeyWrappingKey, ml.membershipList().contents());
        return privateKeyWrappingKey;
    }
