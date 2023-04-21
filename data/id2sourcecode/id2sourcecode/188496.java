    public void Update(String primaryKey, String JSONitem) {
        Method[] methods = clazz.getDeclaredMethods();
        Poll poll = new Poll();
        try {
            Map obj = (Map) new JSONParser().parse(JSONitem);
            for (Field classFeild : clazz.getDeclaredFields()) {
                Annotation ann = classFeild.getAnnotation(Column.class);
                if (ann == null) {
                    continue;
                }
                if (null == obj.get(classFeild.getName())) {
                    continue;
                }
                ann = classFeild.getAnnotation(Id.class);
                boolean isPrimaryKey = false;
                if (ann != null) {
                    isPrimaryKey = true;
                }
                String methodName = "set" + classFeild.getName().substring(0, 1).toUpperCase() + classFeild.getName().substring(1);
                Method m = null;
                try {
                    for (Method method : methods) {
                        if (method.getName().equals(methodName)) {
                            Logger.getLogger(Polls.class.getName()).info(methodName);
                            m = method;
                            break;
                        }
                    }
                    String s = obj.get(classFeild.getName()).toString();
                    Object parameterObject;
                    if (isPrimaryKey) {
                        parameterObject = Conversion.Cast(primaryKey, m.getParameterTypes()[0]);
                    } else {
                        parameterObject = Conversion.Cast(s, m.getParameterTypes()[0]);
                    }
                    m.invoke(poll, new Object[] { parameterObject });
                } catch (Exception ex) {
                    Logger.getLogger(Polls.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            poll.Update();
        } catch (Exception ex) {
            Logger.getLogger(Polls.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
