    protected Expression getExpressionForClass(final Class clazz, final String sId) throws IllegalArgumentException {
        Constructor constr = EXPR_CONSTRUCTORCACHE.get(clazz);
        try {
            if (constr != null) {
                Object[] obj = { new IRI(sId) };
                return (Expression) constr.newInstance(obj);
            } else {
                Class[] params = { Id.class };
                constr = clazz.getConstructor(params);
                if (constr != null) {
                    EXPR_CONSTRUCTORCACHE.put(clazz, constr);
                    return getExpressionForClass(clazz, sId);
                } else {
                    throw new IllegalArgumentException("Can't find a Constructor for the class: " + clazz.getName());
                }
            }
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        } catch (SecurityException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
