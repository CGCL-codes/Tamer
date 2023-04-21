    public static final void addSystemProperties(Properties newProperties) {
        try {
            Enumeration propertyNames = newProperties.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String name = (String) propertyNames.nextElement();
                String value = newProperties.getProperty(name);
                System.setProperty(name, value);
            }
        } catch (AccessControlException e) {
            final Class thisClass = Util.class;
            System.err.println("AccessControlException attempting to set System Properties.  Ensure access for this class ('" + thisClass.getName() + "') allows System Property writes");
            System.err.println("Need to grant permission to this class: \n\tpermission java.util.PropertyPermission \"*\", \"read,write\";");
            throw e;
        }
    }
