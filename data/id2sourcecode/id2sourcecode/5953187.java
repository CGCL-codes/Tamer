    public Object[] invoke(DPWSContextImpl context, OperationInfo context_aoi, Object body) throws DPWSException {
        String actionURI = (context_aoi.isEvent()) ? context_aoi.getOutAction() : context_aoi.getInAction();
        OperationInfo aoi = invokationMap.get(actionURI);
        if (aoi == null) throw new DPWSException("Could not invoke action : " + actionURI + " .");
        Method method = aoi.getMethod();
        if (method == null) throw new DPWSException("No method define for action : " + actionURI + " .");
        Class<?>[] classes = method.getParameterTypes();
        if (classes == null) throw new DPWSException("Method with incorrect parameters for action : " + actionURI + " .");
        int len = classes.length;
        Object[] params = new Object[classes.length];
        params[0] = context;
        Object localImplementor = actionToImplementor.get(actionURI);
        if (body == null) {
        } else if (body instanceof List) {
            List<?> bodyList = (List<?>) body;
            if (!(localImplementor instanceof InvocationHandler) && len != 1 + bodyList.size()) throw new DPWSException("Incorrect parameter number for action : " + actionURI + " .");
            for (int i = 0; i < bodyList.size(); i++) params[i + 1] = bodyList.get(i);
        } else {
            params[1] = new Object[] { body };
        }
        try {
            Class<?> retClass = method.getReturnType();
            if (localImplementor instanceof InvocationHandler) {
                InvocationHandler handler = (InvocationHandler) localImplementor;
                Object obj = handler.invoke(null, method, params);
                return new Object[] { obj };
            }
            if (retClass.isArray()) {
                return (Object[]) method.invoke(localImplementor, params);
            }
            Object obj = null;
            if ((getIfmappingpe() != null) && (getIfmappingpe().serviceMap.containsKey(actionURI))) {
                Mappedservice ms = getIfmappingpe().serviceMap.get(actionURI);
                if (ms.invparams.length == 0) obj = ms.invmethode.invoke(getIfmappingpe().invokerinstanz); else {
                    Object[] invokingparameters = new Object[params.length - 1];
                    for (int i = 0; i < params.length - 1; i++) invokingparameters[0] = params[i + 1];
                    obj = ms.invmethode.invoke(getIfmappingpe().invokerinstanz, invokingparameters);
                }
            } else {
                obj = method.invoke(localImplementor, params);
            }
            return new Object[] { obj };
        } catch (IllegalArgumentException e) {
            throw new DPWSException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new DPWSException(e.getMessage());
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof DPWSException) throw (DPWSException) e.getTargetException();
            throw new DPWSException(e.getTargetException());
        } catch (Throwable e) {
            log.error("While invoking InvocationHandler", e);
            throw new DPWSException(e.getMessage());
        }
    }
