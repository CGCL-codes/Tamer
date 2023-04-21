    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(STRING_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        again: for (; ; ) {
            switch(id) {
                case ConstructorId_charAt:
                case ConstructorId_charCodeAt:
                case ConstructorId_indexOf:
                case ConstructorId_lastIndexOf:
                case ConstructorId_split:
                case ConstructorId_substring:
                case ConstructorId_toLowerCase:
                case ConstructorId_toUpperCase:
                case ConstructorId_substr:
                case ConstructorId_concat:
                case ConstructorId_slice:
                case ConstructorId_equalsIgnoreCase:
                case ConstructorId_match:
                case ConstructorId_search:
                case ConstructorId_replace:
                case ConstructorId_localeCompare:
                case ConstructorId_toLocaleLowerCase:
                    {
                        if (args.length > 0) {
                            thisObj = ScriptRuntime.toObject(scope, ScriptRuntime.toString(args[0]));
                            Object[] newArgs = new Object[args.length - 1];
                            for (int i = 0; i < newArgs.length; i++) newArgs[i] = args[i + 1];
                            args = newArgs;
                        } else {
                            thisObj = ScriptRuntime.toObject(scope, ScriptRuntime.toString(thisObj));
                        }
                        id = -id;
                        continue again;
                    }
                case ConstructorId_fromCharCode:
                    {
                        int N = args.length;
                        if (N < 1) return "";
                        StringBuffer sb = new StringBuffer(N);
                        for (int i = 0; i != N; ++i) {
                            sb.append(ScriptRuntime.toUint16(args[i]));
                        }
                        return sb.toString();
                    }
                case Id_constructor:
                    {
                        String s = (args.length >= 1) ? ScriptRuntime.toString(args[0]) : "";
                        if (thisObj == null) {
                            return new NativeString(s);
                        }
                        return s;
                    }
                case Id_toString:
                case Id_valueOf:
                    return realThis(thisObj, f).string;
                case Id_toSource:
                    {
                        String s = realThis(thisObj, f).string;
                        return "(new String(\"" + ScriptRuntime.escapeString(s) + "\"))";
                    }
                case Id_charAt:
                case Id_charCodeAt:
                    {
                        String target = ScriptRuntime.toString(thisObj);
                        double pos = ScriptRuntime.toInteger(args, 0);
                        if (pos < 0 || pos >= target.length()) {
                            if (id == Id_charAt) return ""; else return ScriptRuntime.NaNobj;
                        }
                        char c = target.charAt((int) pos);
                        if (id == Id_charAt) return String.valueOf(c); else return ScriptRuntime.wrapInt(c);
                    }
                case Id_indexOf:
                    return ScriptRuntime.wrapInt(js_indexOf(ScriptRuntime.toString(thisObj), args));
                case Id_lastIndexOf:
                    return ScriptRuntime.wrapInt(js_lastIndexOf(ScriptRuntime.toString(thisObj), args));
                case Id_split:
                    return ScriptRuntime.checkRegExpProxy(cx).js_split(cx, scope, ScriptRuntime.toString(thisObj), args);
                case Id_substring:
                    return js_substring(cx, ScriptRuntime.toString(thisObj), args);
                case Id_toLowerCase:
                    return ScriptRuntime.toString(thisObj).toLowerCase(ScriptRuntime.ROOT_LOCALE);
                case Id_toUpperCase:
                    return ScriptRuntime.toString(thisObj).toUpperCase(ScriptRuntime.ROOT_LOCALE);
                case Id_substr:
                    return js_substr(ScriptRuntime.toString(thisObj), args);
                case Id_concat:
                    return js_concat(ScriptRuntime.toString(thisObj), args);
                case Id_slice:
                    return js_slice(ScriptRuntime.toString(thisObj), args);
                case Id_bold:
                    return tagify(thisObj, "b", null, null);
                case Id_italics:
                    return tagify(thisObj, "i", null, null);
                case Id_fixed:
                    return tagify(thisObj, "tt", null, null);
                case Id_strike:
                    return tagify(thisObj, "strike", null, null);
                case Id_small:
                    return tagify(thisObj, "small", null, null);
                case Id_big:
                    return tagify(thisObj, "big", null, null);
                case Id_blink:
                    return tagify(thisObj, "blink", null, null);
                case Id_sup:
                    return tagify(thisObj, "sup", null, null);
                case Id_sub:
                    return tagify(thisObj, "sub", null, null);
                case Id_fontsize:
                    return tagify(thisObj, "font", "size", args);
                case Id_fontcolor:
                    return tagify(thisObj, "font", "color", args);
                case Id_link:
                    return tagify(thisObj, "a", "href", args);
                case Id_anchor:
                    return tagify(thisObj, "a", "name", args);
                case Id_equals:
                case Id_equalsIgnoreCase:
                    {
                        String s1 = ScriptRuntime.toString(thisObj);
                        String s2 = ScriptRuntime.toString(args, 0);
                        return ScriptRuntime.wrapBoolean((id == Id_equals) ? s1.equals(s2) : s1.equalsIgnoreCase(s2));
                    }
                case Id_match:
                case Id_search:
                case Id_replace:
                    {
                        int actionType;
                        if (id == Id_match) {
                            actionType = RegExpProxy.RA_MATCH;
                        } else if (id == Id_search) {
                            actionType = RegExpProxy.RA_SEARCH;
                        } else {
                            actionType = RegExpProxy.RA_REPLACE;
                        }
                        return ScriptRuntime.checkRegExpProxy(cx).action(cx, scope, thisObj, args, actionType);
                    }
                case Id_localeCompare:
                    {
                        Collator collator = Collator.getInstance(cx.getLocale());
                        collator.setStrength(Collator.IDENTICAL);
                        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
                        return ScriptRuntime.wrapNumber(collator.compare(ScriptRuntime.toString(thisObj), ScriptRuntime.toString(args, 0)));
                    }
                case Id_toLocaleLowerCase:
                    {
                        return ScriptRuntime.toString(thisObj).toLowerCase(cx.getLocale());
                    }
                case Id_toLocaleUpperCase:
                    {
                        return ScriptRuntime.toString(thisObj).toUpperCase(cx.getLocale());
                    }
                case Id_trim:
                    {
                        String str = ScriptRuntime.toString(thisObj);
                        char[] chars = str.toCharArray();
                        int start = 0;
                        while (start < chars.length && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[start])) {
                            start++;
                        }
                        int end = chars.length;
                        while (end > start && ScriptRuntime.isJSWhitespaceOrLineTerminator(chars[end - 1])) {
                            end--;
                        }
                        return str.substring(start, end);
                    }
            }
            throw new IllegalArgumentException(String.valueOf(id));
        }
    }
