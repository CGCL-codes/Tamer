    byte[] doFinal() {
        if (first == true) {
            md.update(k_ipad);
        } else {
            first = true;
        }
        try {
            byte[] tmp = md.digest();
            md.update(k_opad);
            md.update(tmp);
            md.digest(tmp, 0, tmp.length);
            return tmp;
        } catch (DigestException e) {
            throw new ProviderException(e);
        }
    }
