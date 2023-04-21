    public JWSClassLoader(String name, ClassLoader cl, String classFile) throws FileNotFoundException, IOException {
        super(cl);
        this.name = name + ".class";
        this.classFile = classFile;
        FileInputStream fis = new FileInputStream(classFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        for (int i = 0; (i = fis.read(buf)) != -1; ) baos.write(buf, 0, i);
        fis.close();
        baos.close();
        byte[] data = baos.toByteArray();
        defineClass(name, data, 0, data.length);
        ClassUtils.setClassLoader(name, this);
    }
