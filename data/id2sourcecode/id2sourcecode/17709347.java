    private Vector<String> loadTemplates() {
        TreeMap<String, String> templateTM = new TreeMap<String, String>();
        try {
            File dir = new File(TEMPLATE_DIR);
            if (dir.isDirectory()) {
                for (File f : dir.listFiles()) if (!f.isDirectory()) {
                    String name = f.getName();
                    templateTM.put(name, name);
                }
            } else {
                ClassLoader cl = this.getClass().getClassLoader();
                URL url = cl.getResource(ITERATOR_PACKAGE.replace(".", "/"));
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                JarFile jarFile = conn.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String className = entry.getName();
                    if (className.startsWith("template") && className.endsWith(".java")) {
                        URL template = cl.getResource(className);
                        String name = template.toString().substring(template.toString().lastIndexOf("/") + 1);
                        templateTM.put(name, name);
                    }
                }
            }
        } catch (SecurityException ex) {
            templateTM = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return templateTM == null ? null : new Vector<String>(templateTM.values());
    }
