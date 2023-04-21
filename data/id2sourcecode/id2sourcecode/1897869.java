    public void run() {
        if (dbg) VM.sysWriteln("MainThread.run() starting ");
        ClassLoader cl = VM_ClassLoader.getApplicationClassLoader();
        setContextClassLoader(cl);
        runAgents(cl);
        if (dbg) VM.sysWrite("[MainThread.run() loading class to run... ");
        VM_Class cls = null;
        try {
            VM_Atom mainAtom = VM_Atom.findOrCreateUnicodeAtom(args[0].replace('.', '/'));
            VM_TypeReference mainClass = VM_TypeReference.findOrCreate(cl, mainAtom.descriptorFromClassName());
            cls = mainClass.resolve().asClass();
            cls.resolve();
            cls.instantiate();
            cls.initialize();
        } catch (NoClassDefFoundError e) {
            if (dbg) VM.sysWrite("failed.]");
            VM.sysWrite(e + "\n");
            return;
        }
        if (dbg) VM.sysWriteln("loaded.]");
        mainMethod = cls.findMainMethod();
        if (mainMethod == null) {
            VM.sysWrite(cls + " doesn't have a \"public static void main(String[])\" method to execute\n");
            return;
        }
        if (dbg) VM.sysWrite("[MainThread.run() making arg list... ");
        String[] mainArgs = new String[args.length - 1];
        for (int i = 0, n = mainArgs.length; i < n; ++i) mainArgs[i] = args[i + 1];
        if (dbg) VM.sysWriteln("made.]");
        if (dbg) VM.sysWrite("[MainThread.run() compiling main(String[])... ");
        mainMethod.compile();
        if (dbg) VM.sysWriteln("compiled.]");
        VM_Callbacks.notifyStartup();
        launched = true;
        if (dbg) VM.sysWriteln("[MainThread.run() invoking \"main\" method... ");
        VM_Reflection.invoke(mainMethod, null, new Object[] { mainArgs }, false);
        if (dbg) VM.sysWriteln("  MainThread.run(): \"main\" method completed.]");
    }
