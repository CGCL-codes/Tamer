    public void init(Context cx) {
        initStandardObjects(cx, sealedStdLib);
        String[] names = { "defineClass", "loadClass", "deserialize", "serialize", "include", "use", "load", "print", "readFile", "readUrl", "writeFile", "writeUrl", "runCommand", "seal", "spawn", "sync", "toint32", "version", "help", "quit" };
        defineFunctionProperties(names, Global.class, ScriptableObject.DONTENUM);
        Environment.defineClass(this);
        Environment environment = new Environment(this);
        defineProperty("environment", environment, ScriptableObject.DONTENUM);
        history = (NativeArray) cx.newArray(this, 0);
        defineProperty("history", history, ScriptableObject.DONTENUM);
        initialized = true;
    }
