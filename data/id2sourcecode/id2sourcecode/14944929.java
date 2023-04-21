        public void perform(Context context) {
            Long workflowIdParentProcess;
            SimpleProvider config;
            VariablesContext var;
            String xmldocument;
            Service service;
            Object[] param;
            String method;
            String wsurl;
            Object ret;
            Call call;
            URL url;
            try {
                var = Variables.getContext();
                if (var.get("wsurl") == null || var.get("method") == null) {
                    System.out.println("----- Did not get method or wsurl from the properties! -----");
                    return;
                }
                method = var.get("method");
                wsurl = var.get("wsurl");
                url = new java.net.URL(wsurl);
                try {
                    url.openConnection().connect();
                } catch (IOException ex) {
                    System.out.println("----- Could not connect to the webservice! -----");
                }
                if (var.get("param0") == null || var.get("param1") == null) {
                    System.out.println("----- Need parameters! -----");
                    return;
                }
                xmldocument = var.get("param0");
                workflowIdParentProcess = new Long(var.get("param1"));
                param = new Object[] { xmldocument, workflowIdParentProcess };
                config = new SimpleProvider();
                config.deployTransport("http", new HTTPSender());
                service = new Service(config);
                call = (Call) service.createCall();
                call.setTargetEndpointAddress(url);
                call.setOperationName(new QName("http://schemas.xmlsoap.org/soap/encoding/", method));
                try {
                    ret = call.invoke(param);
                    context.set(CTX_ANSW, "=> notifyIhk invoked - Result: " + ret);
                } catch (RemoteException ex) {
                    System.out.println("----- Could not invoke the method! -----");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
