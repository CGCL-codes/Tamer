    public PropertyInfo createSimplePropertyInfo(final PropertyDescriptor pd) {
        final boolean readMethod = isValidMethod(pd.getReadMethod());
        final boolean writeMethod = isValidMethod(pd.getWriteMethod());
        if (!writeMethod || !readMethod) {
            return null;
        }
        final PropertyInfo pi = new PropertyInfo(pd.getName(), pd.getPropertyType());
        pi.setConstrained(pd.isConstrained());
        pi.setDescription(pd.getShortDescription());
        pi.setNullable(true);
        pi.setPreserve(false);
        pi.setReadMethodAvailable(readMethod);
        pi.setWriteMethodAvailable(writeMethod);
        pi.setXmlName(pd.getName());
        if (isAttributeProperty(pd.getPropertyType())) {
            pi.setPropertyType(PropertyType.ATTRIBUTE);
            pi.setXmlHandler(getHandlerClass(pd.getPropertyType()));
        } else {
            pi.setPropertyType(PropertyType.ELEMENT);
        }
        return pi;
    }
