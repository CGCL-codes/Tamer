    public static Object getPrimaryKey(PrimaryKeyDefinition def, ManagedTransientObject fromObject, Map attributeNameMap) throws PrimaryKeyFactoryException {
        String className = null;
        if (def == null) {
            throw new PrimaryKeyFactoryException("getPrimaryKey(PrimaryKeyDefinition[" + def + "], ManagedTransientObject[" + fromObject + "], Map[" + attributeNameMap + "]): Definition [null] is invalid.");
        }
        try {
            Object toObject = def.getPrimaryKeyClass().newInstance();
            className = toObject.getClass().getName();
            String name = null;
            Class type = null;
            Field[] fields = toObject.getClass().getFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                name = f.getName();
                type = f.getType();
                if (attributeNameMap != null && attributeNameMap.size() > 0) {
                    String mappedName = (String) attributeNameMap.get(name);
                    if (mappedName != null) {
                        name = mappedName;
                    }
                }
                try {
                    String readMethod = UtilityBean.getReadMethod(name);
                    Expression expression = new Expression(fromObject, readMethod, null);
                    Object rObj = expression.getValue();
                    Object valueObject = WrapperFactory.buildObject(String.valueOf(rObj), type);
                    f.set(toObject, valueObject);
                } catch (IllegalArgumentException e) {
                    throw new PrimaryKeyFactoryException("IllegalArgumentException occurred while constructing a PrimaryKey [" + className + "] attribute [" + name + "]:" + "Possibly the number of actual parameters supplied (by readMethod of fromObject) via args[" + name + "] is different from the number of formal parameters required by the underlying method (of toObject's writeMethod).[" + name + "]" + e.toString());
                } catch (java.lang.NoSuchMethodException e) {
                    log.log("getPrimaryKey(PrimaryKeyDefinition[" + def + "], ManagedTransientObject[" + fromObject + "], Map[" + attributeNameMap + "]): " + "NoSuchMethodException occurred while a PrimaryKey [" + className + "] attribute [" + name + "]:", e, log.ERROR);
                    throw new PrimaryKeyFactoryException("getPrimaryKey(PrimaryKeyDefinition[" + def + "], ManagedTransientObject[" + fromObject + "], Map[" + attributeNameMap + "]): " + "NoSuchMethodException occurred while a PrimaryKey [" + className + "] attribute [" + name + "]:" + e.toString());
                } catch (Exception e) {
                    throw new PrimaryKeyFactoryException("getPrimaryKey(PrimaryKeyDefinition[" + def + "], ManagedTransientObject[" + fromObject + "], Map[" + attributeNameMap + "]): " + "Error occurred while constructing a PrimaryKey [" + className + "] attribute [" + name + "]:" + e.toString());
                }
            }
            return toObject;
        } catch (Exception e) {
            throw new PrimaryKeyFactoryException("getPrimaryKey(PrimaryKeyDefinition[" + def + "], ManagedTransientObject[" + fromObject + "], Map[" + attributeNameMap + "]): " + e.toString());
        }
    }
