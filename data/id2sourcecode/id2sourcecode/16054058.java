    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        Class clazz = this.findLoadedClass(className);
        if (null == clazz) {
            try {
                String classFile = getClassFile(className);
                FileInputStream fis = new FileInputStream(classFile);
                FileChannel fileC = fis.getChannel();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                WritableByteChannel outC = Channels.newChannel(baos);
                ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                while (true) {
                    int i = fileC.read(buffer);
                    if (i == 0 || i == -1) {
                        break;
                    }
                    buffer.flip();
                    outC.write(buffer);
                    buffer.clear();
                }
                fis.close();
                byte[] bytes = baos.toByteArray();
                clazz = defineClass(className, bytes, 0, bytes.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }
