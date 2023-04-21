    private void writeSignatureFile(OutputStream out) throws IOException, GeneralSecurityException {
        Manifest sf = new Manifest();
        Attributes main = sf.getMainAttributes();
        main.putValue("Signature-Version", "1.0");
        main.putValue("Created-By", "1.0 (Android)");
        BASE64Encoder base64 = new BASE64Encoder();
        MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
        PrintStream print = new PrintStream(new DigestOutputStream(new ByteArrayOutputStream(), md), true, "UTF-8");
        mManifest.write(print);
        print.flush();
        main.putValue(DIGEST_MANIFEST_ATTR, base64.encode(md.digest()));
        Map<String, Attributes> entries = mManifest.getEntries();
        for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
            print.print("Name: " + entry.getKey() + "\r\n");
            for (Map.Entry<Object, Object> att : entry.getValue().entrySet()) {
                print.print(att.getKey() + ": " + att.getValue() + "\r\n");
            }
            print.print("\r\n");
            print.flush();
            Attributes sfAttr = new Attributes();
            sfAttr.putValue(DIGEST_ATTR, base64.encode(md.digest()));
            sf.getEntries().put(entry.getKey(), sfAttr);
        }
        sf.write(out);
    }
