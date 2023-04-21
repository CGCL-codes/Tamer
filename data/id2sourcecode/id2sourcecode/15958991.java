    public javax.swing.JInternalFrame loadExtensionFromURL(URL u) {
        try {
            if (!extensionCache.contains(u)) {
                java.io.File file = java.io.File.createTempFile("extn", "jar");
                java.net.URLConnection urlcon = u.openConnection();
                int len = urlcon.getContentLength();
                System.err.println("extension size :" + len);
                java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.BufferedInputStream(urlcon.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                while (len > 0) {
                    byte[] buf = new byte[len];
                    int read = dis.read(buf);
                    buffer.append(new String(buf));
                    len -= read;
                }
                dis.close();
                System.err.println("** saving extension to :" + file);
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                fos.write(buffer.toString().getBytes());
                fos.close();
                extensionCache.put(u, file);
            }
            ;
            java.util.jar.JarFile jar = new java.util.jar.JarFile((java.io.File) extensionCache.get(u));
            java.util.jar.Manifest manifest = jar.getManifest();
            System.err.println("** manifest :" + manifest);
            String mainClass = manifest.getMainAttributes().getValue("Main-Class");
            System.err.println("** loading class " + mainClass + " **");
            java.net.URLClassLoader classLoader = new java.net.URLClassLoader(new URL[] { u });
            Class klass = classLoader.loadClass(mainClass);
            System.err.println("** declared methods of " + mainClass + " : " + foo(klass.getDeclaredMethods()));
            System.err.println("** methods of " + mainClass + " : " + foo(klass.getMethods()));
            System.err.println("** declared fields of " + mainClass + " : " + foo(klass.getDeclaredFields()));
            System.err.println("** fields of " + mainClass + " : " + foo(klass.getFields()));
            System.err.println("** declared constructors of " + mainClass + " : " + foo(klass.getDeclaredConstructors()));
            System.err.println("** constructors of " + mainClass + " : " + foo(klass.getConstructors()));
            Class[] arg_types = { ReferenceManager.class };
            java.lang.reflect.Constructor ctor = klass.getConstructor(arg_types);
            Object[] args = { ReferenceManager.getInstance() };
            Object obj = ctor.newInstance(args);
            return (javax.swing.JInternalFrame) obj;
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
            return null;
        }
    }
