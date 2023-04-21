    public void run() {
        VM_Controller.boot();
        VM_ApplicationClassLoader.setPathProperty();
        ClassLoader cl = new VM_ApplicationClassLoader(VM_SystemClassLoader.getVMClassLoader());
        String[] mainArgs = null;
        VM_Class cls = null;
        try {
            cls = (VM_Class) cl.loadClass(args[0], true).getVMType();
        } catch (ClassNotFoundException e) {
            VM.sysWrite(e + "\n");
            return;
        }
        mainMethod = cls.findMainMethod();
        if (mainMethod == null) {
            VM.sysWrite(cls.getName() + " doesn't have a \"public static void main(String[])\" method to execute\n");
            return;
        }
        mainArgs = new String[args.length - 1];
        for (int i = 0, n = mainArgs.length; i < n; ++i) mainArgs[i] = args[i + 1];
        mainMethod.compile();
        VM_Callbacks.notifyStartup();
        VM.debugBreakpoint();
        VM_Magic.invokeMain(mainArgs, mainMethod.getCurrentCompiledMethod().getInstructions());
    }
