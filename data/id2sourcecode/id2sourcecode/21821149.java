    public void test_zip_jar_mix_1() throws Exception {
        File fooZip = File.createTempFile("hyts_", ".zip");
        File barJar = File.createTempFile("hyts_", ".jar");
        fooZip.deleteOnExit();
        barJar.deleteOnExit();
        Manifest man = new Manifest();
        Attributes att = man.getMainAttributes();
        att.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        att.put(Attributes.Name.MAIN_CLASS, "foo.bar.execjartest.Foo");
        att.put(Attributes.Name.CLASS_PATH, barJar.getName());
        File resources = Support_Resources.createTempFolder();
        ZipOutputStream zoutFoo = new ZipOutputStream(new FileOutputStream(fooZip));
        zoutFoo.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        man.write(zoutFoo);
        zoutFoo.putNextEntry(new ZipEntry("foo/bar/execjartest/Foo.class"));
        zoutFoo.write(getResource(resources, "hyts_Foo.ser"));
        zoutFoo.close();
        JarOutputStream joutBar = new JarOutputStream(new FileOutputStream(barJar));
        joutBar.putNextEntry(new ZipEntry("foo/bar/execjartest/Bar.class"));
        joutBar.write(getResource(resources, "hyts_Bar.ser"));
        joutBar.close();
        String[] args = new String[] { "-jar", fooZip.getAbsolutePath() };
        String res = Support_Exec.execJava(args, null, false);
        assertTrue("Error executing ZIP : result returned was incorrect.", res.startsWith("FOOBAR"));
    }
