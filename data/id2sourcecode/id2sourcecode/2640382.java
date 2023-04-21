    private static TclObject getConstructorInfoList(Interp interp, Class c) throws TclException {
        Constructor[] constructorArray = FuncSig.getAccessibleConstructors(c);
        TclObject resultListObj = TclList.newInstance();
        TclObject elementObj, sigObj;
        for (int m = 0; m < constructorArray.length; ++m) {
            sigObj = TclList.newInstance();
            elementObj = TclString.newInstance(constructorArray[m].getName());
            TclList.append(interp, sigObj, elementObj);
            Class[] paramArray = constructorArray[m].getParameterTypes();
            for (int p = 0; p < paramArray.length; ++p) {
                elementObj = TclString.newInstance(getNameFromClass(paramArray[p]));
                TclList.append(interp, sigObj, elementObj);
            }
            TclList.append(interp, resultListObj, sigObj);
        }
        return resultListObj;
    }
