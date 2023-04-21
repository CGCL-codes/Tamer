    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream pw = response.getOutputStream();
        String command = request.getParameter("command");
        if (null == command || command.length() == 0) {
            try {
                StringWriter strWrit = new StringWriter();
                XmlWriter writ = new XmlWriter(strWrit);
                writ.writeEntity("services");
                Enumeration servicesEnum = Settings.getInstance().getPropertyNames("services");
                while (servicesEnum.hasMoreElements()) {
                    String nextService = (String) servicesEnum.nextElement();
                    String nextServiceUrl = Settings.get("services." + nextService);
                    writ.writeEntity("service");
                    writ.writeEntity("ID").writeText(nextService).endEntity();
                    writ.writeEntity("URL").writeText(nextServiceUrl).endEntity();
                    writ.endEntity();
                }
                writ.endEntity();
                writ.close();
                response.setContentType("text/xml");
                pw.print(strWrit.getBuffer().toString());
                return;
            } catch (WritingException ex1) {
                ex1.printStackTrace();
            }
        }
        String asyncActionStr = request.getParameter("async");
        boolean async = (null != asyncActionStr && asyncActionStr.equalsIgnoreCase("true"));
        String asyncOrder = null;
        if (async) asyncOrder = request.getParameter("asyncorder");
        boolean returnFile = false;
        String[] adds = request.getParameterValues("extension");
        for (int i = 0; adds != null && i < adds.length; i++) {
            if (adds[i].equalsIgnoreCase("returnFile")) {
                returnFile = true;
            }
        }
        String resource = null;
        String service = null;
        StringWriter strWrit = new StringWriter();
        try {
            service = request.getParameter("service");
            if (null == service || service.length() == 0) {
                response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Not found parameter service. Please include it into the query string.");
                return;
            }
            DataRequestExecutionResource drer = null;
            try {
                String serviceStr = Settings.get("services." + service);
                if (null != serviceStr && serviceStr.length() > 0) {
                    URL serverBaseUrl = new URL(serviceStr);
                    uk.org.ogsadai.resource.ResourceID drerId = new uk.org.ogsadai.resource.ResourceID("DataRequestExecutionResource");
                    ServerProxy serverProxy = new ServerProxy();
                    serverProxy.setDefaultBaseServicesURL(serverBaseUrl);
                    drer = serverProxy.getDataRequestExecutionResource(drerId);
                } else {
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Service " + service + " is not found.");
                    return;
                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                throw ex;
            }
            resource = request.getParameter("resource");
            if (null == resource || resource.length() == 0) {
                response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Not found parameter resource. Please include it into the query string.");
                return;
            }
            XmlWriter writ = new XmlWriter(strWrit);
            writ.writeEntity("perform");
            String format = request.getParameter("format");
            if (null != format) {
                writ.writeEntity("value").writeAttribute("name", "format").writeAttribute("value", format).endEntity();
            }
            PipelineWorkflow pipeline = new PipelineWorkflow();
            DeliverToRequestStatus deliverToRequestStatus = new DeliverToRequestStatus();
            DeliverToRequestStatus deliverToRequestStatusOut = null;
            DeliverToRequestStatus deliverToRequestStatusErr = null;
            pipeline.add(deliverToRequestStatus);
            if (command.equalsIgnoreCase("get")) {
                makeGetRequest(writ, request, response, pw);
                writ.endEntity();
                writ.close();
                GetData getData = new GetData();
                getData.setResourceID(new ResourceID(resource));
                log.debug("Making request: " + strWrit.getBuffer().toString() + " " + resource);
                getData.getInput().add(new StringData(strWrit.getBuffer().toString()));
                deliverToRequestStatus.connectInput(getData.getOutput());
                pipeline.add(getData);
            } else if (command.equalsIgnoreCase("shift")) {
                makeShiftRequest(writ, request, response, pw);
                writ.endEntity();
                writ.close();
                ShiftData getData = new ShiftData();
                getData.setResourceID(resource);
                log.debug("Making request: " + strWrit.getBuffer().toString());
                getData.getInput().add(new StringData(strWrit.getBuffer().toString()));
                deliverToRequestStatus.connectInput(getData.getOutput());
                pipeline.add(getData);
            } else if (command.equalsIgnoreCase("view")) {
                makeViewRequest(writ, request, response, pw);
                writ.endEntity();
                writ.close();
                ViewData viewData = new ViewData();
                viewData.setResourceID(resource);
                log.debug("Making request: " + strWrit.getBuffer().toString());
                viewData.getInput().add(new StringData(strWrit.getBuffer().toString()));
                deliverToRequestStatus.connectInput(viewData.getOutput());
                pipeline.add(viewData);
            } else if (command.equalsIgnoreCase("download")) {
                deliverToRequestStatusOut = new DeliverToRequestStatus();
                deliverToRequestStatusErr = new DeliverToRequestStatus();
                makeDownloadRequest(writ, request, response, pw);
                writ.endEntity();
                writ.close();
                DownloadData downData = new DownloadData();
                downData.setResourceID(new ResourceID(resource));
                log.debug("Making request: " + strWrit.getBuffer().toString() + " " + resource);
                downData.getInput().add(new StringData(strWrit.getBuffer().toString()));
                deliverToRequestStatus.connectInput(downData.getOutputs()[0].getSingleActivityOutputs()[0]);
                deliverToRequestStatusOut.connectInput(downData.getOutputs()[1].getSingleActivityOutputs()[0]);
                deliverToRequestStatusErr.connectInput(downData.getOutputs()[2].getSingleActivityOutputs()[0]);
                pipeline.add(deliverToRequestStatusOut);
                pipeline.add(deliverToRequestStatusErr);
                pipeline.add(downData);
            } else if (command.equalsIgnoreCase("inventory")) {
            } else if (command.equalsIgnoreCase("metadata")) {
                writ.endEntity();
                writ.close();
                GetMetadata getData = new GetMetadata();
                getData.setResourceID(resource);
                deliverToRequestStatus.connectInput(getData.getOutput());
                pipeline.add(getData);
            } else if (command.equalsIgnoreCase("provenance")) {
                writ.endEntity();
                writ.close();
                GetProvenance getData = new GetProvenance();
                getData.setResourceID(resource);
                deliverToRequestStatus.connectInput(getData.getOutput());
                pipeline.add(getData);
            }
            if (async) {
                try {
                    if (command.equalsIgnoreCase("status")) {
                        StringWriter strwrit = new StringWriter();
                        XmlWriter outwrit = new XmlWriter(strwrit);
                        outwrit.writeEntity("Response");
                        if (null != asyncRequests.get(asyncOrder)) {
                            AsyncJob job = (AsyncJob) asyncRequests.get(asyncOrder);
                            outwrit.writeEntity("status");
                            if (job.getRequestResource().getRequestStatus().getExecutionStatus().hasFinished()) {
                                outwrit.writeText("finished");
                            } else {
                                outwrit.writeText("processing");
                            }
                            outwrit.endEntity();
                        } else {
                            outwrit.writeEntity("status").writeText("Order " + asyncOrder + " is not found.").endEntity();
                        }
                        outwrit.endEntity();
                        outwrit.close();
                        pw.print(strwrit.toString());
                    } else if (command.equalsIgnoreCase("result")) {
                        if (null != asyncRequests.get(asyncOrder)) {
                            AsyncJob job = (AsyncJob) asyncRequests.get(asyncOrder);
                            if (job.getRequestResource().getRequestStatus().getExecutionStatus().hasFinished()) {
                                pw.print(job.getStatus().getDataValueIterator().next().toString());
                                asyncRequests.remove(asyncOrder);
                            }
                        } else {
                            StringWriter strwrit = new StringWriter();
                            XmlWriter outwrit = new XmlWriter(strwrit);
                            outwrit.writeEntity("Response");
                            outwrit.writeEntity("status").writeText("Order " + asyncOrder + " is not found.").endEntity();
                            outwrit.endEntity();
                            outwrit.close();
                            pw.print(strwrit.toString());
                        }
                    } else {
                        StringWriter strwrit = new StringWriter();
                        XmlWriter outwrit = new XmlWriter(strwrit);
                        outwrit.writeEntity("Response");
                        String procId = new RandomGUID().toString();
                        uk.org.ogsadai.client.toolkit.RequestResource rs = drer.execute(pipeline, RequestExecutionType.ASYNCHRONOUS);
                        AsyncJob job = new AsyncJob();
                        job.setPipeline(pipeline);
                        job.setRequestResource(rs);
                        job.setStatus(deliverToRequestStatus);
                        asyncRequests.put(procId, job);
                        outwrit.writeEntity("OrderId").writeText(procId).endEntity();
                        outwrit.endEntity();
                        outwrit.close();
                        pw.print(strwrit.toString());
                    }
                } catch (ClientToolkitException ex) {
                    ex.printStackTrace();
                } catch (ClientException ex) {
                    ex.printStackTrace();
                } catch (ClientServerCompatibilityException ex) {
                    ex.printStackTrace();
                } catch (ResourceUnknownException ex) {
                    ex.printStackTrace();
                } catch (ServerException ex) {
                    ex.printStackTrace();
                } catch (ServerCommsException ex) {
                    ex.printStackTrace();
                } catch (DataSourceUsageException ex) {
                    ex.printStackTrace();
                } catch (UnexpectedDataValueException ex) {
                    ex.printStackTrace();
                } catch (DataStreamErrorException ex) {
                    ex.printStackTrace();
                } catch (RequestException ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    uk.org.ogsadai.client.toolkit.RequestResource rs = drer.execute(pipeline, RequestExecutionType.SYNCHRONOUS);
                    if (!returnFile) {
                        pw.print(deliverToRequestStatus.getDataValueIterator().next().toString());
                        if (null != deliverToRequestStatusOut && deliverToRequestStatusOut.getDataValueIterator().hasNext()) {
                            pw.print("\n");
                            pw.print(deliverToRequestStatusOut.getDataValueIterator().next().toString());
                        }
                        if (null != deliverToRequestStatusErr && deliverToRequestStatusErr.getDataValueIterator().hasNext()) {
                            pw.print("\n");
                            pw.print(deliverToRequestStatusErr.getDataValueIterator().next().toString());
                        }
                    } else {
                        URL imgUrl = new URL(deliverToRequestStatus.getDataValueIterator().next().toString());
                        URLConnection urlConn = imgUrl.openConnection();
                        response.setContentType(urlConn.getContentType());
                        byte[] buf = new byte[1024];
                        InputStream inp = urlConn.getInputStream();
                        int read = inp.read(buf);
                        while (read > 0) {
                            pw.write(buf, 0, read);
                            read = inp.read(buf);
                        }
                        inp.close();
                    }
                } catch (ClientToolkitException ex) {
                    ex.printStackTrace();
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (RequestErrorException ex) {
                    ex.printStackTrace();
                    Throwable er = ex;
                    while (null != er.getCause()) {
                        er = er.getCause();
                    }
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, er.getMessage());
                } catch (RequestException ex) {
                    ex.printStackTrace();
                    Throwable er = ex;
                    while (null != er.getCause()) {
                        er = er.getCause();
                    }
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, er.getMessage());
                } catch (ClientException ex) {
                    ex.printStackTrace();
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (ResourceUnknownException ex) {
                    ex.printStackTrace();
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (ServerException ex) {
                    ex.printStackTrace();
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (ServerCommsException ex) {
                    ex.printStackTrace();
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (DataSourceUsageException ex) {
                    ex.printStackTrace();
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (UnexpectedDataValueException ex) {
                    ex.printStackTrace();
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                } catch (DataStreamErrorException ex) {
                    ex.printStackTrace();
                    response.sendError(response.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                }
            }
        } catch (WritingException ex1) {
            ex1.printStackTrace();
        }
    }
