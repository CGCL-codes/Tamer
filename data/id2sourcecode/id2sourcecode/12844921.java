    public static void testJarURL() {
        TestCase t = new TestCase("org.mortbay.util.Zip");
        try {
            File tmpJar = File.createTempFile("test", ".jar");
            tmpJar.deleteOnExit();
            URL jar1 = new URL(__userURL + "TestData/test.zip");
            System.err.println(jar1);
            IO.copy(jar1.openStream(), new FileOutputStream(tmpJar));
            URL url1 = new URL("jar:" + tmpJar.toURL() + "!/");
            JarURLConnection jc1 = (JarURLConnection) url1.openConnection();
            JarFile j1 = jc1.getJarFile();
            System.err.println("T1:");
            Enumeration e = j1.entries();
            while (e.hasMoreElements()) System.err.println(e.nextElement());
            URL jar2 = new URL(__userURL + "TestData/alt.zip");
            System.err.println(jar2);
            IO.copy(jar2.openStream(), new FileOutputStream(tmpJar));
            URL url2 = new URL("jar:" + tmpJar.toURL() + "!/");
            JarURLConnection jc2 = (JarURLConnection) url2.openConnection();
            JarFile j2 = jc2.getJarFile();
            System.err.println("T2:");
            e = j2.entries();
            while (e.hasMoreElements()) System.err.println(e.nextElement());
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }
