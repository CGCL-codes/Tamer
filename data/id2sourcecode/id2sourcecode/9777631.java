    public UUID generateNameBasedUUID(UUID nameSpaceUUID, String name, MessageDigest digest) {
        digest.reset();
        if (nameSpaceUUID != null) {
            digest.update(nameSpaceUUID.asByteArray());
        }
        digest.update(name.getBytes());
        return new UUID(UUID.TYPE_NAME_BASED, digest.digest());
    }
