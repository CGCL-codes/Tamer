    public void generate(ClassNode root, PeerGenSettings peerGenSettings) throws java.io.IOException {
        this.peerGenSettings = peerGenSettings;
        String fullFileName = peerGenSettings.getProject().getCPPOutputDir() + File.separatorChar;
        if (root.getPackageName().equals("") == true) fullFileName += root.getCPPClassName() + "Mapping.cpp"; else fullFileName += root.getPackageName().replace('.', File.separatorChar) + File.separatorChar + root.getCPPClassName() + "Mapping.cpp";
        FormattedFileWriter writer = new FormattedFileWriter(fullFileName, true);
        writer.outputLine("#include \"net/sourceforge/jnipp/JVM.h\"");
        writer.outputLine("#include \"net/sourceforge/jnipp/JNIEnvHelper.h\"");
        writer.outputLine("#include \"net/sourceforge/jnipp/EnvironmentAlreadyInitializedException.h\"");
        writer.outputLine("#include \"" + root.getClassName() + "Mapping.h\"");
        writer.outputLine("#include \"" + root.getClassName() + "PeerFactory.h\"");
        writer.newLine(1);
        writer.outputLine("using namespace net::sourceforge::jnipp;");
        Iterator it = root.getNamespaceElements();
        if (it.hasNext() == true) {
            writer.output("using namespace " + (String) it.next());
            while (it.hasNext() == true) writer.output("::" + (String) it.next());
            writer.outputLine(";");
            writer.newLine(1);
        }
        writer.newLine(2);
        generateMethods(root, writer);
        writer.newLine(2);
        writer.flush();
        writer.close();
    }
