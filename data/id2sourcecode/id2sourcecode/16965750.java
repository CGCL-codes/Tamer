    public BeanInvocationHandler(Object subject, Class[] ifaceArray, boolean fallback) {
        m_fallback = fallback;
        m_subject = subject;
        m_ifaceSet = new HashSet();
        m_properties = new HashMap();
        m_readMethodToPropertyMap = new HashMap();
        m_writeMethodToPropertyMap = new HashMap();
        for (int i = 0; i < ifaceArray.length; i++) {
            m_ifaceSet.addAll(ReflectionHelper.getAllInterfacesAsSet(ifaceArray[i]));
        }
        m_ifaceMethodToSubjectMap = (subject != null) ? ReflectionHelper.mapInterfacesToClass(subject.getClass(), m_ifaceSet) : Collections.EMPTY_MAP;
        Set methodsToBeSupported = ReflectionHelper.getAllDeclaredMethods(m_ifaceSet);
        m_subjectImplementedMethods = new HashSet();
        for (Iterator i = m_ifaceSet.iterator(); i.hasNext(); ) {
            Class iface = (Class) i.next();
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(iface);
                PropertyDescriptor[] pdscr = beanInfo.getPropertyDescriptors();
                for (int j = 0; j < pdscr.length; j++) {
                    if (pdscr[j] instanceof IndexedPropertyDescriptor) {
                        if (subject == null) {
                            throw new ProxyInstantiationException(ProxyUtils.INDEXED_PROPERTIES_NOT_SUPPORTED, "Property '" + pdscr[j].getName() + "' is indexed.");
                        } else {
                            checkMethodExistence(pdscr[j].getReadMethod());
                            checkMethodExistence(pdscr[j].getWriteMethod());
                            checkMethodExistence(((IndexedPropertyDescriptor) pdscr[j]).getIndexedReadMethod());
                            checkMethodExistence(((IndexedPropertyDescriptor) pdscr[j]).getIndexedWriteMethod());
                        }
                    }
                    String propertyName = pdscr[j].getName();
                    Class propertyType = pdscr[j].getPropertyType();
                    Method readMethod = pdscr[j].getReadMethod();
                    Method writeMethod = pdscr[j].getWriteMethod();
                    Property p = (Property) m_properties.get(propertyName);
                    if (p != null && !p.type.equals(propertyType)) {
                        if (subject == null) {
                            debug();
                            throw new ProxyInstantiationException(ProxyUtils.GETSET_TYPE_MISMATCH, "Type for property '" + propertyName + "' " + "ambiguous: " + p.type + " <-> " + propertyType + ".");
                        } else {
                            Method rm = (readMethod != null) ? readMethod : p.readMethod;
                            if (rm == null) {
                                if (m_ifaceMethodToSubjectMap.get(p.writeMethod) != null) {
                                    p.type = propertyType;
                                    methodsToBeSupported.remove(p.writeMethod);
                                    m_subjectImplementedMethods.add(p.writeMethod);
                                    p.writeMethod = writeMethod;
                                } else if (m_ifaceMethodToSubjectMap.get(writeMethod) != null) {
                                    methodsToBeSupported.remove(writeMethod);
                                    m_subjectImplementedMethods.add(writeMethod);
                                } else {
                                    throw new ProxyInstantiationException(ProxyUtils.METHOD_NOT_SUPPORTED, "To set methods for the same property " + "exist but none is implemented by the " + "subject. First method: '" + writeMethod + "', second method: '" + p.writeMethod + "'");
                                }
                            } else {
                                if (readMethod != null && p.readMethod != null) {
                                    throw new ProxyInstantiationException(ProxyUtils.CONFLICTING_INTERFACES, "Two methods with the same name and " + "different return types exist: " + "First method: '" + p.readMethod + "' " + "second method: '" + readMethod + "'.");
                                }
                                if (rm == readMethod) {
                                    if (m_ifaceMethodToSubjectMap.get(p.writeMethod) == null) {
                                        throw new ProxyInstantiationException(ProxyUtils.METHOD_NOT_SUPPORTED, "Expected subject to implement " + "method '" + p.writeMethod + "' as " + "it cannot be implemented by the " + "handler because a get method '" + readMethod + "' with a different type " + "exists.");
                                    } else {
                                        methodsToBeSupported.remove(p.writeMethod);
                                        m_subjectImplementedMethods.add(p.writeMethod);
                                        p.writeMethod = null;
                                        p.readMethod = readMethod;
                                        p.type = propertyType;
                                    }
                                } else {
                                    if (m_ifaceMethodToSubjectMap.get(writeMethod) == null) {
                                        throw new ProxyInstantiationException(ProxyUtils.METHOD_NOT_SUPPORTED, "Expected subject to implement " + "method '" + writeMethod + "' as it " + "cannot be implemented by the handler" + " because a get method '" + p.readMethod + "' with a different type exists.");
                                    } else {
                                        methodsToBeSupported.remove(writeMethod);
                                        m_subjectImplementedMethods.add(writeMethod);
                                    }
                                }
                            }
                        }
                    } else {
                        if (p == null) {
                            p = new Property();
                            p.name = propertyName;
                            p.type = propertyType;
                            m_properties.put(p.name, p);
                        }
                        if (readMethod != null) p.readMethod = readMethod;
                        if (writeMethod != null) p.writeMethod = writeMethod;
                    }
                }
            } catch (IntrospectionException e) {
                debug(e);
                throw new ProxyInstantiationException("Introspection of " + iface + " failed: " + e.getMessage());
            }
        }
        for (Iterator i = m_properties.values().iterator(); i.hasNext(); ) {
            Property p = (Property) i.next();
            Method subjReadMethod = (Method) m_ifaceMethodToSubjectMap.get(p.readMethod);
            Method subjWriteMethod = (Method) m_ifaceMethodToSubjectMap.get(p.writeMethod);
            if (p.readMethod != null && p.writeMethod != null) {
                if (!m_fallback || m_subject == null || subjReadMethod == null || subjWriteMethod == null) {
                    methodsToBeSupported.remove(p.readMethod);
                    methodsToBeSupported.remove(p.writeMethod);
                    m_readMethodToPropertyMap.put(p.readMethod, p);
                    m_writeMethodToPropertyMap.put(p.writeMethod, p);
                } else {
                    i.remove();
                    methodsToBeSupported.remove(p.readMethod);
                    m_subjectImplementedMethods.add(p.readMethod);
                    methodsToBeSupported.remove(p.writeMethod);
                    m_subjectImplementedMethods.add(p.writeMethod);
                }
            } else {
                i.remove();
                if (p.readMethod != null) {
                    if (subjReadMethod == null) {
                        throw new ProxyInstantiationException(ProxyUtils.METHOD_NOT_SUPPORTED, "Read method '" + p.readMethod + "' required but " + "cannot be implemented by proxy as it never " + "could be set as no corresponding set method " + "exists in any interface and subject (if " + "existent) doesn't implement method either.");
                    } else {
                        methodsToBeSupported.remove(p.readMethod);
                        m_subjectImplementedMethods.add(p.readMethod);
                    }
                }
                if (p.writeMethod != null) {
                    if (subjWriteMethod == null) {
                        throw new ProxyInstantiationException(ProxyUtils.METHOD_NOT_SUPPORTED, "Write method '" + p.writeMethod + "' required but " + "cannot be implemented by proxy as it never " + "could be get as no corresponding get method " + "exists in any interface and subject (if " + "existent) doesn't implement method either.");
                    } else {
                        methodsToBeSupported.remove(p.writeMethod);
                        m_subjectImplementedMethods.add(p.writeMethod);
                    }
                }
            }
        }
        for (Iterator i = methodsToBeSupported.iterator(); i.hasNext(); ) {
            Method m = (Method) i.next();
            if (m_ifaceMethodToSubjectMap.get(m) == null) {
                throw new ProxyInstantiationException(ProxyUtils.METHOD_NOT_SUPPORTED, "Non bean accessor/mutator method '" + m + "' required " + "but not implemented by subject (if existent).");
            } else {
                m_subjectImplementedMethods.add(m);
            }
        }
        for (Iterator i = m_subjectImplementedMethods.iterator(); i.hasNext(); ) {
            Method m = (Method) i.next();
            if (!m.isAccessible()) {
                try {
                    m.setAccessible(true);
                } catch (SecurityException e) {
                    throw new ProxyInstantiationException(ProxyUtils.FALLBACK_ACCESS_IMPOSSIBLE, "Cannot call method '" + m + "' as it is not " + "accessible: " + e.getMessage());
                }
            }
        }
        debug();
    }
