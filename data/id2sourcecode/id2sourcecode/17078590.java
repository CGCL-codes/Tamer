    public void generateHibernateMapping(Class<?> clazz) {
        if (!isClassMapped(clazz)) {
            Logger.log("Adding mapping for " + clazz.getSimpleName().toLowerCase());
            Element mappingRootElement = getMappingElement();
            Element mappingElement = null;
            if (!clazz.getSuperclass().equals(Object.class)) {
                generateHibernateMapping(clazz.getSuperclass());
                Element parentMappingElement = classElementMap.get(clazz.getSuperclass());
                mappingElement = parentMappingElement.addElement("joined-subclass");
                mappingElement.addAttribute("name", clazz.getName());
                mappingElement.addAttribute("table", getTablePrefix() + clazz.getSimpleName().toLowerCase());
                Element keyElement = mappingElement.addElement("key");
                keyElement.addAttribute("column", "id");
                idElementMap.put(clazz, keyElement);
            } else {
                mappingElement = mappingRootElement.addElement("class");
                mappingElement.addAttribute("name", clazz.getName());
                mappingElement.addAttribute("table", getTablePrefix() + clazz.getSimpleName().toLowerCase());
                Element keyElement = mappingElement.addElement("id");
                keyElement.addAttribute("name", "id");
                keyElement.addAttribute("type", "long");
                keyElement.addAttribute("column", "id");
                Element generatorElement = keyElement.addElement("generator");
                generatorElement.addAttribute("class", "native");
                idElementMap.put(clazz, keyElement);
            }
            classElementMap.put(clazz, mappingElement);
            String sqlUpdate = null;
            String cachePolicy = "none";
            boolean lazy = true;
            if (IHibernateFriendly.class.isAssignableFrom(clazz)) {
                try {
                    Method getSqlUpdate = clazz.getMethod("getSqlUpdate");
                    sqlUpdate = (String) getSqlUpdate.invoke(clazz.newInstance());
                    Method getCachePolicy = clazz.getMethod("getCachePolicy");
                    Method isLazy = clazz.getMethod("isLazy");
                    cachePolicy = (String) getCachePolicy.invoke(clazz.newInstance());
                    lazy = (Boolean) isLazy.invoke(clazz.newInstance());
                } catch (Exception e) {
                }
            }
            if (sqlUpdate != null && !"".equals(sqlUpdate)) {
                mappingElement.addElement("sql-update").setText(sqlUpdate);
            }
            if (cachePolicy != null && !"".equals(cachePolicy) && !"none".equals(cachePolicy)) {
                Element cacheElement = mappingElement.addElement("cache");
                cacheElement.addAttribute("usage", cachePolicy);
            }
            mappingElement.addAttribute("lazy", "" + lazy);
            List<Field> fields = ReflectionCache.getFields(clazz);
            for (Field field : fields) {
                String name = field.getName();
                try {
                    Method isFieldMappedMethod = clazz.getMethod("isFieldMapped", String.class);
                    boolean isFieldMapped = (Boolean) isFieldMappedMethod.invoke(clazz.newInstance(), name);
                    if (!isFieldMapped) {
                        Logger.log("  -" + name + ":" + field.getType().getName());
                        continue;
                    }
                } catch (Throwable t) {
                    Logger.log("Cannot determine if field is hibernated managed:" + field.getName() + " (" + field.getType().getName() + ")");
                }
                boolean skip = false;
                List<Field> parentFields = ReflectionCache.getFields(clazz.getSuperclass());
                for (Field parentField : parentFields) {
                    if (field.equals(parentField)) {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue;
                }
                Logger.log("  +" + name + ":" + field.getType().getName());
                Boolean isKey = Boolean.FALSE;
                Boolean isUnique = Boolean.FALSE;
                String typeOverride = null;
                int fieldLength = -1;
                if (IHibernateFriendly.class.isAssignableFrom(clazz)) {
                    try {
                        Method isKeyMethod = clazz.getMethod("isFieldKey", String.class);
                        Method isUniqueMethod = clazz.getMethod("isFieldUnique", String.class);
                        Method getFieldTypeMethod = clazz.getMethod("getFieldType", String.class);
                        Method getFieldLengthMethod = clazz.getMethod("getFieldLength", String.class);
                        isKey = (Boolean) isKeyMethod.invoke(clazz.newInstance(), name);
                        isUnique = (Boolean) isUniqueMethod.invoke(clazz.newInstance(), name);
                        typeOverride = (String) getFieldTypeMethod.invoke(clazz.newInstance(), name);
                        fieldLength = (Integer) getFieldLengthMethod.invoke(clazz.newInstance(), name);
                    } catch (Exception e) {
                    }
                }
                if (isKey) {
                    Element keyElement = idElementMap.get(clazz);
                    if (idElementClearedMap.get(clazz) == null || !idElementClearedMap.get(clazz)) {
                        keyElement.detach();
                        keyElement = mappingElement.addElement("composite-id");
                        idElementMap.put(clazz, keyElement);
                        idElementClearedMap.put(clazz, Boolean.TRUE);
                    }
                    if (!isJavaType(field.getType())) {
                        Element keyEntry = keyElement.addElement("key-many-to-one");
                        keyEntry.addAttribute("name", field.getName());
                        keyEntry.addAttribute("class", field.getType().getName());
                        keyEntry.addAttribute("column", field.getName());
                    } else {
                        Element keyEntry = keyElement.addElement("key-property");
                        keyEntry.addAttribute("name", field.getName());
                        keyEntry.addAttribute("column", field.getName());
                    }
                    continue;
                }
                if (!name.equals("id")) {
                    String type = field.getType().getSimpleName().toLowerCase();
                    if (isJavaType(field.getType())) {
                        Element propertyElement = mappingElement.addElement("property");
                        propertyElement.addAttribute("name", name);
                        if (typeOverride != null) {
                            propertyElement.addAttribute("type", typeOverride);
                        } else {
                            propertyElement.addAttribute("type", type);
                        }
                        if (fieldLength > 0) {
                            propertyElement.addAttribute("length", "" + fieldLength);
                        }
                        propertyElement.addAttribute("column", name);
                        if (isUnique) {
                            propertyElement.addAttribute("unique", "true");
                        }
                    } else if (field.getType().isAssignableFrom(Set.class)) {
                        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                        Element setElement = mappingElement.addElement("set");
                        setElement.addAttribute("name", name);
                        setElement.addElement("cache").addAttribute("usage", "nonstrict-read-write");
                        setElement.addAttribute("inverse", "true");
                        setElement.addAttribute("lazy", "false");
                        setElement.addElement("key").addAttribute("column", "id");
                        setElement.addElement("one-to-many").addAttribute("class", ((Class<?>) genericType.getActualTypeArguments()[0]).getName());
                    } else if (byte[].class.equals(field.getType())) {
                        Element propertyElement = mappingElement.addElement("property");
                        propertyElement.addAttribute("name", name);
                        propertyElement.addAttribute("type", "binary");
                        propertyElement.addElement("column").addAttribute("name", name).addAttribute("sql-type", "LONGBLOB");
                        if (isUnique) {
                            propertyElement.addAttribute("unique", "true");
                        }
                    } else {
                        Element manyToOneElement = mappingElement.addElement("many-to-one");
                        manyToOneElement.addAttribute("name", name);
                        manyToOneElement.addAttribute("class", field.getType().getName());
                        manyToOneElement.addAttribute("column", name);
                        manyToOneElement.addAttribute("lazy", "false");
                        if (isUnique) {
                            manyToOneElement.addAttribute("unique", "true");
                        }
                    }
                }
            }
            addMappedClass(clazz);
            Logger.log("Finished mapping for " + clazz.getSimpleName().toLowerCase());
        }
    }
