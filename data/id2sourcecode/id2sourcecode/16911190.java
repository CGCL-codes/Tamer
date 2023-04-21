    protected Object createObject() throws SAXException {
        try {
            RoleInfo roleInfo = new RoleInfo(name, refMBeanClassName, readable, writeable, minDegree, maxDegree, description);
            return roleInfo;
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }
