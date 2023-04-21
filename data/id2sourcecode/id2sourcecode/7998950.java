    public Vset checkValue(Environment env, Context ctx, Vset vset, Hashtable exp) {
        ClassDeclaration c = null;
        boolean isArray = false;
        boolean staticRef = false;
        MemberDefinition implMethod = null;
        ClassDefinition ctxClass = ctx.field.getClassDefinition();
        Expression args[] = this.args;
        if (id.equals(idInit)) {
            ClassDefinition conCls = ctxClass;
            try {
                Expression conOuter = null;
                if (right instanceof SuperExpression) {
                    conCls = conCls.getSuperClass().getClassDefinition(env);
                    conOuter = ((SuperExpression) right).outerArg;
                } else if (right instanceof ThisExpression) {
                    conOuter = ((ThisExpression) right).outerArg;
                }
                args = NewInstanceExpression.insertOuterLink(env, ctx, where, conCls, conOuter, args);
            } catch (ClassNotFound ee) {
            }
        }
        Type argTypes[] = new Type[args.length];
        ClassDefinition sourceClass = ctxClass;
        try {
            if (right == null) {
                staticRef = ctx.field.isStatic();
                ClassDefinition cdef = ctxClass;
                MemberDefinition m = null;
                for (; cdef != null; cdef = cdef.getOuterClass()) {
                    m = cdef.findAnyMethod(env, id);
                    if (m != null) {
                        break;
                    }
                }
                if (m == null) {
                    c = ctx.field.getClassDeclaration();
                } else {
                    c = cdef.getClassDeclaration();
                    if (m.getClassDefinition() != cdef) {
                        ClassDefinition cdef2 = cdef;
                        while ((cdef2 = cdef2.getOuterClass()) != null) {
                            MemberDefinition m2 = cdef2.findAnyMethod(env, id);
                            if (m2 != null && m2.getClassDefinition() == cdef2) {
                                env.error(where, "inherited.hides.method", id, cdef.getClassDeclaration(), cdef2.getClassDeclaration());
                                break;
                            }
                        }
                    }
                }
            } else {
                if (id.equals(idInit)) {
                    int thisN = ctx.getThisNumber();
                    if (!ctx.field.isConstructor()) {
                        env.error(where, "invalid.constr.invoke");
                        return vset.addVar(thisN);
                    }
                    if (!vset.isReallyDeadEnd() && vset.testVar(thisN)) {
                        env.error(where, "constr.invoke.not.first");
                        return vset;
                    }
                    vset = vset.addVar(thisN);
                    if (right instanceof SuperExpression) {
                        vset = right.checkAmbigName(env, ctx, vset, exp, this);
                    } else {
                        vset = right.checkValue(env, ctx, vset, exp);
                    }
                } else {
                    vset = right.checkAmbigName(env, ctx, vset, exp, this);
                    if (right.type == Type.tPackage) {
                        FieldExpression.reportFailedPackagePrefix(env, right);
                        return vset;
                    }
                    if (right instanceof TypeExpression) {
                        staticRef = true;
                    }
                }
                if (right.type.isType(TC_CLASS)) {
                    c = env.getClassDeclaration(right.type);
                } else if (right.type.isType(TC_ARRAY)) {
                    isArray = true;
                    c = env.getClassDeclaration(Type.tObject);
                } else {
                    if (!right.type.isType(TC_ERROR)) {
                        env.error(where, "invalid.method.invoke", right.type);
                    }
                    return vset;
                }
                if (right instanceof FieldExpression) {
                    Identifier id = ((FieldExpression) right).id;
                    if (id == idThis) {
                        sourceClass = ((FieldExpression) right).clazz;
                    } else if (id == idSuper) {
                        isSuper = true;
                        sourceClass = ((FieldExpression) right).clazz;
                    }
                } else if (right instanceof SuperExpression) {
                    isSuper = true;
                }
                if (id != idInit) {
                    if (!FieldExpression.isTypeAccessible(where, env, right.type, sourceClass)) {
                        ClassDeclaration cdecl = sourceClass.getClassDeclaration();
                        if (staticRef) {
                            env.error(where, "no.type.access", id, right.type.toString(), cdecl);
                        } else {
                            env.error(where, "cant.access.member.type", id, right.type.toString(), cdecl);
                        }
                    }
                }
            }
            boolean hasErrors = false;
            if (id.equals(idInit)) {
                vset = vset.clearVar(ctx.getThisNumber());
            }
            for (int i = 0; i < args.length; i++) {
                vset = args[i].checkValue(env, ctx, vset, exp);
                argTypes[i] = args[i].type;
                hasErrors = hasErrors || argTypes[i].isType(TC_ERROR);
            }
            if (id.equals(idInit)) {
                vset = vset.addVar(ctx.getThisNumber());
            }
            if (hasErrors) {
                return vset;
            }
            clazz = c.getClassDefinition(env);
            if (field == null) {
                field = clazz.matchMethod(env, sourceClass, id, argTypes);
                if (field == null) {
                    if (id.equals(idInit)) {
                        if (diagnoseMismatch(env, args, argTypes)) return vset;
                        String sig = clazz.getName().getName().toString();
                        sig = Type.tMethod(Type.tError, argTypes).typeString(sig, false, false);
                        env.error(where, "unmatched.constr", sig, c);
                        return vset;
                    }
                    String sig = id.toString();
                    sig = Type.tMethod(Type.tError, argTypes).typeString(sig, false, false);
                    if (clazz.findAnyMethod(env, id) == null) {
                        if (ctx.getField(env, id) != null) {
                            env.error(where, "invalid.method", id, c);
                        } else {
                            env.error(where, "undef.meth", sig, c);
                        }
                    } else if (diagnoseMismatch(env, args, argTypes)) {
                    } else {
                        env.error(where, "unmatched.meth", sig, c);
                    }
                    return vset;
                }
            }
            type = field.getType().getReturnType();
            if (staticRef && !field.isStatic()) {
                env.error(where, "no.static.meth.access", field, field.getClassDeclaration());
                return vset;
            }
            if (field.isProtected() && !(right == null) && !(right instanceof SuperExpression || (right instanceof FieldExpression && ((FieldExpression) right).id == idSuper)) && !sourceClass.protectedAccess(env, field, right.type)) {
                env.error(where, "invalid.protected.method.use", field.getName(), field.getClassDeclaration(), right.type);
                return vset;
            }
            if (right instanceof FieldExpression && ((FieldExpression) right).id == idSuper) {
                if (!field.isPrivate()) {
                    if (sourceClass != ctxClass) {
                        implMethod = sourceClass.getAccessMember(env, ctx, field, true);
                    }
                }
            }
            if (implMethod == null && field.isPrivate()) {
                ClassDefinition cdef = field.getClassDefinition();
                if (cdef != ctxClass) {
                    implMethod = cdef.getAccessMember(env, ctx, field, false);
                }
            }
            if (field.isAbstract() && (right != null) && (right.op == SUPER)) {
                env.error(where, "invoke.abstract", field, field.getClassDeclaration());
                return vset;
            }
            if (field.reportDeprecated(env)) {
                if (field.isConstructor()) {
                    env.error(where, "warn.constr.is.deprecated", field);
                } else {
                    env.error(where, "warn.meth.is.deprecated", field, field.getClassDefinition());
                }
            }
            if (field.isConstructor() && ctx.field.equals(field)) {
                env.error(where, "recursive.constr", field);
            }
            if (sourceClass == ctxClass) {
                ClassDefinition declarer = field.getClassDefinition();
                if (!field.isConstructor() && declarer.isPackagePrivate() && !declarer.getName().getQualifier().equals(sourceClass.getName().getQualifier())) {
                    field = MemberDefinition.makeProxyMember(field, clazz, env);
                }
            }
            sourceClass.addDependency(field.getClassDeclaration());
            if (sourceClass != ctxClass) {
                ctxClass.addDependency(field.getClassDeclaration());
            }
        } catch (ClassNotFound ee) {
            env.error(where, "class.not.found", ee.name, ctx.field);
            return vset;
        } catch (AmbiguousMember ee) {
            env.error(where, "ambig.field", id, ee.field1, ee.field2);
            return vset;
        }
        if ((right == null) && !field.isStatic()) {
            right = ctx.findOuterLink(env, where, field);
            vset = right.checkValue(env, ctx, vset, exp);
        }
        argTypes = field.getType().getArgumentTypes();
        for (int i = 0; i < args.length; i++) {
            args[i] = convert(env, ctx, argTypes[i], args[i]);
        }
        if (field.isConstructor()) {
            MemberDefinition m = field;
            if (implMethod != null) {
                m = implMethod;
            }
            int nargs = args.length;
            Expression[] newargs = args;
            if (nargs > this.args.length) {
                Expression rightI;
                if (right instanceof SuperExpression) {
                    rightI = new SuperExpression(right.where, ctx);
                    ((SuperExpression) right).outerArg = args[0];
                } else if (right instanceof ThisExpression) {
                    rightI = new ThisExpression(right.where, ctx);
                } else {
                    throw new CompilerError("this.init");
                }
                if (implMethod != null) {
                    newargs = new Expression[nargs + 1];
                    this.args = new Expression[nargs];
                    newargs[0] = args[0];
                    this.args[0] = newargs[1] = new NullExpression(where);
                    for (int i = 1; i < nargs; i++) {
                        this.args[i] = newargs[i + 1] = args[i];
                    }
                } else {
                    for (int i = 1; i < nargs; i++) {
                        this.args[i - 1] = args[i];
                    }
                }
                implementation = new MethodExpression(where, rightI, m, newargs);
                implementation.type = type;
            } else {
                if (implMethod != null) {
                    newargs = new Expression[nargs + 1];
                    newargs[0] = new NullExpression(where);
                    for (int i = 0; i < nargs; i++) {
                        newargs[i + 1] = args[i];
                    }
                }
                implementation = new MethodExpression(where, right, m, newargs);
            }
        } else {
            if (args.length > this.args.length) {
                throw new CompilerError("method arg");
            }
            if (implMethod != null) {
                Expression oldargs[] = this.args;
                if (field.isStatic()) {
                    Expression call = new MethodExpression(where, null, implMethod, oldargs);
                    implementation = new CommaExpression(where, right, call);
                } else {
                    int nargs = oldargs.length;
                    Expression newargs[] = new Expression[nargs + 1];
                    newargs[0] = right;
                    for (int i = 0; i < nargs; i++) {
                        newargs[i + 1] = oldargs[i];
                    }
                    implementation = new MethodExpression(where, null, implMethod, newargs);
                }
            }
        }
        if (ctx.field.isConstructor() && field.isConstructor() && (right != null) && (right.op == SUPER)) {
            Expression e = makeVarInits(env, ctx);
            if (e != null) {
                if (implementation == null) implementation = (Expression) this.clone();
                implementation = new CommaExpression(where, implementation, e);
            }
        }
        ClassDeclaration exceptions[] = field.getExceptions(env);
        if (isArray && (field.getName() == idClone) && (field.getType().getArgumentTypes().length == 0)) {
            exceptions = new ClassDeclaration[0];
            for (Context p = ctx; p != null; p = p.prev) {
                if (p.node != null && p.node.op == TRY) {
                    ((TryStatement) p.node).arrayCloneWhere = where;
                }
            }
        }
        for (int i = 0; i < exceptions.length; i++) {
            if (exp.get(exceptions[i]) == null) {
                exp.put(exceptions[i], this);
            }
        }
        if (ctx.field.isConstructor() && field.isConstructor() && (right != null) && (right.op == THIS)) {
            ClassDefinition cls = field.getClassDefinition();
            for (MemberDefinition f = cls.getFirstMember(); f != null; f = f.getNextMember()) {
                if (f.isVariable() && f.isBlankFinal() && !f.isStatic()) {
                    vset = vset.addVar(ctx.getFieldNumber(f));
                }
            }
        }
        return vset;
    }
