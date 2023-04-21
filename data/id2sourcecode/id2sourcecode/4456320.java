    public SHA1 digest(BEncType o) {
        md.reset();
        encode(o);
        return new SHA1(md.digest());
    }
