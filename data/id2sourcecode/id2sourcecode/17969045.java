    private void run() throws IllegalClassFormatException, IOException {
        final PersistableTransformer transformer = new PersistableTransformer(true);
        final String jvmClassName = "net/sf/joafip/store/service/Store";
        final InputStream stream = ClassLoader.getSystemResourceAsStream(jvmClassName + ".class");
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read;
        while ((read = stream.read()) != -1) {
            byteArrayOutputStream.write(read);
        }
        byteArrayOutputStream.close();
        stream.close();
        System.err.println("transform of " + jvmClassName + " is " + transformer.transformAttribute(jvmClassName));
        final byte[] classfileBuffer = byteArrayOutputStream.toByteArray();
        ClassReader cr = new ClassReader(classfileBuffer);
        PrintWriter pw = new PrintWriter("logs/original_check.txt");
        CheckClassAdapter.verify(cr, true, pw);
        final byte[] generated = transformer.transform(null, jvmClassName, null, null, classfileBuffer);
        cr = new ClassReader(generated);
        pw = new PrintWriter("logs/generated_check.txt");
        CheckClassAdapter.verify(cr, true, pw);
    }
