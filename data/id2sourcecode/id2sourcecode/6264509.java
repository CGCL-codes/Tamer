    protected MethodTarget findMethodByActionName(Parse cells, int allArgs) throws Exception {
        int parms = allArgs / 2 + 1;
        int argCount = (allArgs + 1) / 2;
        String name = cells.text();
        for (int i = 1; i < parms; i++) name += " " + cells.at(i * 2).text();
        MethodTarget target = findMethod(ExtendedCamelCase.camel(name), argCount);
        target.setEverySecond(true);
        return target;
    }
