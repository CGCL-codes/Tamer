    private static ClassInfo _createIntrospectionInfo(Class<?> objClass) {
        if (objClass == Object.class) return null;
        Map<String, PropertyReader> readers = null;
        Map<String, PropertyWriter> writers = null;
        if (!Modifier.isPublic(objClass.getModifiers())) {
            Class<?> superClass = objClass.getSuperclass();
            if (superClass != null) {
                ClassInfo superClassInfo = _getIntrospectionInfo(superClass);
                if (superClassInfo != null) {
                    readers = new HashMap<String, PropertyReader>();
                    writers = new HashMap<String, PropertyWriter>();
                    superClassInfo.putAllReadersInto(readers);
                    superClassInfo.putAllWritersInto(writers);
                }
            }
            Class[] interfaces = objClass.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                ClassInfo interfaceInfo = _getIntrospectionInfo(interfaces[i]);
                if (interfaceInfo != null) {
                    if (readers == null) readers = new HashMap<String, PropertyReader>();
                    interfaceInfo.putAllReadersInto(readers);
                    if (writers == null) writers = new HashMap<String, PropertyWriter>();
                    interfaceInfo.putAllWritersInto(writers);
                }
            }
        } else {
            readers = new HashMap<String, PropertyReader>();
            writers = new HashMap<String, PropertyWriter>();
            try {
                BeanInfo info = JavaIntrospector.getBeanInfo(objClass);
                PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
                if (descriptors != null) {
                    for (int i = 0; i < descriptors.length; i++) {
                        String name = descriptors[i].getName();
                        Method reader = descriptors[i].getReadMethod();
                        Method writer = descriptors[i].getWriteMethod();
                        if (name != null) {
                            if (reader != null) {
                                readers.put(name, new MethodReader(reader));
                            }
                            if (writer != null) {
                                writers.put(name, new MethodWriter(writer));
                            }
                        }
                    }
                }
                Field[] fields = objClass.getFields();
                for (int i = 0; i < fields.length; i++) {
                    FieldReaderWriter frw = new FieldReaderWriter(fields[i]);
                    readers.put(fields[i].getName(), frw);
                    writers.put(fields[i].getName(), frw);
                }
            } catch (IntrospectionException ie) {
                _LOG.severe(ie);
            }
        }
        if (readers == null && writers == null) return null;
        return new ClassInfo(readers, writers);
    }
