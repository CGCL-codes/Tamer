    public javax.swing.JInternalFrame loadExtensionFromURL(URL u) {
        try {
            if (!extensionCache.contains(u)) {
                File file = File.createTempFile("extn", "jar");
                URLConnection urlcon = u.openConnection();
                int len = urlcon.getContentLength();
                logger.debug("extension size :" + len);
                DataInputStream dis = new DataInputStream(new BufferedInputStream(urlcon.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                while (len > 0) {
                    byte[] buf = new byte[len];
                    int read = dis.read(buf);
                    buffer.append(new String(buf));
                    len -= read;
                }
                dis.close();
                logger.debug("** saving extension to :" + file);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(buffer.toString().getBytes());
                fos.close();
                extensionCache.put(u, file);
            }
            JarFile jar = new JarFile(extensionCache.get(u));
            Manifest manifest = jar.getManifest();
            logger.debug("** manifest :" + manifest);
            String mainClass = manifest.getMainAttributes().getValue("Main-Class");
            logger.debug("** loading class " + mainClass + " **");
            URLClassLoader classLoader = new URLClassLoader(new URL[] { u });
            Class klass = classLoader.loadClass(mainClass);
            logger.debug("** declared methods of " + mainClass + " : " + foo(klass.getDeclaredMethods()));
            logger.debug("** methods of " + mainClass + " : " + foo(klass.getMethods()));
            logger.debug("** declared fields of " + mainClass + " : " + foo(klass.getDeclaredFields()));
            logger.debug("** fields of " + mainClass + " : " + foo(klass.getFields()));
            logger.debug("** declared constructors of " + mainClass + " : " + foo(klass.getDeclaredConstructors()));
            logger.debug("** constructors of " + mainClass + " : " + foo(klass.getConstructors()));
            Class[] arg_types = { ReferenceManager.class };
            Constructor ctor = klass.getConstructor(arg_types);
            Object[] args = { ReferenceManager.getInstance() };
            Object obj = ctor.newInstance(args);
            return (JInternalFrame) obj;
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
            return null;
        }
    }
