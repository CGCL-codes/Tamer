    private OdmPojo(Class<T> pojoClass) throws OdmException {
        try {
            this.pojoClass = pojoClass;
            ldapClass = (ObjectClass) pojoClass.getAnnotation(ObjectClass.class);
            if (Modifier.isAbstract(pojoClass.getModifiers())) {
                abstractClass = true;
                Set<Class<?>> set = AnnotationUtils.listAnnotatedSubClasses(pojoClass);
                for (Class<?> annotatedClass : set) {
                    annotatedSubClasses.put(getInstance(annotatedClass).ldapClass.value(), annotatedClass);
                }
            } else {
                if (ldapClass != null) {
                    if (ldapClass.value().length() == 0) {
                        ldapClass = AnnotationUtils.copyObjectClass(ldapClass, pojoClass.getSimpleName());
                    }
                    PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(pojoClass, Object.class).getPropertyDescriptors();
                    HashMap<String, Field> map = new HashMap<String, Field>();
                    getAllFields(map, pojoClass);
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        String propName = propertyDescriptor.getName();
                        Method writeMethod = propertyDescriptor.getWriteMethod();
                        Method readMethod = propertyDescriptor.getReadMethod();
                        Field field = map.get(propName);
                        if (field == null) {
                            continue;
                        }
                        Attribute attribute = field.getAnnotation(Attribute.class);
                        Child child = field.getAnnotation(Child.class);
                        BaseDn baseDn = field.getAnnotation(BaseDn.class);
                        byte check = check(attribute, child, baseDn, readMethod, writeMethod);
                        switch(check) {
                            case BYPASS_CASE:
                                continue;
                            case ATTRIBUTE_CASE:
                                if (attribute.value().length() == 0) {
                                    attribute = AnnotationUtils.copyAttribute(attribute, propName);
                                }
                                AttributeMapper attributeMapper = new AttributeMapper(attribute, readMethod, writeMethod);
                                attributeMappers.put(attributeMapper.getAttribute().value(), attributeMapper);
                                if (attribute.isId() && idMapper == null) {
                                    idMapper = attributeMapper;
                                } else if (attribute.isId()) {
                                    throw new OdmParsingException(pojoClass, "Only one DN annotation is allowed");
                                }
                                break;
                            case CHILLD_CASE:
                                childMappers.add(new ChildMapper(child, readMethod, writeMethod));
                                break;
                            case BASEDN_CASE:
                                if (baseDnMapper == null) {
                                    baseDnMapper = new BaseDnMapper(baseDn, readMethod, writeMethod);
                                } else {
                                    throw new OdmParsingException(pojoClass, "Only one BaseDn annotation is allowed");
                                }
                                break;
                            default:
                                if (check < 0) {
                                    throw new OdmParsingException(pojoClass, "no getter or setter found for field " + field.getName());
                                } else {
                                    throw new OdmParsingException(pojoClass, "Attribute, Child, Id and BaseDn annotation are mutually exclusive");
                                }
                        }
                    }
                    if (idMapper == null) {
                        throw new OdmParsingException(pojoClass, "DN annotation is mandatory");
                    }
                } else {
                    throw new OdmParsingException(pojoClass, "ObjectClass annotation (value) is mandatory");
                }
            }
        } catch (Exception exception) {
            throw new OdmException(exception);
        }
    }
