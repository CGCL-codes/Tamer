    protected void writeSingleTask(FragmentGeneratorContext context, Task task) throws UnableToCompleteException {
        TreeLogger logger = context.parentLogger.branch(TreeLogger.DEBUG, "Writing Task " + task.getFieldName(context.parentLogger), null);
        context = new FragmentGeneratorContext(context);
        context.parentLogger = logger;
        logger.log(TreeLogger.DEBUG, "Implementing task " + context.fieldName, null);
        if (task.getter != null) {
            context.returnType = task.getter.getReturnType();
            context.parameterName = context.objRef + "." + context.fieldName;
            writeGetter(context, task.getter);
        }
        if (task.imported != null) {
            context.returnType = task.imported.getReturnType();
            writeImported(context, task.imported);
        }
        if (task.setter != null) {
            if (context.readOnly) {
                logger.log(TreeLogger.ERROR, "Unable to write property setter on read-only wrapper.", null);
                throw new UnableToCompleteException();
            }
            JParameter parameter = getSetterParameter(task.setter);
            context.returnType = parameter.getType();
            context.parameterName = parameter.getName();
            writeSetter(context, task.setter);
        }
        if (task.constructor != null) {
            context.returnType = task.constructor.getReturnType();
            context.parameterName = "this.";
            context.objRef = "this";
            writeConstructor(context, task.constructor);
        }
    }
