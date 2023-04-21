    public int compareTo(PublisherPublicKeyDigest o) {
        int result = DataUtils.compare(this.digest(), o.digest());
        return result;
    }
