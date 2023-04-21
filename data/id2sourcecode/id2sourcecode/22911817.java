    private String getUnitDir(String aProject, EDataKind aKind, String aName, String aFile) {
        String vStatic = root + aProject + File.separator + "unit" + File.separator + aKind.value() + File.separator;
        String vDynamic = aName + aFile;
        if (aFile != null && aFile.length() != 0) {
            vDynamic += "/" + aFile;
        }
        MessageDigest m;
        try {
            m = MessageDigest.getInstance(DIGEST);
            vDynamic = encoder.encode(m.digest(vDynamic.getBytes())).replace(File.separator, SEPARATOR);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return vStatic + vDynamic;
    }
