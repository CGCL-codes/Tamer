    @SuppressWarnings("unchecked")
    private static <C, T> GetSetProperty<C, T> createGetSetProperty(ClassPool classPool, final ClassInfo<C> classInfo, final Class<C> clazz, Method readMethod, Method writeMethod, Class<?> theType, String propertyName) throws NotFoundException, CannotCompileException, IOException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        final ClassPool pool = classPool != null ? classPool : new ClassPool();
        final String clazzName = clazz.getName();
        final String valueClazzName = theType.getName();
        final String newClassName;
        {
            StringBuilder sb = new StringBuilder(512);
            sb.append(GetSetProperty.class.getName()).append("$").append(clazzName.replace('.', '_'));
            if (readMethod != null) {
                sb.append("$").append(readMethod.getName());
            }
            if (writeMethod != null) {
                sb.append("$").append(writeMethod.getName());
            }
            sb.append("$").append((valueClazzName != null ? valueClazzName : valueClazzName).replace('.', '_'));
            newClassName = sb.toString();
        }
        CtClass newClass = pool.makeClass(newClassName);
        newClass.setSuperclass(GETSETPROPERTY_CTCLASS);
        {
            CtConstructor cons = new CtConstructor(new CtClass[] { CLASSINFO_CTCLASS, STRING_CTCLASS, CLASS_CTCLASS }, newClass);
            cons.setBody("super($1, $2, $3);");
            newClass.addConstructor(cons);
        }
        {
            CtMethod meth = new CtMethod(OBJECT_CTCLASS, "get", new CtClass[] { OBJECT_CTCLASS }, newClass);
            if (readMethod != null) {
                meth.setBody("return ($w)((" + clazzName + ")$1)." + readMethod.getName() + "();");
            } else {
                meth.setBody("throw new IllegalStateException(\"" + propertyName + "\");");
            }
            newClass.addMethod(meth);
        }
        {
            CtMethod meth = new CtMethod(CtClass.voidType, "set", new CtClass[] { OBJECT_CTCLASS, OBJECT_CTCLASS }, newClass);
            if (writeMethod != null) {
                if (theType.isPrimitive()) {
                    {
                        CtMethod castMeth = new CtMethod(pool.get(valueClazzName), "cast", new CtClass[] { OBJECT_CTCLASS }, newClass);
                        castMeth.setBody("return ($r)$1;");
                        newClass.addMethod(castMeth);
                    }
                    meth.setBody("{" + valueClazzName + " xx = this.cast($2);" + "((" + clazzName + ")$1)." + writeMethod.getName() + "(xx);" + "}");
                } else {
                    meth.setBody("((" + clazzName + ")$1)." + writeMethod.getName() + "((" + valueClazzName + ")$2);");
                }
            } else {
                meth.setBody("throw new IllegalStateException(\"" + propertyName + "\");");
            }
            newClass.addMethod(meth);
        }
        {
            CtMethod meth = new CtMethod(CtClass.booleanType, "isReadable", NO_CTARGS, newClass);
            meth.setBody("return " + (readMethod != null) + ";");
            newClass.addMethod(meth);
        }
        {
            CtMethod meth = new CtMethod(CtClass.booleanType, "isWriteable", NO_CTARGS, newClass);
            meth.setBody("return " + (writeMethod != null) + ";");
            newClass.addMethod(meth);
        }
        Class<GetSetProperty<C, T>> theClass = pool.toClass(newClass);
        newClass.detach();
        Constructor<GetSetProperty<C, T>> ctor = theClass.getConstructor(ClassInfo.class, String.class, Class.class);
        GetSetProperty<C, T> res = ctor.newInstance(classInfo, propertyName, convertType(theType));
        return res;
    }
