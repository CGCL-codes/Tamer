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
                    assertNotNull(providerClassName);
                    assertTrue(providerClassName.startsWith("M"));
                    assertTrue(providerClassName.endsWith("_0"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
