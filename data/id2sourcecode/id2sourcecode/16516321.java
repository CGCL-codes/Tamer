    public static Expression inlineApplyToArgs(ApplyExp exp, InlineCalls walker, boolean argsInlined, Procedure applyToArgs) {
        Expression[] args = exp.getArgs();
        int nargs = args.length - 1;
        if (nargs >= 0) {
            Expression proc = args[0];
            if (!argsInlined) {
                if (proc instanceof LambdaExp) {
                    Expression[] rargs = new Expression[nargs];
                    System.arraycopy(args, 1, rargs, 0, nargs);
                    return walker.walk(new ApplyExp(proc, rargs));
                }
                proc = walker.walk(proc);
                args[0] = proc;
            }
            Type ptype = proc.getType();
            ApplyExp result;
            Compilation comp = walker.getCompilation();
            Language language = comp.getLanguage();
            if (ptype.isSubtype(Compilation.typeProcedure)) {
                Expression[] rargs = new Expression[nargs];
                System.arraycopy(args, 1, rargs, 0, nargs);
                return proc.inline(new ApplyExp(proc, rargs), walker, null, argsInlined);
            }
            if (!argsInlined) {
                for (int i = 1; i <= nargs; i++) args[i] = walker.walk(args[i]);
            }
            if (Invoke.checkKnownClass(ptype, comp) < 0) return exp;
            ClassType ctype;
            if (ptype.isSubtype(Compilation.typeType) || language.getTypeFor(proc, false) != null) {
                result = new ApplyExp(Invoke.make, args);
            } else if (ptype instanceof ArrayType) {
                Type elementType = ((ArrayType) ptype).getComponentType();
                result = new ApplyExp(new ArrayGet(elementType), args);
            } else if (ptype instanceof ClassType && (ctype = (ClassType) ptype).isSubclass(typeList) && nargs == 1) {
                Method get = ctype.getMethod("get", new Type[] { Type.intType });
                result = new ApplyExp(get, args);
            } else return exp;
            result.setLine(exp);
            return ((InlineCalls) walker).walkApplyOnly(result);
        }
        return exp;
    }
