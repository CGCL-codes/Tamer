    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java -cp inputjar.jar ClassVerifier inputjar.jar");
            System.exit(0);
        }
        JarInputStream in = new JarInputStream(new FileInputStream(args[0]));
        JarOutputStream out = new JarOutputStream(new FileOutputStream(new File("outclasses.jar")));
        JarEntry entry = null;
        while ((entry = in.getNextJarEntry()) != null) {
            if (entry.isDirectory()) continue;
            String name = entry.getName();
            name = name.replace("org/jpc/dynamic/", "org.jpc.dynamic.");
            name = name.replace(".class", "");
            try {
                Class.forName(name).newInstance();
                out.putNextEntry(entry);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                int data = 0;
                while ((data = in.read()) != -1) {
                    bout.write(data);
                }
                out.write(bout.toByteArray());
                out.flush();
                out.closeEntry();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (VerifyError e) {
                System.out.println("Ignoring class: " + name);
            }
        }
        out.close();
    }
