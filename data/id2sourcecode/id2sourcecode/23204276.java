    public int hashCode() {
        if (_hashCode <= 0) {
            MessageDigest md = new SHA1();
            String byteString = _message + _url + _messageType + _version + _torrent + _os + _showOnce;
            md.update(byteString.getBytes());
            byte[] digest = md.digest();
            _hashCode = 0;
            for (int n : digest) {
                _hashCode += Math.abs((int) n);
            }
        }
        return _hashCode;
    }
