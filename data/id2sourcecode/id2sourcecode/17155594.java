    public String[] getDependencies(final boolean includeRootSet) throws IOException {
        final Set _result = new HashSet();
        final ClassLoader loader = new URLClassLoader(m_classPath, null);
        if (includeRootSet) {
            for (int i = 0; i < m_rootSet.length; ++i) {
                _result.add(m_rootSet[i]);
            }
        }
        final LinkedList queue = new LinkedList();
        for (int i = 0; i < m_rootSet.length; ++i) {
            queue.add(Descriptors.javaNameToVMName(m_rootSet[i]));
        }
        final ByteArrayOStream baos = new ByteArrayOStream(8 * 1024);
        final byte[] readbuf = new byte[8 * 1024];
        while (!queue.isEmpty()) {
            final String classVMName = (String) queue.removeFirst();
            InputStream in = null;
            try {
                in = loader.getResourceAsStream(classVMName + ".class");
                if (in == null) {
                    throw new IllegalArgumentException("class [" + Descriptors.vmNameToJavaName(classVMName) + "] not found in the input classpath");
                } else {
                    baos.reset();
                    for (int read; (read = in.read(readbuf)) >= 0; baos.write(readbuf, 0, read)) ;
                }
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException ignore) {
                    ignore.printStackTrace();
                }
            }
            in = null;
            final ClassDef cls = ClassDefParser.parseClass(baos.getByteArray(), baos.size());
            final List clsDeps = getCONSTANT_Class_info(cls);
            for (Iterator i = clsDeps.iterator(); i.hasNext(); ) {
                final String classDepVMName = (String) i.next();
                if (classDepVMName.startsWith("com/vladium/")) {
                    if (_result.add(Descriptors.vmNameToJavaName(classDepVMName))) {
                        queue.addLast(classDepVMName);
                    }
                }
            }
        }
        final String[] result = new String[_result.size()];
        _result.toArray(result);
        return result;
    }
