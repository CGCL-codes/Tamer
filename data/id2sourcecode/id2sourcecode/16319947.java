    public static ITree getTreeData(String type) {
        ITree ret = null;
        type = type.trim();
        try {
            ret = (ITree) Class.forName(type).newInstance();
            return ret;
        } catch (Exception e) {
            log.info("class " + type + " not found in class path,tree.properties will been checked!!!");
        }
        if (map == null) {
            return null;
        } else {
            Object obj = map.get(type);
            if (obj == null) {
                try {
                    ClassLoader classLoader = TreeFactory.class.getClassLoader();
                    URL url = classLoader.getResource("treedata.properties");
                    Properties treeProps = new Properties();
                    if (url != null) {
                        InputStream is = url.openStream();
                        treeProps.load(is);
                        is.close();
                        obj = treeProps.getProperty(type);
                        if (obj != null) {
                            map.put(type + "", obj);
                        }
                    } else {
                        log.info("not found resources file treedata.properties");
                        return null;
                    }
                } catch (Exception e) {
                    log.info("not found resources file treedata.properties");
                    return null;
                }
            }
            if (obj != null) {
                String className = ((String) obj).trim();
                try {
                    ret = (ITree) Class.forName(className).newInstance();
                } catch (Exception e) {
                    log.info("class " + className + " not found in class path!!!");
                    return null;
                }
            }
            if (ret == null) log.info("type " + type + " not exist please check treedata.properties");
            return ret;
        }
    }
