    private JExpression getCopyOfPrimitiveArrayExpression(final ClassOutline classOutline, final JClass arrayType, final JExpression source) {
        if (this.isTargetSupported(TARGET_1_6)) {
            final JClass arrays = classOutline.parent().getCodeModel().ref(Arrays.class);
            return JOp.cond(source.eq(JExpr._null()), JExpr._null(), arrays.staticInvoke("copyOf").arg(source).arg(source.ref("length")));
        }
        final JClass array = classOutline.parent().getCodeModel().ref(Array.class);
        final JClass system = classOutline.parent().getCodeModel().ref(System.class);
        final int mod = this.getVisibilityModifier();
        final String methodName = "copyOf";
        final JType[] signature = new JType[] { arrayType };
        if (mod != JMod.PRIVATE) {
            for (JMethod m : classOutline._package().objectFactory().methods()) {
                if (m.name().equals(methodName) && m.hasSignature(signature)) {
                    return classOutline._package().objectFactory().staticInvoke(m).arg(source);
                }
            }
        } else {
            for (JMethod m : classOutline.implClass.methods()) {
                if (m.name().equals(methodName) && m.hasSignature(signature)) {
                    return JExpr.invoke(m).arg(source);
                }
            }
        }
        final JMethod m = (mod != JMod.PRIVATE ? classOutline._package().objectFactory().method(JMod.STATIC | mod, arrayType, methodName) : classOutline.implClass.method(JMod.STATIC | mod, arrayType, methodName));
        final JVar arrayParam = m.param(JMod.FINAL, arrayType, "array");
        m.javadoc().append("Creates and returns a deep copy of a given array.");
        m.javadoc().addParam(arrayParam).append("The array to copy or {@code null}.");
        m.javadoc().addReturn().append("A deep copy of {@code array} or {@code null} if {@code array} is {@code null}.");
        m.body().directStatement("// " + getMessage("title"));
        final JConditional arrayNotNull = m.body()._if(arrayParam.ne(JExpr._null()));
        final JVar copy = arrayNotNull._then().decl(JMod.FINAL, arrayType, "copy", JExpr.cast(arrayType, array.staticInvoke("newInstance").arg(arrayParam.invoke("getClass").invoke("getComponentType")).arg(arrayParam.ref("length"))));
        arrayNotNull._then().add(system.staticInvoke("arraycopy").arg(arrayParam).arg(JExpr.lit(0)).arg(copy).arg(JExpr.lit(0)).arg(arrayParam.ref("length")));
        arrayNotNull._then()._return(copy);
        m.body()._return(JExpr._null());
        this.methodCount = this.methodCount.add(BigInteger.ONE);
        return (mod != JMod.PRIVATE ? classOutline._package().objectFactory().staticInvoke(m).arg(source) : JExpr.invoke(m).arg(source));
    }
