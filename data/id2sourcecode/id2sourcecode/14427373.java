    public static Set aggregateSessions(Set sessions, String method) {
        Set retSet = new TreeSet();
        try {
            String[] elements = method.split("~");
            String strMethod = elements[0];
            String[] params = new String[elements.length - 1];
            Class[] cArr = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                params[i] = elements[i + 1];
                cArr[i] = String.class;
            }
            java.lang.reflect.Method meth = Session.class.getMethod(strMethod, cArr);
            Iterator sIt = sessions.iterator();
            while (sIt.hasNext()) retSet.addAll((Collection) meth.invoke(sIt.next(), params));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return retSet;
    }
