    synchronized long generateSerialNumber(String material) {
        md.reset();
        md.update(material.getBytes());
        byte[] digest = md.digest();
        return new BigInteger(digest).longValue();
    }
