    private void write(Fault fault) throws JClassAlreadyExistsException {
        String className = Names.customExceptionClassName(fault);
        JDefinedClass cls = cm._class(className, ClassType.CLASS);
        JDocComment comment = cls.javadoc();
        if (fault.getJavaDoc() != null) {
            comment.add(fault.getJavaDoc());
            comment.add("\n\n");
        }
        for (String doc : getJAXWSClassComment()) {
            comment.add(doc);
        }
        cls._extends(java.lang.Exception.class);
        JAnnotationUse faultAnn = cls.annotate(WebFault.class);
        faultAnn.param("name", fault.getBlock().getName().getLocalPart());
        faultAnn.param("targetNamespace", fault.getBlock().getName().getNamespaceURI());
        JType faultBean = fault.getBlock().getType().getJavaType().getType().getType();
        JFieldVar fi = cls.field(JMod.PRIVATE, faultBean, "faultInfo");
        fault.getBlock().getType().getJavaType().getType().annotate(fi);
        fi.javadoc().add("Java type that goes as soapenv:Fault detail element.");
        JFieldRef fr = JExpr.ref(JExpr._this(), fi);
        JMethod constrc1 = cls.constructor(JMod.PUBLIC);
        JVar var1 = constrc1.param(String.class, "message");
        JVar var2 = constrc1.param(faultBean, "faultInfo");
        constrc1.javadoc().addParam(var1);
        constrc1.javadoc().addParam(var2);
        JBlock cb1 = constrc1.body();
        cb1.invoke("super").arg(var1);
        cb1.assign(fr, var2);
        JMethod constrc2 = cls.constructor(JMod.PUBLIC);
        var1 = constrc2.param(String.class, "message");
        var2 = constrc2.param(faultBean, "faultInfo");
        JVar var3 = constrc2.param(Throwable.class, "cause");
        constrc2.javadoc().addParam(var1);
        constrc2.javadoc().addParam(var2);
        constrc2.javadoc().addParam(var3);
        JBlock cb2 = constrc2.body();
        cb2.invoke("super").arg(var1).arg(var3);
        cb2.assign(fr, var2);
        JMethod fim = cls.method(JMod.PUBLIC, faultBean, "getFaultInfo");
        fim.javadoc().addReturn().add("returns fault bean: " + faultBean.fullName());
        JBlock fib = fim.body();
        fib._return(fi);
        fault.setExceptionClass(cls);
    }
