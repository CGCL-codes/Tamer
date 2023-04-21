    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(ARRAY_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        }
        int id = f.methodId();
        again: for (; ; ) {
            switch(id) {
                case ConstructorId_join:
                case ConstructorId_reverse:
                case ConstructorId_sort:
                case ConstructorId_push:
                case ConstructorId_pop:
                case ConstructorId_shift:
                case ConstructorId_unshift:
                case ConstructorId_splice:
                case ConstructorId_concat:
                case ConstructorId_slice:
                case ConstructorId_indexOf:
                case ConstructorId_lastIndexOf:
                case ConstructorId_every:
                case ConstructorId_filter:
                case ConstructorId_forEach:
                case ConstructorId_map:
                case ConstructorId_some:
                case ConstructorId_reduce:
                case ConstructorId_reduceRight:
                    {
                        if (args.length > 0) {
                            thisObj = ScriptRuntime.toObject(scope, args[0]);
                            Object[] newArgs = new Object[args.length - 1];
                            for (int i = 0; i < newArgs.length; i++) newArgs[i] = args[i + 1];
                            args = newArgs;
                        }
                        id = -id;
                        continue again;
                    }
                case ConstructorId_isArray:
                    return args.length > 0 && (args[0] instanceof NativeArray);
                case Id_constructor:
                    {
                        boolean inNewExpr = (thisObj == null);
                        if (!inNewExpr) {
                            return f.construct(cx, scope, args);
                        }
                        return jsConstructor(cx, scope, args);
                    }
                case Id_toString:
                    return toStringHelper(cx, scope, thisObj, cx.hasFeature(Context.FEATURE_TO_STRING_AS_SOURCE), false);
                case Id_toLocaleString:
                    return toStringHelper(cx, scope, thisObj, false, true);
                case Id_toSource:
                    return toStringHelper(cx, scope, thisObj, true, false);
                case Id_join:
                    return js_join(cx, thisObj, args);
                case Id_reverse:
                    return js_reverse(cx, thisObj, args);
                case Id_sort:
                    return js_sort(cx, scope, thisObj, args);
                case Id_push:
                    return js_push(cx, thisObj, args);
                case Id_pop:
                    return js_pop(cx, thisObj, args);
                case Id_shift:
                    return js_shift(cx, thisObj, args);
                case Id_unshift:
                    return js_unshift(cx, thisObj, args);
                case Id_splice:
                    return js_splice(cx, scope, thisObj, args);
                case Id_concat:
                    return js_concat(cx, scope, thisObj, args);
                case Id_slice:
                    return js_slice(cx, thisObj, args);
                case Id_indexOf:
                    return indexOfHelper(cx, thisObj, args, false);
                case Id_lastIndexOf:
                    return indexOfHelper(cx, thisObj, args, true);
                case Id_every:
                case Id_filter:
                case Id_forEach:
                case Id_map:
                case Id_some:
                    return iterativeMethod(cx, id, scope, thisObj, args);
                case Id_reduce:
                case Id_reduceRight:
                    return reduceMethod(cx, id, scope, thisObj, args);
            }
            throw new IllegalArgumentException(String.valueOf(id));
        }
    }
