    private static <T extends InvocableAdapter> List<T> guessInvocable(Class[] paramSignature, Object[] params, Iterable<T> candidates) {
        if (paramSignature == null) {
            paramSignature = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                paramSignature[i] = (params[i] != null) ? params[i].getClass() : null;
            }
        }
        List<T> exactMatches = new ArrayList<T>(), varargsMatches = new ArrayList<T>();
        candidateLoop: for (T adapter : candidates) {
            Class[] mSignature = adapter.getParameterTypes();
            if (!adapter.isVarArgs()) {
                if (paramSignature.length != mSignature.length) {
                    continue;
                }
            } else if (paramSignature.length - 1 < mSignature.length) {
                continue;
            } else if (paramSignature.length > mSignature.length) {
                Class[] newSignature = new Class[paramSignature.length];
                System.arraycopy(mSignature, 0, newSignature, 0, mSignature.length - 1);
                for (int i = mSignature.length - 1; i < newSignature.length; i++) {
                    newSignature[i] = mSignature[mSignature.length - 1].getComponentType();
                }
                mSignature = newSignature;
            } else if (paramSignature[paramSignature.length - 1] != null && !mSignature[mSignature.length - 1].isAssignableFrom(paramSignature[paramSignature.length - 1])) {
                mSignature[mSignature.length - 1] = mSignature[mSignature.length - 1].getComponentType();
            }
            for (int i = 0; i < paramSignature.length; i++) {
                Class paramClass = paramSignature[i];
                Class mClass = (i >= mSignature.length ? mSignature[mSignature.length - 1] : mSignature[i]);
                if (paramClass != null && !toNonPrimitive(mClass).isAssignableFrom(paramClass)) {
                    continue candidateLoop;
                }
            }
            (adapter.isVarArgs() ? varargsMatches : exactMatches).add(adapter);
        }
        return !exactMatches.isEmpty() ? exactMatches : varargsMatches;
    }
