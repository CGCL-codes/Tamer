    private List<Property> getProperties(Object target) throws IntrospectionException {
        Method[] methods = ClassUtilities.findMethods(target.getClass(), Parameter.class);
        BeanInfo info = Introspector.getBeanInfo(target.getClass(), Object.class);
        PropertyDescriptor[] pds = info.getPropertyDescriptors();
        List<Property> props = new ArrayList<Property>();
        for (PropertyDescriptor pd : pds) {
            if (methods.length > 0) {
                Method readMethod = pd.getReadMethod();
                Method writeMethod = pd.getWriteMethod();
                Parameter param = readMethod != null ? readMethod.getAnnotation(Parameter.class) : null;
                if (param == null) param = writeMethod != null ? writeMethod.getAnnotation(Parameter.class) : null;
                if (param != null) {
                    String conv = param.converter();
                    StringConverter converter = null;
                    if (conv.trim().length() != 0) {
                        try {
                            Class clazz = Class.forName(conv);
                            converter = (StringConverter) clazz.newInstance();
                        } catch (IllegalAccessException e) {
                            msg.warn("Error while creating converter for agent parameter '" + param.usageName() + "'", e);
                        } catch (InstantiationException e) {
                            msg.warn("Error while creating converter for agent parameter '" + param.usageName() + "'", e);
                        } catch (ClassNotFoundException e) {
                            msg.warn("Error while creating converter for agent parameter '" + param.usageName() + "'", e);
                        }
                    } else {
                        converter = converterMap.get(readMethod.getReturnType());
                    }
                    Property prop = new Property(pd.getName(), readMethod, writeMethod);
                    prop.displayName = param.displayName();
                    prop.converter = converter;
                    props.add(prop);
                }
            } else {
                Property prop = new Property(pd.getName(), pd.getReadMethod(), pd.getWriteMethod());
                if (pd.getReadMethod() != null) {
                    prop.converter = converterMap.get(pd.getReadMethod().getReturnType());
                    props.add(prop);
                }
            }
        }
        return props;
    }
