    void beforeMethod(JoinPoint joinPoint) {
        if (clientSocket == null) {
            return;
        }
        Object joinPointSignature = joinPoint.getSignature();
        String signature;
        Traceable annotation;
        if (joinPointSignature instanceof MethodSignature) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            annotation = method.getAnnotation(Traceable.class);
            signature = buildSignature(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
        } else if (joinPointSignature instanceof ConstructorSignature) {
            Constructor<?> constructor = ((ConstructorSignature) joinPoint.getSignature()).getConstructor();
            annotation = ((Traceable) constructor.getAnnotation(Traceable.class));
            signature = buildSignature(constructor.getDeclaringClass(), constructor.getDeclaringClass().getSimpleName(), constructor.getParameterTypes());
        } else {
            throw new IllegalArgumentException("Only methods and constructors can be traced");
        }
        int[] injectableExceptions = annotation == null ? NO_INJECTABLES : annotation.injectableExceptions();
        writeToClientAndProcessResponse(STACK_FRAME_START_ACTION, Thread.currentThread().getName(), signature, injectableExceptions);
    }
