    @SuppressWarnings("unchecked")
    public static void startGeneratorThread(CachedDevice cd) {
        try {
            String host = cd.getDefaultTransportAddress().substring(cd.getDefaultTransportAddress().indexOf("//") + 2, cd.getDefaultTransportAddress().lastIndexOf("/"));
            String port = host.substring(host.indexOf(":") + 1, host.length());
            String ipadresse = host.substring(0, host.indexOf(":"));
            String serviceName = null;
            ServiceProxy csp = null;
            Iterator it = cd.getHostedServices().iterator();
            while (it.hasNext()) {
                ServiceProxy sp = (CachedService) it.next();
                Iterator nit = sp.getTypes().iterator();
                while (nit.hasNext()) {
                    QName q = (QName) nit.next();
                    System.out.println("Found Service " + q.getLocalPart().toLowerCase() + "service");
                    if (serviceName == null) {
                        serviceName = q.getLocalPart().toLowerCase() + "service";
                        csp = sp;
                    } else {
                        throw new DPWSException("Found more then one Service on device ... don't know which one to handle!");
                    }
                }
            }
            if (isDuplicate(cd.getDefaultTransportAddress())) {
                return;
            }
            if (!(ProxyUtils.isnewProxyInstance(serviceName))) {
                int counter = 2;
                boolean newname = ProxyUtils.isnewProxyInstance(serviceName + counter);
                while (!(newname)) {
                    counter++;
                    newname = ProxyUtils.isnewProxyInstance(serviceName + counter);
                }
                serviceName = serviceName + counter;
            }
            boolean MSOAenabled = false;
            if (cd.getScopes().contains("http://www.ist-more.org/MSOADevice")) MSOAenabled = true;
            String WSDLFileName = serviceName + ".wsdl";
            URL url = new URL(csp.getDefaultEndpoint().getAddress() + "/getwsdl");
            URLConnection conn = url.openConnection();
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = dis.readLine()) != null) {
                sb.append(inputLine);
            }
            dis.close();
            byte[] b = TypeConversation.toByta(sb.toString());
            ProxyUtils.cleanupWorkingDir();
            ProxyUtils.writeWSDLtoFile(b, WSDLFileName);
            @SuppressWarnings("unused") Thread generationThread = new Thread(new ProxyUtils.callBundleGeneratorThread(serviceName, ProxyUtils.workingdir + "//WSDL//" + WSDLFileName, csp.getDefaultEndpoint().getAddress(), MSOAenabled, csp.getDefaultEndpoint().getAddress()));
        } catch (DPWSException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
