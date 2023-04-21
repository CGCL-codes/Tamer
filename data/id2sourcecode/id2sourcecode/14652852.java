    public static void createMidlet(URL midletInputUrl, File midletOutputFile) throws IOException {
        JarInputStream jis = null;
        JarInputStream ijis = null;
        JarOutputStream jos = null;
        InstrumentationConfig config = new InstrumentationConfig();
        config.setEnhanceThreadCreation(false);
        try {
            jis = new JarInputStream(midletInputUrl.openStream());
            Manifest manifest = jis.getManifest();
            if (manifest == null) {
                jos = new JarOutputStream(new FileOutputStream(midletOutputFile));
            } else {
                jos = new JarOutputStream(new FileOutputStream(midletOutputFile), manifest);
            }
            byte[] inputBuffer = new byte[1024];
            JarEntry jarEntry;
            while ((jarEntry = jis.getNextJarEntry()) != null) {
                if (jarEntry.isDirectory() == false) {
                    String name = jarEntry.getName();
                    int size = 0;
                    int read;
                    int length = inputBuffer.length;
                    while ((read = jis.read(inputBuffer, size, length)) > 0) {
                        size += read;
                        length = 1024;
                        if (size + length > inputBuffer.length) {
                            byte[] newInputBuffer = new byte[size + length];
                            System.arraycopy(inputBuffer, 0, newInputBuffer, 0, inputBuffer.length);
                            inputBuffer = newInputBuffer;
                        }
                    }
                    byte[] outputBuffer = inputBuffer;
                    int outputSize = size;
                    if (name.endsWith(".class")) {
                        outputBuffer = ClassPreprocessor.instrument(new ByteArrayInputStream(inputBuffer, 0, size), config);
                        outputSize = outputBuffer.length;
                    }
                    jos.putNextEntry(new JarEntry(name));
                    jos.write(outputBuffer, 0, outputSize);
                }
            }
            URL url = AppletProducer.class.getResource("/microemu-injected.jar");
            if (url != null) {
                ijis = new JarInputStream(url.openStream());
                while ((jarEntry = ijis.getNextJarEntry()) != null) {
                    if (jarEntry.getName().equals("org/microemu/Injected.class")) {
                        jos.putNextEntry(new JarEntry(jarEntry.getName()));
                        int read;
                        while ((read = ijis.read(inputBuffer)) > 0) {
                            jos.write(inputBuffer, 0, read);
                        }
                    }
                }
            } else {
                Logger.error("Cannot find microemu-injected.jar resource in classpath");
            }
        } finally {
            IOUtils.closeQuietly(jis);
            IOUtils.closeQuietly(ijis);
            IOUtils.closeQuietly(jos);
        }
    }
