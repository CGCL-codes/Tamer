    protected Object newBean(String localName, Object parent, Object[] args, Class[] argTypes) {
        String propertyName = LavaBeanUtils.toLCC(localName, lowerCaseAcronyms);
        Constructor ctor = findConstructor(propertyName, argTypes);
        if (ctor == null) {
            Property[] pds = beanInfo.getProperties(parent.getClass(), propertyName);
            for (int i = 0; ctor == null && i < pds.length; i++) {
                Property prop = pds[i];
                ctor = getConstructor(prop.getType(), argTypes);
                if (ctor == null) ctor = findConstructor(LavaBeanUtils.getShortName(prop.getType()), argTypes);
            }
        }
        if (ctor != null) {
            return beanInfo.newInstance(ctor, args);
        } else {
            if (LOG.isDebugEnabled()) LOG.debug("Not able to construct a bean for " + localName);
            return null;
        }
    }
