    public SignatureFile(MessageDigest digests[], Manifest mf, ManifestDigester md, String baseName, boolean signManifest) {
        this.baseName = baseName;
        String version = System.getProperty("java.version");
        String javaVendor = System.getProperty("java.vendor");
        sf = new Manifest();
        Attributes mattr = sf.getMainAttributes();
        mattr.putValue(java.util.jar.Attributes.Name.SIGNATURE_VERSION.toString(), "1.0");
        mattr.putValue("Created-By", version + " (" + javaVendor + ")");
        if (signManifest) {
            for (int i = 0; i < digests.length; i++) mattr.putValue(digests[i].getAlgorithm() + "-Digest-Manifest", new String(Base64.encode(md.manifestDigest(digests[i]))));
        }
        Map<String, Attributes> entries = sf.getEntries();
        Iterator<Entry<String, Attributes>> mit = mf.getEntries().entrySet().iterator();
        do {
            if (!mit.hasNext()) break;
            java.util.Map.Entry<String, Attributes> e = mit.next();
            String name = (String) e.getKey();
            ManifestDigester.Entry mde = md.get(name, false);
            if (mde != null) {
                Attributes attr = new Attributes();
                for (int i = 0; i < digests.length; i++) attr.putValue(digests[i].getAlgorithm() + "-Digest", new String(Base64.encode(mde.digest(digests[i]))));
                entries.put(name, attr);
            }
        } while (true);
    }
