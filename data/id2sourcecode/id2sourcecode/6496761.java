        public long computeSerialVersionUID(JavaClass javaClass) {
            try {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                DataOutputStream dout = new DataOutputStream(bout);
                Method[] methods = javaClass.getMethods();
                Field[] fields = javaClass.getFields();
                dout.writeUTF(javaClass.getClassName());
                int classMods = javaClass.getAccessFlags();
                classMods &= (Modifier.PUBLIC | Modifier.FINAL | Modifier.INTERFACE | Modifier.ABSTRACT);
                if ((classMods & Modifier.INTERFACE) != 0) {
                    classMods = (methods.length > 0) ? (classMods | Modifier.ABSTRACT) : (classMods & ~Modifier.ABSTRACT);
                }
                dout.writeInt(classMods);
                if (true) {
                    String[] ifaceNames = javaClass.getInterfaceNames();
                    Arrays.sort(ifaceNames);
                    for (int i = 0; i < ifaceNames.length; i++) {
                        dout.writeUTF(ifaceNames[i]);
                    }
                }
                Arrays.sort(fields, FIELD_OR_METHOD_COMPARATOR);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    int mods = fields[i].getAccessFlags();
                    if (((mods & Modifier.PRIVATE) == 0) || ((mods & (Modifier.STATIC | Modifier.TRANSIENT)) == 0)) {
                        dout.writeUTF(field.getName());
                        dout.writeInt(mods);
                        dout.writeUTF(field.getSignature());
                    }
                }
                Arrays.sort(methods, FIELD_OR_METHOD_COMPARATOR);
                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];
                    int mods = method.getAccessFlags();
                    if ((mods & Modifier.PRIVATE) == 0) {
                        dout.writeUTF(method.getName());
                        dout.writeInt(mods);
                        dout.writeUTF(method.getSignature().replace('/', '.'));
                    }
                }
                dout.flush();
                MessageDigest md = MessageDigest.getInstance("SHA");
                byte[] hashBytes = md.digest(bout.toByteArray());
                long hash = 0;
                for (int i = Math.min(hashBytes.length, 8) - 1; i >= 0; i--) {
                    hash = (hash << 8) | (hashBytes[i] & 0xFF);
                }
                return hash;
            } catch (IOException ex) {
                throw new InternalError();
            } catch (NoSuchAlgorithmException ex) {
                throw new SecurityException(ex.getMessage());
            }
        }
