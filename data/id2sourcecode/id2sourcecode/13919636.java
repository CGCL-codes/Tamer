    public void testServices() throws IOException {
        count++;
        if (shouldSkip()) return;
        ClassLoader loader = getClass().getClassLoader();
        Enumeration services = loader.getResources("META-INF/services/com.simontuffs.onejar.services.IHelloService");
        int count = 0;
        while (services.hasMoreElements()) {
            URL url = (URL) services.nextElement();
            System.out.println("testServices(): found " + url);
            InputStream is = url.openStream();
            copy(is, null);
            is.close();
            count++;
        }
        if (count != 3) {
            fail("testServices(): incorrect number of services found: should be 3, was " + count + " loader=" + loader);
        } else {
            System.out.println("testServices(): OK: found 3 services");
        }
    }
