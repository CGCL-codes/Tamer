    public void test_putNextEntryLjava_util_zip_ZipEntry() throws Exception {
        final String testClass = "hyts_mainClass.ser";
        final String entryName = "foo/bar/execjartest/MainClass.class";
        final String[] manifestMain = { "foo.bar.execjartest.MainClass", "foo/bar/execjartest/MainClass" };
        for (String element : manifestMain) {
            Manifest newman = new Manifest();
            Attributes att = newman.getMainAttributes();
            att.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            att.put(Attributes.Name.MAIN_CLASS, element);
            File outputJar = null;
            JarOutputStream jout = null;
            outputJar = File.createTempFile("hyts_", ".jar");
            jout = new JarOutputStream(new FileOutputStream(outputJar), newman);
            jout.putNextEntry(new JarEntry(entryName));
            File resources = Support_Resources.createTempFolder();
            Support_Resources.copyFile(resources, null, testClass);
            URL jarURL = new URL((new File(resources, testClass)).toURL().toString());
            InputStream jis = jarURL.openStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = jis.read(bytes)) != -1) {
                jout.write(bytes, 0, len);
            }
            jout.flush();
            jout.close();
            jis.close();
            String res = null;
            String[] args = new String[2];
            args[0] = "-jar";
            args[1] = outputJar.getAbsolutePath();
            res = Support_Exec.execJava(args, null, true);
            assertTrue("Error executing JAR test on: " + element + ". Result returned was incorrect.", res.startsWith("TEST"));
            outputJar.delete();
        }
    }
