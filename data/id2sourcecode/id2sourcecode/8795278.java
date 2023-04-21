    @Override
    public String push(String message) {
        String result = null;
        if (message != null && !message.trim().equals("")) {
            Class stringClass = message.getClass();
            String[] data = message.split("\\s+");
            Class[] argTypes = new Class[data.length - 1];
            for (int i = 0; i < argTypes.length; i++) {
                argTypes[i] = stringClass;
            }
            Object[] args = new Object[data.length - 1];
            for (int i = 0; i < args.length; i++) {
                args[i] = data[i + 1];
            }
            try {
                Method m = cmds.getClass().getMethod(data[0], argTypes);
                if (m == null) {
                    StringBuffer err = new StringBuffer();
                    err.append("no method named '");
                    err.append(data[0]);
                    err.append("' taking ");
                    err.append(data.length - 1);
                    err.append(" strings as arguments");
                    throw new NoSuchMethodException(err.toString());
                }
                Object o = m.invoke(cmds, args);
                if (o == null) {
                    result = "OK";
                } else {
                    result = o.toString();
                }
            } catch (Throwable t) {
                t.printStackTrace();
                result = "ERR: " + t.toString();
            }
        }
        return result;
    }
