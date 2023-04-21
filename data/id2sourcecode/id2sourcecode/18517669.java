    public SignatureFile(MessageDigest digests[], Manifest mf, ManifestDigester md, String baseName, boolean signManifest) {
        this.baseName = baseName;
        String version = System.getProperty("java.version");
        String javaVendor = System.getProperty("java.vendor");
        sf = new Manifest();
        Attributes mattr = sf.getMainAttributes();
        BASE64Encoder encoder = new JarBASE64Encoder();
        mattr.putValue(Attributes.Name.SIGNATURE_VERSION.toString(), "1.0");
        mattr.putValue("Created-By", version + " (" + javaVendor + ")");
        if (signManifest) {
            for (int i = 0; i < digests.length; i++) {
                mattr.putValue(digests[i].getAlgorithm() + "-Digest-Manifest", encoder.encode(md.manifestDigest(digests[i])));
            }
        }
        ManifestDigester.Entry mde = md.get(ManifestDigester.MF_MAIN_ATTRS, false);
        if (mde != null) {
            for (int i = 0; i < digests.length; i++) {
                mattr.putValue(digests[i].getAlgorithm() + "-Digest-" + ManifestDigester.MF_MAIN_ATTRS, encoder.encode(mde.digest(digests[i])));
            }
        } else {
            throw new IllegalStateException("ManifestDigester failed to create " + "Manifest-Main-Attribute entry");
        }
        Map<String, Attributes> entries = sf.getEntries();
        Iterator<Map.Entry<String, Attributes>> mit = mf.getEntries().entrySet().iterator();
        while (mit.hasNext()) {
            Map.Entry<String, Attributes> e = mit.next();
            String name = e.getKey();
            mde = md.get(name, false);
            if (mde != null) {
                Attributes attr = new Attributes();
                for (int i = 0; i < digests.length; i++) {
                    attr.putValue(digests[i].getAlgorithm() + "-Digest", encoder.encode(mde.digest(digests[i])));
                }
                entries.put(name, attr);
            }
        }
    }
