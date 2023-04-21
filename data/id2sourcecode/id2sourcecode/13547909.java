    private Class<PropertyAccessStrategy> createPropertyAccessStrategyClass(String className, String propertyName, Member readMember, Member writeMember, boolean usesFieldAccess) {
        try {
            String propertyAccessStrategyClassName = getPropertyAccessStrategyClassName(className, propertyName);
            ClassPool pool = ClassPool.getDefault();
            CtClass strategy = pool.makeClass(propertyAccessStrategyClassName);
            strategy.setInterfaces(new CtClass[] { pool.get(PropertyAccessStrategy.class.getName()) });
            strategy.addConstructor(CtNewConstructor.defaultConstructor(strategy));
            CtMethod readMethod = CtNewMethod.make(getReadMethod(className, readMember, usesFieldAccess), strategy);
            strategy.addMethod(readMethod);
            CtMethod writeMethod = CtNewMethod.make(getWriteMethod(className, writeMember, usesFieldAccess), strategy);
            strategy.addMethod(writeMethod);
            return strategy.toClass();
        } catch (NotFoundException e) {
            throw new IllegalStateException(e);
        } catch (CannotCompileException e) {
            throw new IllegalStateException(e);
        }
    }
