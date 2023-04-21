    public static void main(String[] args) {
        try {
            EventHeap eh = null;
            String svcName = "foobar";
            String[] svcIFs = new String[] { "amiba.samples.C" };
            String mySvcIF = "amiba.samples.A";
            ProxyManagerImpl pm = new ProxyManagerImpl();
            pm.init("repository.pdom", "HTTP", "http://iw-file.stanford.edu:8080/servlet/gd");
            System.out.println("Initted proxy manager");
            System.out.println("Requesting a proxy ..");
            SProxy spxy = pm.getSProxy(mySvcIF, svcIFs);
            System.out.println("Obtained a proxy, instantiating ..");
            Proxy pxy = spxy.instantiate();
            pxy.init(eh, svcName);
            pxy.setDModel(new ADModel());
            Stub tester = pxy.getTester();
            System.out.println("Invoking a(17) in test mode .. \n\n");
            Method ameth = A.class.getDeclaredMethod("a", new Class[] { Integer.TYPE });
            DegradeOptions d = tester.test(ameth, new Object[] { new Integer(17) });
            if (d == null) {
                System.out.println("\n\n17 not supported! No degradation choices ..");
            } else if (d.fullySupported()) {
                System.out.println("\n\n17 supported!");
            } else {
                System.out.println("\n\n17 not supported!");
                Vector v = d.getDegradedCalls();
                if (v == null || v.size() == 0) {
                    System.out.println("No degraded call supported!");
                } else {
                    System.out.println("Degradation choices:");
                    for (int i = 0; i < v.size(); i++) {
                        System.out.println(Arrays.asList((Object[]) v.elementAt(i)));
                    }
                }
            }
            System.out.println("Invoking a(16) in regular mode .. ");
            A a = (A) pxy.getInvoker();
            System.out.println(a.a(16));
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
