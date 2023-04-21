    public static String adjustQuery(String query) {
        String result = query;
        try {
            String[] parsedAliases = getSelectQueryAliases(query);
            if (parsedAliases != null && parsedAliases.length == 1) {
                QueryTranslatorImpl trans = (QueryTranslatorImpl) HQLHelper.compileQuery(query);
                org.hibernate.type.Type[] types = trans.getReturnTypes();
                String[] aliases = trans.getReturnAliases();
                if (types != null && types.length == 1) {
                    org.hibernate.type.Type type = types[0];
                    String alias = aliases[0];
                    try {
                        Integer.valueOf(alias);
                        alias = "property" + alias;
                        if (Pattern.matches("[a-zA-Z$_][a-zA-Z$_0-9]*", parsedAliases[0])) {
                            alias = parsedAliases[0];
                        } else {
                            return result;
                        }
                    } catch (Throwable e) {
                        return result;
                    }
                    Class<?> clazz = type.getReturnedClass();
                    if (clazz.isAnnotationPresent(Entity.class)) {
                        NodeList beanProperties = getBeanProperties(clazz.getCanonicalName()).getChildNodes();
                        String s = "SELECT ";
                        for (int i = 0; i < beanProperties.getLength(); i++) {
                            Node item = beanProperties.item(i);
                            if ("property".equals(item.getNodeName())) {
                                String propertyName = item.getAttributes().getNamedItem("name").getNodeValue();
                                String typeName = item.getAttributes().getNamedItem("type").getNodeValue();
                                try {
                                    if (typeName.indexOf('<') != -1) {
                                        typeName = typeName.substring(0, typeName.indexOf('<'));
                                    }
                                    Class<?> clazz1 = AnnotationsHelper.class.getClassLoader().loadClass(typeName);
                                    if (Collection.class.isAssignableFrom(clazz1)) {
                                        continue;
                                    }
                                    Field propField = clazz.getDeclaredField(propertyName);
                                    Transient transientAnnotation = propField.getAnnotation(Transient.class);
                                    if (transientAnnotation == null) {
                                        String readMethodName = item.getAttributes().getNamedItem("readMethod").getNodeValue();
                                        String writeMethodName = item.getAttributes().getNamedItem("writeMethod").getNodeValue();
                                        Method[] methods = clazz.getDeclaredMethods();
                                        for (Method m : methods) {
                                            if (m.getName().equals(readMethodName) || m.getName().equals(writeMethodName)) {
                                                transientAnnotation = methods[i].getAnnotation(Transient.class);
                                            }
                                        }
                                    }
                                    if (transientAnnotation != null) {
                                        continue;
                                    }
                                } catch (Throwable e) {
                                }
                                s += alias + "." + propertyName + " as " + propertyName + " , ";
                            }
                        }
                        s = s.substring(0, s.length() - 2) + " ";
                        result = s + result.substring(result.toLowerCase().indexOf("from "));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
