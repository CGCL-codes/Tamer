    private void run() throws IllegalClassFormatException, IOException {
        final String jvmClassName = "net/sf/joafip/store/service/bytecode/agent/NestedTryCatch";
        final InputStream stream = ClassLoader.getSystemResourceAsStream(jvmClassName + ".class");
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read;
        while ((read = stream.read()) != -1) {
            byteArrayOutputStream.write(read);
        }
        byteArrayOutputStream.close();
        stream.close();
        final byte[] classfileBuffer = byteArrayOutputStream.toByteArray();
        final ClassReader cr = new ClassReader(classfileBuffer);
        final PrintWriter pw = new PrintWriter("logs/dump.txt");
        CheckClassAdapter.verify(cr, true, pw);
    }
