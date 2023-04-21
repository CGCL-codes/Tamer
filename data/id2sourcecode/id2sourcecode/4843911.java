    public void initCustomObjects(AskariShell shell) {
        String[] names = { "defineClass", "loadClass", "serialize", "deserialize", "use", "include", "load", "print", "readFile", "readUrl", "writeFile", "writeUrl", "runCommand", "seal", "spawn", "sync", "persist", "remember", "forget" };
        shell.defineFunctionProperties(names, AskariShell.class, ScriptableObject.DONTENUM);
    }
