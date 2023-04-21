    public PasswordHash(byte[] pw, int type, byte[] salt) throws NoSuchAlgorithmException {
        byte[] binary;
        if (pw == null) {
            throw new NullPointerException("pw");
        }
        type_ = type;
        switch(type_) {
            case TYPE_SHA:
                md_ = MessageDigest.getInstance("SHA-1");
                md_.update(pw);
                hash_ = md_.digest();
                md_.reset();
                salt_ = new byte[0];
                str_ = "{SHA}" + Base64.encode(hash_);
                break;
            case TYPE_SSHA:
                if (salt == null) {
                    throw new NullPointerException("salt");
                }
                salt_ = salt;
                md_ = MessageDigest.getInstance("SHA-1");
                md_.update(pw);
                md_.update(salt);
                hash_ = md_.digest();
                md_.reset();
                binary = new byte[hash_.length + salt_.length];
                System.arraycopy(hash_, 0, binary, 0, hash_.length);
                System.arraycopy(salt_, 0, binary, hash_.length, salt_.length);
                str_ = "{SSHA}" + Base64.encode(binary);
                break;
            case TYPE_MD5:
                md_ = MessageDigest.getInstance("MD5");
                md_.update(pw);
                hash_ = md_.digest();
                md_.reset();
                salt_ = new byte[0];
                str_ = "{MD5}" + Base64.encode(hash_);
                break;
            case TYPE_SMD5:
                if (salt == null) {
                    throw new NullPointerException("salt");
                }
                salt_ = salt;
                md_ = MessageDigest.getInstance("MD5");
                md_.update(pw);
                md_.update(salt);
                hash_ = md_.digest();
                md_.reset();
                binary = new byte[hash_.length + salt_.length];
                System.arraycopy(hash_, 0, binary, 0, hash_.length);
                System.arraycopy(salt_, 0, binary, hash_.length, salt_.length);
                str_ = "{SMD5}" + Base64.encode(binary);
                break;
            default:
                throw new NoSuchAlgorithmException("Unrecognized algorithm");
        }
    }
