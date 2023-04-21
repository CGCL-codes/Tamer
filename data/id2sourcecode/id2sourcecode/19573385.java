    String renameFieldSignature(String sig) {
        if (sig == null) {
            return null;
        }
        SignatureReader reader = new SignatureReader(sig);
        SignatureWriter writer = new SignatureWriter();
        reader.acceptType(new RenameSignatureAdapter(writer));
        sig = writer.toString();
        return sig;
    }
