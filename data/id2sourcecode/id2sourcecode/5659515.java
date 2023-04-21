    @SuppressWarnings("unchecked")
    public ServiceEndpoint CreateService(BundleContext context, Device device, Object implementor, String serviceId, String wsdlName) throws DPWSException {
        ServiceEndpoint ServiceInst = null;
        ServiceClass ServiceClassInst = new ServiceClass();
        try {
            Method[] classMethods = WSDLFactory.getDeclaredMethods();
            Method choosenMethod = null;
            for (int i = 0; i < classMethods.length - 1; i++) {
                if ((classMethods[i].getName().equals("getWSDLInfo")) && ((classMethods[i].getParameterTypes()).length == 1)) {
                    choosenMethod = classMethods[i];
                }
            }
            if (choosenMethod == null) return null;
            Object[] params = new Object[1];
            params[0] = "Service.wsdl";
            Object obj = choosenMethod.invoke(null, params);
            wsdlInfo = (WSDLInfo) obj;
            if (isproxy) ifmappingpe.createtable(obj);
        } catch (Exception e) {
            e.printStackTrace();
            log("Error at searching WSDLFactory", e);
            return null;
        }
        ServiceClassInst.addWebService(wsdlInfo);
        device.getDeviceModel().addServiceClass(ServiceClassInst, serviceId);
        ServiceInst = ServiceClassInst.createService(implementor, serviceId, true);
        wsdlInfo.removeLocation("Service.wsdl");
        Bundle bundle = context.getBundle();
        if ((ServiceUID != null) && (!ServiceUID.equals(""))) {
            ServiceInst.setProperty("UID", ServiceUID);
        } else {
            if (bundle.findEntries("config", "*.uid", true) != null) {
                String line = null;
                for (Enumeration<URL> uids = bundle.findEntries("config", "*.uid", true); uids.hasMoreElements(); ) try {
                    URL url = uids.nextElement();
                    if (url == null) break;
                    InputStream input = (url).openStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(input));
                    line = in.readLine();
                    if (line == null) continue;
                    ServiceUID = line;
                } catch (IOException e) {
                    System.out.println("Cannot read uid: " + e);
                    ServiceInst.setProperty("UID", ((new RandomGUID()).toString()));
                }
                if ((ServiceUID.equals("")) || (ServiceUID == null)) {
                    ServiceUID = ((new RandomGUID()).toString());
                    ServiceInst.setProperty("UID", ServiceUID);
                } else ServiceInst.setProperty("UID", ServiceUID);
            }
        }
        System.out.println("Service UID:" + ServiceUID);
        if (bundle.findEntries("config", "local", true) != null) {
            ServiceInst.setProperty("local", "true");
        }
        if (!loadWsdlFromDir(wsdlName, ServiceInst, bundle, "WSDL")) {
            loadWsdlFromDir(wsdlName, ServiceInst, bundle, "wsdl");
        }
        return ServiceInst;
    }
