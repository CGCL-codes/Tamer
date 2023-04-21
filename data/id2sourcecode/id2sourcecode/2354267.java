    private void writeEntry(InputStream input, JarEntry entry) throws IOException {
        mOutputJar.putNextEntry(entry);
        int count;
        while ((count = input.read(mBuffer)) != -1) {
            mOutputJar.write(mBuffer, 0, count);
            if (mMessageDigest != null) {
                mMessageDigest.update(mBuffer, 0, count);
            }
        }
        mOutputJar.closeEntry();
        if (mManifest != null) {
            Attributes attr = mManifest.getAttributes(entry.getName());
            if (attr == null) {
                attr = new Attributes();
                mManifest.getEntries().put(entry.getName(), attr);
            }
            attr.putValue(DIGEST_ATTR, mBase64Encoder.encode(mMessageDigest.digest()));
        }
    }
