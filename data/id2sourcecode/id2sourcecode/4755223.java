    private RMIObjectStreamClass(Class clz, boolean suppressPackageCheck) throws InvalidClassException {
        Class lookupClz = clz;
        addToLocalCache(lookupClz, this);
        if (org.omg.CORBA.Any.class.isAssignableFrom(clz)) {
            clz = org.omg.CORBA.Any.class;
        } else if (org.omg.CORBA.TypeCode.class.isAssignableFrom(clz)) {
            clz = org.omg.CORBA.TypeCode.class;
        }
        m_repository_id = RepoIDHelper.getRepoID(clz);
        m_repo_id_hash = RepoIDHelper.getRepoIDHash(clz);
        m_delegate = ObjectStreamClass.lookup(clz);
        if (clz.isArray()) {
            String[] names = RepoIDHelper.mangleClassName(clz);
            Class cmpt = clz.getComponentType();
            TypeCode contained = lookupTypeCode(cmpt);
            m_type = s_orb.create_value_box_tc(getRepoID(), names[1], s_orb.create_sequence_tc(0, contained));
            addToGlobalCache(lookupClz, this);
            return;
        }
        m_custom_marshaled = isCustomMarshaled(clz);
        org.omg.CORBA.TypeCode baseType = null;
        ObjectStreamField[] fields = m_delegate.getFields();
        boolean[] isPublic = new boolean[fields.length];
        int totalPublic = 0;
        RMIObjectStreamClass parent = null;
        m_fields = new Field[fields.length];
        for (int i = 0; i < m_fields.length; ++i) {
            ObjectStreamField currentField = fields[i];
            try {
                m_fields[i] = clz.getDeclaredField(currentField.getName());
                m_fields[i].setAccessible(true);
                if (Modifier.isPublic(m_fields[i].getModifiers())) {
                    isPublic[i] = true;
                    totalPublic++;
                }
            } catch (NoSuchFieldException ex) {
            }
        }
        m_idl_entity = isIDLEntity(clz);
        if (isIDLEntity()) {
            if (org.omg.CORBA.Any.class.isAssignableFrom(clz)) {
                m_type = s_anyosc.m_type;
                m_read_object_method = s_anyosc.m_read_object_method;
                m_write_object_method = s_anyosc.m_write_object_method;
            } else if (org.omg.CORBA.TypeCode.class.isAssignableFrom(clz)) {
                m_type = s_tcosc.m_type;
                m_read_object_method = s_tcosc.m_read_object_method;
                m_write_object_method = s_tcosc.m_write_object_method;
            } else {
                Class helper = UtilDelegateImpl.locateHelperClass(clz);
                if (helper == null) {
                    throw new InvalidClassException("Unable to find class '" + clz.getName() + "Helper'", "ClassNotFoundException");
                }
                String[] names = RepoIDHelper.mangleClassName(clz);
                m_type = s_orb.create_value_box_tc(getRepoID(), names[1], getTypeCodeFromHelper(helper));
                try {
                    m_read_object_method = helper.getMethod("read", new Class[] { org.omg.CORBA.portable.InputStream.class });
                    m_write_object_method = helper.getMethod("write", new Class[] { org.omg.CORBA.portable.OutputStream.class, clz });
                } catch (NoSuchMethodException ex) {
                    throw new InvalidClassException(helper.getName(), "Couldn't find read/write method (" + ex + ")");
                }
            }
        } else {
            if (java.io.Externalizable.class.isAssignableFrom(clz)) {
                Class base = clz.getSuperclass();
                while (java.io.Serializable.class.isAssignableFrom(base) && parent == null) {
                    try {
                        parent = lookup(base);
                    } catch (InvalidClassException ex) {
                        try {
                            parent = new RMIObjectStreamClass(base, true);
                        } catch (InvalidClassException ex1) {
                            base = base.getSuperclass();
                        }
                    }
                }
                if (parent != null) {
                    baseType = parent.m_type;
                }
                if (!suppressPackageCheck) {
                    try {
                        clz.getConstructor(new Class[0]);
                    } catch (NoSuchMethodException ex) {
                        throw new InvalidClassException(base.getName() + "Missing no-arg constructor for class (" + ex + ")");
                    }
                }
                m_externalizable = true;
                m_class_base = clz;
                m_fields = null;
            } else {
                m_class_base = clz.getSuperclass();
                if (m_class_base != null && java.io.Serializable.class.isAssignableFrom(m_class_base)) {
                    try {
                        parent = lookup(m_class_base);
                    } catch (InvalidClassException ex) {
                        parent = new RMIObjectStreamClass(m_class_base, true);
                    }
                }
                if (parent != null) {
                    baseType = parent.m_type;
                    m_class_base = parent.m_class_base;
                    m_all_classes = new RMIObjectStreamClass[parent.m_all_classes.length + 1];
                    System.arraycopy(parent.m_all_classes, 0, m_all_classes, 0, parent.m_all_classes.length);
                    m_all_classes[parent.m_all_classes.length] = this;
                } else {
                    m_all_classes = new RMIObjectStreamClass[] { this };
                }
                if (m_class_base != null && !m_class_base.equals(java.lang.Object.class)) {
                    java.lang.reflect.Constructor ctor;
                    try {
                        ctor = m_class_base.getDeclaredConstructor(new Class[0]);
                    } catch (NoSuchMethodException ex) {
                        throw new InvalidClassException(m_class_base.getName() + "Missing no-arg constructor for class (" + ex + ")");
                    }
                    int modf = ctor.getModifiers();
                    if (Modifier.isPrivate(modf) || !(suppressPackageCheck || Modifier.isPublic(modf) || Modifier.isProtected(modf) || m_class_base.getPackage().equals(clz.getPackage()))) {
                        throw new InvalidClassException(m_class_base.getName(), "IllegalAccessException");
                    }
                }
                if (!clz.isArray()) {
                    m_read_object_method = ReflectionUtils.getReadObjectMethod(clz);
                    m_write_object_method = ReflectionUtils.getWriteObjectMethod(clz);
                    m_write_replace_method = ReflectionUtils.getWriteReplaceMethod(clz);
                    m_read_resolve_method = ReflectionUtils.getReadResolveMethod(clz);
                }
            }
            short type_modifier = isCustomMarshaled() ? org.omg.CORBA.VM_CUSTOM.value : org.omg.CORBA.VM_NONE.value;
            if (baseType == null) {
                baseType = s_orb.get_primitive_tc(TCKind.tk_null);
            }
            org.omg.CORBA.ValueMember[] tcMembers = new org.omg.CORBA.ValueMember[m_externalizable ? totalPublic : fields.length];
            if (fields.length > 0) {
                String[] memberNames = new String[tcMembers.length];
                int upto = 0;
                for (int i = 0; i < fields.length; ++i) {
                    if (!m_externalizable || isPublic[i]) {
                        tcMembers[upto] = new org.omg.CORBA.ValueMember();
                        memberNames[upto] = fields[i].getName();
                        Class fType = fields[i].getType();
                        RMIObjectStreamClass sc = (RMIObjectStreamClass) getFromCache(fType);
                        if (sc != null) {
                            if (sc.type() == null) {
                                tcMembers[upto].type = s_orb.create_recursive_tc(sc.getRepoID());
                            } else {
                                tcMembers[upto].type = sc.type();
                            }
                        } else {
                            tcMembers[upto].type = lookupTypeCode(fType);
                        }
                        tcMembers[upto].access = isPublic[i] ? org.omg.CORBA.PUBLIC_MEMBER.value : org.omg.CORBA.PRIVATE_MEMBER.value;
                        ++upto;
                    }
                }
                RepoIDHelper.mangleMemberNames(clz, memberNames);
                for (int i = 0; i < memberNames.length; ++i) {
                    tcMembers[i].name = memberNames[i];
                }
            }
            try {
                m_type = s_orb.create_value_tc(getRepoID(), "", type_modifier, baseType, tcMembers);
            } catch (org.omg.CORBA.BAD_PARAM ex) {
                throw new InvalidClassException(clz.getName(), "Duplicate member name (" + ex + ")");
            }
        }
        addToGlobalCache(lookupClz, this);
    }
