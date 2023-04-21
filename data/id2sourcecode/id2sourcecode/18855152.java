    public UUID generateNameBasedUUID(UUID aNameSpaceUUID, String aName, MessageDigest aHash) {
        aHash.reset();
        if (aNameSpaceUUID != null) {
            aHash.update(aNameSpaceUUID.asByteArray());
        }
        aHash.update(aName.getBytes());
        return new UUID(UUID.TYPE_NAME_BASED, aHash.digest());
    }
