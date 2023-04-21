    public long getSerialVersionUID() throws NoSuchAlgorithmException {
        if (status < DECLARATIONS) throw new IllegalStateException("status is " + status);
        FieldInfo fi = findField("serialVersionUID", "J");
        if (fi != null && ((fi.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) == (Modifier.STATIC | Modifier.FINAL)) && fi.getConstant() != null) return ((Long) fi.getConstant()).longValue();
        final MessageDigest md = MessageDigest.getInstance("SHA");
        OutputStream digest = new OutputStream() {

            public void write(int b) {
                md.update((byte) b);
            }

            public void write(byte[] data, int offset, int length) {
                md.update(data, offset, length);
            }
        };
        DataOutputStream out = new DataOutputStream(digest);
        try {
            out.writeUTF(this.name);
            int modifs = javaModifiersToBytecode(this.modifiers) & (Modifier.ABSTRACT | Modifier.FINAL | Modifier.INTERFACE | Modifier.PUBLIC);
            out.writeInt(modifs);
            ClassInfo[] interfaces = (ClassInfo[]) this.interfaces.clone();
            Arrays.sort(interfaces);
            for (int i = 0; i < interfaces.length; i++) out.writeUTF(interfaces[i].name);
            FieldInfo[] fields = (FieldInfo[]) this.fields.clone();
            Arrays.sort(fields);
            for (int i = 0; i < fields.length; i++) {
                modifs = fields[i].getModifiers();
                if ((modifs & Modifier.PRIVATE) != 0 && (modifs & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) continue;
                out.writeUTF(fields[i].getName());
                out.writeInt(modifs);
                out.writeUTF(fields[i].getType());
            }
            MethodInfo[] methods = (MethodInfo[]) this.methods.clone();
            Arrays.sort(methods);
            for (int i = 0; i < methods.length; i++) {
                modifs = methods[i].getModifiers();
                if (methods[i].getName().equals("<clinit>")) modifs = Modifier.STATIC;
                if ((modifs & Modifier.PRIVATE) != 0) continue;
                out.writeUTF(methods[i].getName());
                out.writeInt(modifs);
                out.writeUTF(methods[i].getType().replace('/', '.'));
            }
            out.close();
            byte[] sha = md.digest();
            long result = 0;
            for (int i = 0; i < 8; i++) {
                result += (long) (sha[i] & 0xFF) << (8 * i);
            }
            return result;
        } catch (IOException ex) {
            throw new InternalError();
        }
    }
