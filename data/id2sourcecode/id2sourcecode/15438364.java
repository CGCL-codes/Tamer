    public String scanHashcode(String _pvd, String _vf) throws ScanException {
        String pvd = _pvd;
        String vf = _vf;
        if (pvd == null) {
            pvd = scanPrimaryVolumeDescriptor();
        }
        if (vf == null) {
            vf = scanVolumeFiles();
        }
        String hashcode = null;
        try {
            vf.getBytes();
            byte[] hash = MessageDigest.getInstance("MD5").digest(pvd.getBytes("UTF-8"));
            BASE64Encoder encoder = new BASE64Encoder();
            hashcode = encoder.encodeBuffer(hash);
            hashcode = hashcode.trim();
        } catch (NoSuchAlgorithmException e) {
            throw new ScanException("Can not found MD5 algorithm. This is necessary to count hashcode. " + e);
        } catch (UnsupportedEncodingException e) {
            throw new ScanException("Can not found UTF-8 charset. This is necessary to count hashcode. " + e);
        }
        return hashcode;
    }
