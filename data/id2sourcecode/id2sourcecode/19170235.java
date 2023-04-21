    public void run() {
        try {
            Socket.setSocketImplFactory(new SocketImplFactory() {

                public SocketImpl createSocketImpl() {
                    return new JikesRVMSocketImpl();
                }
            });
            ServerSocket.setSocketFactory(new SocketImplFactory() {

                public SocketImpl createSocketImpl() {
                    return new JikesRVMSocketImpl();
                }
            });
            DatagramSocket.setDatagramSocketImplFactory(new DatagramSocketImplFactory() {

                public DatagramSocketImpl createDatagramSocketImpl() {
                    throw new VM_UnimplementedError("Need to implement JikesRVMDatagramSocketImpl");
                }
            });
        } catch (java.io.IOException e) {
            VM.sysWrite("trouble setting socket impl factories");
        }
        VM_Controller.boot();
        ClassLoader cl = VM_ClassLoader.getApplicationClassLoader();
        setContextClassLoader(cl);
        VM_Class cls = null;
        try {
            VM_Atom mainAtom = VM_Atom.findOrCreateUnicodeAtom(args[0].replace('.', '/'));
            VM_TypeReference mainClass = VM_TypeReference.findOrCreate(cl, mainAtom.descriptorFromClassName());
            cls = mainClass.resolve().asClass();
            cls.resolve();
            cls.instantiate();
            cls.initialize();
        } catch (ClassNotFoundException e) {
            VM.sysWrite(e + "\n");
            return;
        }
        mainMethod = cls.findMainMethod();
        if (mainMethod == null) {
            VM.sysWrite(cls + " doesn't have a \"public static void main(String[])\" method to execute\n");
            return;
        }
        String[] mainArgs = new String[args.length - 1];
        for (int i = 0, n = mainArgs.length; i < n; ++i) mainArgs[i] = args[i + 1];
        mainMethod.compile();
        VM_Callbacks.notifyStartup();
        VM.debugBreakpoint();
        VM_Magic.invokeMain(mainArgs, mainMethod.getCurrentCompiledMethod().getInstructions());
    }
