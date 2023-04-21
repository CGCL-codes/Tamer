    @Test
    public void runnableShouldBeAbleToLoadProvider() throws Throwable {
        final IntTestIface iface = mock(IntTestIface.class);
        MockSPI.bind(IntTestIface.class, iface).andMock(new Runnable() {

            @Override
            public void run() {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                Enumeration<URL> resources;
                try {
                    resources = loader.getResources("META-INF/services/" + IntTestIface.class.getName());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                URL url = resources.nextElement();
                try {
                    InputStream in = url.openStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    copy(in, out);
                    String providerClassName = out.toString().trim();
                    Class.forName(providerClassName, true, loader);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
