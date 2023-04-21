    private long computeInterfaceHash() {
        long hash = 0;
        ByteArrayOutputStream sink = new ByteArrayOutputStream(512);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            DataOutputStream out = new DataOutputStream(new DigestOutputStream(sink, md));
            out.writeInt(INTERFACE_HASH_STUB_VERSION);
            for (int i = 0; i < remoteMethods.length; i++) {
                MemberDefinition m = remoteMethods[i].getMemberDefinition();
                Identifier name = m.getName();
                Type type = m.getType();
                out.writeUTF(name.toString());
                out.writeUTF(type.getTypeSignature());
                ClassDeclaration exceptions[] = m.getExceptions(env);
                sortClassDeclarations(exceptions);
                for (int j = 0; j < exceptions.length; j++) {
                    out.writeUTF(Names.mangleClass(exceptions[j].getName()).toString());
                }
            }
            out.flush();
            byte hashArray[] = md.digest();
            for (int i = 0; i < Math.min(8, hashArray.length); i++) {
                hash += ((long) (hashArray[i] & 0xFF)) << (i * 8);
            }
        } catch (IOException e) {
            throw new Error("unexpected exception computing intetrface hash: " + e);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("unexpected exception computing intetrface hash: " + e);
        }
        return hash;
    }
