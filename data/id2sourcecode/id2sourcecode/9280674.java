    public void setComponent(Object comp) {
        try {
            className = comp.getClass().getName();
            cleanupClassName();
            if (className.indexOf(".swing.") > 0) isSwing = true;
            Class clazz = (comp instanceof ComponentProxy ? ((ComponentProxy) comp).getProxiedComponent().getClass() : comp.getClass());
            BeanInfo bi = Introspector.getBeanInfo(clazz);
            PropertyDescriptor props[] = bi.getPropertyDescriptors();
            for (int i = 0; i < props.length; i++) {
                Method readMethod = props[i].getReadMethod();
                Method writeMethod = props[i].getWriteMethod();
                if ((readMethod != null) && (writeMethod != null)) {
                    String propertyName = props[i].getName();
                    String qualifiedName = className + "." + propertyName;
                    if (!properties.contains(qualifiedName)) {
                        addPropertyType(qualifiedName, new PlainProperty(propertyName, readMethod.getName(), writeMethod.getName()));
                        properties.add(qualifiedName);
                    }
                }
            }
            EventSetDescriptor[] reflectedEvents = bi.getEventSetDescriptors();
            for (int i = 0; i < reflectedEvents.length; i++) {
                String eventName = "_" + reflectedEvents[i].getName();
                String qualifiedName = className + "." + eventName;
                Method adder = reflectedEvents[i].getAddListenerMethod();
                if (!properties.contains(qualifiedName)) {
                    addPropertyType(qualifiedName, new EventProperty(eventName, adder));
                    properties.add(qualifiedName);
                }
            }
        } catch (IntrospectionException ex) {
            DebugLogger.logError("Couldn't introspect property: " + ex.getMessage());
        }
    }
