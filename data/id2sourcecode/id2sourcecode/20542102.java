    public static DataInfo getDataInfo(String type) {
        DataInfo data = null;
        type = type.trim();
        try {
            data = (DataInfo) Class.forName(type).newInstance();
            return data;
        } catch (Exception e) {
            log.info("class " + type + " not found in class path!!!");
        }
        if (map == null) {
            return null;
        } else {
            Object obj = map.get(type);
            if (obj == null) {
                try {
                    ClassLoader classLoader = ListDataInfoFactory.class.getClassLoader();
                    URL url = classLoader.getResource("listdata.properties");
                    Properties listProps = new Properties();
                    if (url != null) {
                        InputStream is = url.openStream();
                        listProps.load(is);
                        is.close();
                        obj = listProps.getProperty(type);
                        if (obj != null) {
                            map.put(type, obj);
                        }
                    } else {
                        log.info("not found resources file listdata.properties");
                        return null;
                    }
                } catch (Exception e) {
                    log.info("not found resources file listdata.properties");
                    return null;
                }
            }
            if (obj != null) {
                String className = ((String) obj).trim();
                try {
                    data = (DataInfo) Class.forName(className).newInstance();
                } catch (Exception e) {
                    log.info("class " + className + " not found in class path!!!");
                    return null;
                }
            }
            if (data == null) log.info("type " + type + " not exist please check listdata.properties");
            return data;
        }
    }
