    public String getConditionReport() throws ConditionThrowable {
        String s = getMessage();
        if (s != null) return s;
        LispObject formatControl = getFormatControl();
        if (formatControl != NIL) {
            try {
                return format(formatControl, getFormatArguments());
            } catch (Throwable t) {
            }
        }
        return unreadableString(typeOf().writeToString());
    }
