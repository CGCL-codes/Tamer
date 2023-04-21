    public Object getChannelObject() throws XAwareException {
        try {
            final Class homeClass = Class.forName(this.getFactoryClassName(), true, BaseBizComponentInst.getClassLoader());
            Object obj = homeClass.newInstance();
            Method[] setterMethods = getSetterMethods(obj.getClass().getMethods());
            Properties connProps = (Properties) this.m_props.get(PN_CONNECTION_PROPERTIES);
            Set<Entry<Object, Object>> properties = connProps.entrySet();
            for (Entry<Object, Object> entry : properties) {
                String property = (String) entry.getKey();
                String value = (String) entry.getValue();
                Method method = getSetMethod(setterMethods, property);
                if (method != null) {
                    Class[] parameterClasses = method.getParameterTypes();
                    Object[] args = new Object[1];
                    if (parameterClasses[0].equals(Boolean.class) || parameterClasses[0].equals(boolean.class)) {
                        args[0] = new Boolean(value);
                    } else if (parameterClasses[0].equals(Byte.class) || parameterClasses[0].equals(byte.class)) {
                        args[0] = new Byte(value);
                    } else if (parameterClasses[0].equals(Character.class) || parameterClasses[0].equals(char.class)) {
                        args[0] = new Character(value.charAt(0));
                    } else if (parameterClasses[0].equals(Double.class) || parameterClasses[0].equals(double.class)) {
                        args[0] = new Double(value);
                    } else if (parameterClasses[0].equals(Float.class) || parameterClasses[0].equals(float.class)) {
                        args[0] = new Float(value);
                    } else if (parameterClasses[0].equals(Integer.class) || parameterClasses[0].equals(int.class)) {
                        args[0] = new Integer(value);
                    } else if (parameterClasses[0].equals(Long.class) || parameterClasses[0].equals(long.class)) {
                        args[0] = new Long(value);
                    } else if (parameterClasses[0].equals(Short.class) || parameterClasses[0].equals(short.class)) {
                        args[0] = new Short(value);
                    } else if (parameterClasses[0].equals(String.class)) {
                        args[0] = value;
                    } else {
                        throw new XAwareConfigurationException("Unsupported method parameter type for the jms factory properties");
                    }
                    method.invoke(obj, args);
                } else {
                    throw new XAwareConfigurationException("Unable to locate method to set " + property);
                }
            }
            return new JmsConnectionFactoryHolder((ConnectionFactory) obj, isJms102(), null, this.getUsername(), this.getPassword());
        } catch (Exception e) {
            lf.printStackTrace(e);
            throw new XAwareConfigurationException(e);
        }
    }
