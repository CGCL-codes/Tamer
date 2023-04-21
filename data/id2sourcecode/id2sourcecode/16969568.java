    public static EmmetProfileSchema buildEmmetProfileSchema(EmmetAuthoritiesRegistry authoritiesRegistry) {
        EmmetProfileSchema res = new EmmetProfileSchema();
        res.setAuthoritiesRegistry(authoritiesRegistry);
        Map<String, Properties> schema = new HashMap<String, Properties>();
        String[] rawProperties = new String[] { "propName=age\nname=Age\nThe user's age in years\n" + "read=ROLE_ADMIN,ROLE_USER,ROLE_SERVICE\n" + "write=ROLE_ADMIN,ROLE_USER+IS_TARGET_USER,ROLE_SERVICE\n", "propName=reputation\nname=Reputation\nThe user's reputation\n" + "read=ROLE_ADMIN,ROLE_USER,ROLE_SERVICE\n" + "write=ROLE_ADMIN,ROLE_SERVICE\n", "propName=naughtiness\nname=Naughtiness\nThe user's naughtiness\n" + "read=ROLE_ADMIN\n" + "write=ROLE_ADMIN\n", "propName=secret\nname=Secret\nA user secret\n" + "read=ROLE_USER+IS_TARGET_USER\n" + "write=ROLE_USER+IS_TARGET_USER\n" };
        for (String str : rawProperties) {
            Properties props = PropertiesHelper.initProperties(str);
            schema.put(props.getProperty("propName"), props);
        }
        res.setSchema(schema);
        res.afterPropertiesSet();
        return res;
    }
