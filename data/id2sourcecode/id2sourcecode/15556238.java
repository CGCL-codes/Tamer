    public static Object copy(final Object target, final Object source, Set forbidden, final String[] props, final boolean checkInheritance, final boolean copyIfNull) throws IntrospectionException {
        if (forbidden == null) forbidden = new TreeSet();
        if (!forbidden.contains("class")) forbidden.add("class");
        if (!checkInheritance || Beans.isInstanceOf(target, source.getClass())) {
            BeanInfo childInfo = Introspector.getBeanInfo(target.getClass());
            PropertyDescriptor[] childProps = childInfo.getPropertyDescriptors();
            String from = source.getClass().getName();
            from = from.substring(from.lastIndexOf(".") + 1);
            String to = target.getClass().getName();
            to = to.substring(to.lastIndexOf(".") + 1);
            Set set = getPropertiesSet(childProps, props);
            Iterator it = set.iterator();
            while (it.hasNext()) {
                String property = (String) it.next();
                if (!forbidden.contains(property)) {
                    Method read = getReadMethod(source.getClass(), property);
                    if (read != null) {
                        Method write = getWriteMethod(target.getClass(), property, read.getParameterTypes());
                        if (write != null) {
                            Object value = null;
                            try {
                                value = read.invoke(source, null);
                                if (!copyIfNull && value == null) continue;
                                Object[] args = new Object[1];
                                args[0] = value;
                                write.invoke(target, args);
                            } catch (IllegalAccessException ex) {
                            } catch (IllegalArgumentException ex) {
                            } catch (InvocationTargetException ex) {
                            }
                        }
                    }
                }
            }
        }
        return target;
    }
