    public void generateInitMethod(ClassNode root, FormattedFileWriter writer) throws java.io.IOException {
        String namespace = root.getPackageName().replace('.', '_');
        if (namespace == null || namespace.equals("") == true) namespace = "Java_"; else namespace = "Java_" + namespace + "_";
        writer.outputLine("JNIEXPORT void JNICALL " + namespace + root.getClassName() + "Proxy_init(JNIEnv* env, jclass cls)");
        writer.outputLine("{");
        writer.incTabLevel();
        writer.outputLine("// This method is called by the Java Proxy init() on the other side of the JNI to initialize the environment");
        writer.outputLine("try");
        writer.outputLine("{");
        writer.incTabLevel();
        writer.outputLine("JNIEnvHelper::init( env );");
        writer.outputLine("JNINativeMethod nativeMethods[] =");
        writer.outputLine("{");
        writer.incTabLevel();
        writer.outputLine("{ \"releasePeer\", \"()V\", (void*)" + namespace + root.getClassName() + "Proxy_releasePeer },");
        Iterator it = root.getMethods();
        while (it.hasNext() == true) {
            MethodNode node = (MethodNode) it.next();
            writer.output("{ \"" + node.getName() + "\", \"" + node.getJNISignature() + "\", (void*)" + namespace + root.getClassName() + "Proxy_");
            writer.output((peerGenSettings.getUseRichTypes() == true ? node.getCPPName() : node.getUniqueCPPName()) + " }");
            if (it.hasNext() == true) writer.output(",");
            writer.outputLine("");
        }
        writer.decTabLevel();
        writer.outputLine("};");
        writer.newLine(1);
        writer.outputLine("JNIEnvHelper::RegisterNatives( cls, nativeMethods, sizeof(nativeMethods)/sizeof(nativeMethods[0]) );");
        writer.decTabLevel();
        writer.outputLine("}");
        writer.outputLine("catch(EnvironmentAlreadyInitializedException&)");
        writer.outputLine("{");
        writer.outputLine("}");
        writer.decTabLevel();
        writer.outputLine("}");
    }
