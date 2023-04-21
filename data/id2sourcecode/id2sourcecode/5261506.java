    byte[] doFinal() {
        if (first == true) {
            md.update(secret);
            md.update(pad1);
        } else {
            first = true;
        }
        try {
            byte[] tmp = md.digest();
            md.update(secret);
            md.update(pad2);
            md.update(tmp);
            md.digest(tmp, 0, tmp.length);
            return tmp;
        } catch (DigestException e) {
            throw new ProviderException(e);
        }
    }
