    public long calcSerialVersionUID() {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            GlobalOptions.err.println("Can't calculate serialVersionUID");
            return 0L;
        }
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
            out.writeUTF(info.getName());
            int modifiers = info.getModifiers();
            modifiers = modifiers & (Modifier.ABSTRACT | Modifier.FINAL | Modifier.INTERFACE | Modifier.PUBLIC);
            out.writeInt(modifiers);
            ClassInfo[] interfaces = (ClassInfo[]) info.getInterfaces().clone();
            Arrays.sort(interfaces, new Comparator() {

                public int compare(Object o1, Object o2) {
                    return ((ClassInfo) o1).getName().compareTo(((ClassInfo) o2).getName());
                }
            });
            for (int i = 0; i < interfaces.length; i++) {
                out.writeUTF(interfaces[i].getName());
            }
            Comparator identCmp = new Comparator() {

                public int compare(Object o1, Object o2) {
                    Identifier i1 = (Identifier) o1;
                    Identifier i2 = (Identifier) o2;
                    boolean special1 = (i1.equals("<init>") || i1.equals("<clinit>"));
                    boolean special2 = (i2.equals("<init>") || i2.equals("<clinit>"));
                    if (special1 != special2) {
                        return special1 ? -1 : 1;
                    }
                    int comp = i1.getName().compareTo(i2.getName());
                    if (comp != 0) {
                        return comp;
                    } else {
                        return i1.getType().compareTo(i2.getType());
                    }
                }
            };
            List fields = Arrays.asList(fieldIdents.toArray());
            List methods = Arrays.asList(methodIdents.toArray());
            Collections.sort(fields, identCmp);
            Collections.sort(methods, identCmp);
            for (Iterator i = fields.iterator(); i.hasNext(); ) {
                FieldIdentifier field = (FieldIdentifier) i.next();
                modifiers = field.info.getModifiers();
                if ((modifiers & Modifier.PRIVATE) != 0 && (modifiers & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) continue;
                out.writeUTF(field.getName());
                out.writeInt(modifiers);
                out.writeUTF(field.getType());
            }
            for (Iterator i = methods.iterator(); i.hasNext(); ) {
                MethodIdentifier method = (MethodIdentifier) i.next();
                modifiers = method.info.getModifiers();
                if (Modifier.isPrivate(modifiers)) continue;
                out.writeUTF(method.getName());
                out.writeInt(modifiers);
                out.writeUTF(method.getType().replace('/', '.'));
            }
            out.close();
            byte[] sha = md.digest();
            long result = 0;
            for (int i = 0; i < 8; i++) {
                result += (long) (sha[i] & 0xFF) << (8 * i);
            }
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
            GlobalOptions.err.println("Can't calculate serialVersionUID");
            return 0L;
        }
    }
