    @Override
    public String writeToString() throws ConditionThrowable {
        final LispThread thread = LispThread.currentThread();
        if (SymbolConstants.PRINT_ESCAPE.symbolValue(thread) == NIL) {
            String s = getMessage();
            if (s != null) return s;
            LispObject formatControl = getFormatControl();
            if (formatControl instanceof Function) {
                StringOutputStream stream = new StringOutputStream();
                SymbolConstants.APPLY.execute(formatControl, stream, getFormatArguments());
                return stream.getStringOutputString().getStringValue();
            }
            if (formatControl instanceof AbstractString) {
                LispObject f = SymbolConstants.FORMAT.getSymbolFunction();
                if (f == null || f instanceof Autoload) return format(formatControl, getFormatArguments());
                return SymbolConstants.APPLY.execute(f, NIL, formatControl, getFormatArguments()).getStringValue();
            }
        }
        final int maxLevel;
        LispObject printLevel = SymbolConstants.PRINT_LEVEL.symbolValue(thread);
        if (printLevel instanceof Fixnum) maxLevel = printLevel.intValue(); else maxLevel = Integer.MAX_VALUE;
        LispObject currentPrintLevel = _CURRENT_PRINT_LEVEL_.symbolValue(thread);
        int currentLevel = currentPrintLevel.intValue();
        if (currentLevel >= maxLevel) return "#";
        return unreadableString(typeOf().writeToString());
    }
