    @Override
    @Entrypoint
    public void run() {
        launched = true;
        if (dbg) VM.sysWriteln("MainThread.run() starting ");
        ClassLoader cl = RVMClassLoader.getApplicationClassLoader();
        setContextClassLoader(cl);
        runAgents(cl);
        if (dbg) VM.sysWrite("[MainThread.run() loading class to run... ");
        RVMClass cls = null;
        try {
            Atom mainAtom = Atom.findOrCreateUnicodeAtom(args[0].replace('.', '/'));
            TypeReference mainClass = TypeReference.findOrCreate(cl, mainAtom.descriptorFromClassName());
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
        for (int i = 0, n = mainArgs.length; i < n; ++i) {
            mainArgs[i] = args[i + 1];
        }
        if (dbg) VM.sysWriteln("made.]");
        if (dbg) VM.sysWrite("[MainThread.run() compiling main(String[])... ");
        mainMethod.compile();
        if (dbg) VM.sysWriteln("compiled.]");
        Callbacks.notifyStartup();
        if (dbg) VM.sysWriteln("[MainThread.run() invoking \"main\" method... ");
        Reflection.invoke(mainMethod, null, null, new Object[] { mainArgs }, true);
        if (dbg) VM.sysWriteln("  MainThread.run(): \"main\" method completed.]");
    }
