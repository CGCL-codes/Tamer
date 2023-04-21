        public void perform(Context context) {
            SimpleProvider config;
            VariablesContext var;
            Service service;
            Object ret;
            Call call;
            Object[] parameter;
            String method;
            String wsurl;
            String xmlDoc;
            int actual;
            URL url;
            try {
                var = Variables.getContext();
                actual = Integer.parseInt((String) context.get(CTX_ACTUAL));
                wsurl = var.get(actual + "." + XML_URL);
                method = var.get(actual + "." + XML_METHOD);
                if (wsurl == null || method == null) {
                    System.out.println("----- Did not get method or wsurl from the properties! -----");
                    return;
                }
                url = new java.net.URL(wsurl);
                try {
                    url.openConnection().connect();
                } catch (IOException ex) {
                    System.out.println("----- Could not connect to the webservice! -----");
                }
                xmlDoc = (String) context.get(CTX_XML);
                if (xmlDoc == null) xmlDoc = var.get(CTX_XML);
                Vector v_param = new Vector();
                v_param.add(xmlDoc);
                v_param.add(new Long(var.get(CTX_DOCID)));
                parameter = v_param.toArray();
                config = new SimpleProvider();
                config.deployTransport("http", new HTTPSender());
                service = new Service(config);
                call = (Call) service.createCall();
                call.setTargetEndpointAddress(url);
                call.setOperationName(new QName("http://schemas.xmlsoap.org/soap/encoding/", method));
                try {
                    ret = call.invoke(parameter);
                    System.out.println("Returned: " + ret);
                    context.set(CTX_XML, ret);
                } catch (RemoteException ex) {
                    System.out.println("----- Could not invoke the method! -----");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
