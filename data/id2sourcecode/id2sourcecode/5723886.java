    private static void enhanceFile(String fullClassName, String prefix, String bundleId, URL[] classpath) throws ClassNotFoundException, IOException {
        int idx = fullClassName.indexOf("/@dot/");
        if (idx < 0) {
            idx = fullClassName.indexOf("/bin/");
            if (idx < 0) {
                return;
            } else {
                idx += "/bin/".length();
            }
        } else {
            idx += "/@dot/".length();
        }
        String className = fullClassName.substring(idx).replace("/", ".");
        className = className.substring(0, className.lastIndexOf("."));
        URL url = new URL("file://" + new File(SerializableEnhancerUtils.getBinDir(fullClassName)).getCanonicalPath() + "/");
        List<URL> urlList = new ArrayList<URL>();
        URL jdoUrl = new URL("file://" + new File("../SchemaBuilder/lib/jdo2-api-2.3-ec.jar").getCanonicalPath());
        URL eclipseUrl = new URL("file://" + new File("/home/sl/dev/eclipse/plugins/org.eclipse.core.jobs_3.5.100.v20110404.jar").getCanonicalPath());
        URL eclipseCoreruntime = new URL("file://" + new File("/home/sl/dev/eclipse/plugins/org.eclipse.equinox.common_3.6.0.v20110523.jar").getCanonicalPath());
        urlList.add(jdoUrl);
        urlList.add(eclipseUrl);
        urlList.add(eclipseCoreruntime);
        urlList.add(url);
        for (URL urItem : classpath) {
            urlList.add(urItem);
        }
        URL[] urls = new URL[urlList.size()];
        urls = urlList.toArray(urls);
        URLClassLoader l1 = null;
        if (params.isUseHardcode()) {
            ClassLoader clloader = Thread.currentThread().getContextClassLoader();
            l1 = new URLClassLoader(urls, clloader);
        } else {
            l1 = new URLClassLoader(urls, null);
        }
        System.out.println("className: " + className);
        Class<?> cl = l1.loadClass(className);
        if (cl.isInterface()) {
            return;
        }
        String packageName = cl.getPackage().getName();
        if (isClassNeedToBeEnhanced(cl, packageName)) {
            boolean isSerializable = isSerializable(cl, packageName);
            System.out.println("Enhance class: " + className);
            InputStream is = new FileInputStream(new File(fullClassName));
            ClassReader cr = new ClassReader(is);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassAdapter ca = new SerializableEnhancer(cw, l1, bundleId, isSerializable);
            cr.accept(ca, 0);
            FileOutputStream fos = new FileOutputStream(new File(fullClassName));
            fos.write(cw.toByteArray());
            fos.close();
        } else {
            System.out.println("Class " + className + " already enhanced...");
        }
    }
