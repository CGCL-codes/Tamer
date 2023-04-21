    public final long computeSUID(final boolean skipCLINIT) {
        long result = m_declaredSUID;
        if (result != 0L) return result; else {
            try {
                final ByteArrayOStream bout = new ByteArrayOStream(1024);
                final DataOutputStream dout = new DataOutputStream(bout);
                dout.writeUTF(Types.vmNameToJavaName(getName()));
                {
                    final int[] nestedAccessFlags = new int[1];
                    final int modifiers = (isNested(nestedAccessFlags) ? nestedAccessFlags[0] : getAccessFlags()) & (ACC_PUBLIC | ACC_FINAL | ACC_INTERFACE | ACC_ABSTRACT);
                    dout.writeInt(modifiers);
                }
                {
                    final IInterfaceCollection interfaces = getInterfaces();
                    final String[] ifcs = new String[interfaces.size()];
                    final int iLimit = ifcs.length;
                    for (int i = 0; i < iLimit; ++i) {
                        ifcs[i] = Types.vmNameToJavaName(((CONSTANT_Class_info) m_constants.get(interfaces.get(i))).getName(this));
                    }
                    Arrays.sort(ifcs);
                    for (int i = 0; i < iLimit; ++i) {
                        dout.writeUTF(ifcs[i]);
                    }
                }
                {
                    final IFieldCollection fields = getFields();
                    final FieldDescriptor[] fds = new FieldDescriptor[fields.size()];
                    int fcount = 0;
                    for (int f = 0, fLimit = fds.length; f < fLimit; ++f) {
                        final Field_info field = fields.get(f);
                        final int modifiers = field.getAccessFlags();
                        if (((modifiers & ACC_PRIVATE) == 0) || ((modifiers & (ACC_STATIC | ACC_TRANSIENT)) == 0)) fds[fcount++] = new FieldDescriptor(field.getName(this), modifiers, field.getDescriptor(this));
                    }
                    if (fcount > 0) {
                        Arrays.sort(fds, 0, fcount);
                        for (int i = 0; i < fcount; ++i) {
                            final FieldDescriptor fd = fds[i];
                            dout.writeUTF(fd.m_name);
                            dout.writeInt(fd.m_modifiers);
                            dout.writeUTF(fd.m_descriptor);
                        }
                    }
                }
                {
                    final IMethodCollection methods = getMethods();
                    boolean hasCLINIT = false;
                    final ConstructorDescriptor[] cds = new ConstructorDescriptor[methods.size()];
                    final MethodDescriptor[] mds = new MethodDescriptor[cds.length];
                    int ccount = 0, mcount = 0;
                    for (int i = 0, iLimit = cds.length; i < iLimit; ++i) {
                        final Method_info method = methods.get(i);
                        final String name = method.getName(this);
                        if (!hasCLINIT && IClassDefConstants.CLINIT_NAME.equals(name)) {
                            hasCLINIT = true;
                            continue;
                        } else {
                            final int modifiers = method.getAccessFlags();
                            if ((modifiers & ACC_PRIVATE) == 0) {
                                if (IClassDefConstants.INIT_NAME.equals(name)) cds[ccount++] = new ConstructorDescriptor(modifiers, method.getDescriptor(this)); else mds[mcount++] = new MethodDescriptor(name, modifiers, method.getDescriptor(this));
                            }
                        }
                    }
                    if (hasCLINIT && !skipCLINIT) {
                        dout.writeUTF(IClassDefConstants.CLINIT_NAME);
                        dout.writeInt(ACC_STATIC);
                        dout.writeUTF(IClassDefConstants.CLINIT_DESCRIPTOR);
                    }
                    if (ccount > 0) {
                        Arrays.sort(cds, 0, ccount);
                        for (int i = 0; i < ccount; ++i) {
                            final ConstructorDescriptor cd = cds[i];
                            dout.writeUTF(IClassDefConstants.INIT_NAME);
                            dout.writeInt(cd.m_modifiers);
                            dout.writeUTF(cd.m_descriptor.replace('/', '.'));
                        }
                    }
                    if (mcount > 0) {
                        Arrays.sort(mds, 0, mcount);
                        for (int i = 0; i < mcount; ++i) {
                            final MethodDescriptor md = mds[i];
                            dout.writeUTF(md.m_name);
                            dout.writeInt(md.m_modifiers);
                            dout.writeUTF(md.m_descriptor.replace('/', '.'));
                        }
                    }
                }
                dout.flush();
                if (DEBUG_SUID) {
                    byte[] dump = bout.copyByteArray();
                    for (int x = 0; x < dump.length; ++x) {
                        System.out.println("DUMP[" + x + "] = " + dump[x] + "\t" + (char) dump[x]);
                    }
                }
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(bout.getByteArray(), 0, bout.size());
                final byte[] hash = md.digest();
                if (DEBUG_SUID) {
                    for (int x = 0; x < hash.length; ++x) {
                        System.out.println("HASH[" + x + "] = " + hash[x]);
                    }
                }
                for (int i = Math.min(hash.length, 8) - 1; i >= 0; --i) {
                    result = (result << 8) | (hash[i] & 0xFF);
                }
                return result;
            } catch (IOException ioe) {
                throw new Error(ioe.getMessage());
            } catch (NoSuchAlgorithmException nsae) {
                throw new SecurityException(nsae.getMessage());
            }
        }
    }
