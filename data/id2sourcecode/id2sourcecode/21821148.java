    public void test_zip_jar_mix() throws Exception {
        File fooJar = File.createTempFile("hyts_", ".jar");
        File barZip = File.createTempFile("hyts_", ".zip");
        fooJar.deleteOnExit();
        barZip.deleteOnExit();
        Manifest man = new Manifest();
        Attributes att = man.getMainAttributes();
        att.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        att.put(Attributes.Name.MAIN_CLASS, "foo.bar.execjartest.Foo");
        att.put(Attributes.Name.CLASS_PATH, barZip.getName());
        File resources = Support_Resources.createTempFolder();
        JarOutputStream joutFoo = new JarOutputStream(new FileOutputStream(fooJar), man);
        joutFoo.putNextEntry(new JarEntry("foo/bar/execjartest/Foo.class"));
        joutFoo.write(getResource(resources, "hyts_Foo.ser"));
        joutFoo.close();
        ZipOutputStream zoutBar = new ZipOutputStream(new FileOutputStream(barZip));
        zoutBar.putNextEntry(new ZipEntry("foo/bar/execjartest/Bar.class"));
        zoutBar.write(getResource(resources, "hyts_Bar.ser"));
        zoutBar.close();
        String[] args = new String[] { "-jar", fooJar.getAbsolutePath() };
        String res = Support_Exec.execJava(args, null, false);
        assertTrue("Error executing JAR : result returned was incorrect.", res.startsWith("FOOBAR"));
    }
