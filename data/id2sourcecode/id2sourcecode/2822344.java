    protected void createDynaBean(KeelResponse kres, WebappRequest wreq, WebappResponse wres, String modelName) throws ClientException {
        ArrayList inputs = new ArrayList();
        ArrayList outputs = new ArrayList();
        ArrayList commands = new ArrayList();
        Iterator allElements = kres.getAll();
        if (!allElements.hasNext()) {
            log.error("No elements in response from server");
        }
        MessageResources messages = getMessageResources(kres, wreq, wres, modelName);
        ResponseElement re = null;
        for (Iterator i = allElements; i.hasNext(); ) {
            re = (ResponseElement) i.next();
            internationalize(re, messages);
            ResponseElementDynaBean reAsBean = new ResponseElementDynaBean(re);
            wreq.setAttribute(re.getName(), reAsBean);
            if (re instanceof Input) {
                inputs.add(reAsBean);
            } else if (re instanceof Output) {
                final String outputType = (String) re.getAttribute("type");
                if ((outputType != null) && outputType.equals("binary")) {
                    log.debug("File Data is available");
                    final BinaryWrapper data = (BinaryWrapper) ((Output) re).getContent();
                    final long dataSize = data.getSize();
                    if ((dataSize > 0) && (dataSize < Integer.MAX_VALUE)) {
                        hres.setContentLength((int) data.getSize());
                    }
                    hres.setContentType(data.getContentType());
                    hres.setHeader("Content-Disposition", (String) re.getAttribute("Content-Disposition"));
                    BufferedOutputStream buffOut = null;
                    try {
                        log.info("Writing data with no compression");
                        OutputStream out = hres.getOutputStream();
                        buffOut = new BufferedOutputStream(out, BUFFER_SIZE);
                        data.writeTo(buffOut);
                        log.trace("Wrote Buffer.");
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("Exception during file read/write:", e);
                        throw new ClientException("Exception during file read/write", e);
                    } finally {
                        try {
                            data.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            buffOut.flush();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                } else {
                    outputs.add(reAsBean);
                }
            } else if (re instanceof Command) {
                commands.add(reAsBean);
            }
        }
        wreq.setAttribute("inputs", inputs);
        wreq.setAttribute("outputs", outputs);
        wreq.setAttribute("commands", commands);
        int inputCount = 0;
        DynaProperty[] dps = new DynaProperty[inputs.size()];
        ResponseElementDynaBean oneInput = null;
        for (Iterator ii = inputs.iterator(); ii.hasNext(); ) {
            oneInput = (ResponseElementDynaBean) ii.next();
            Object defValue = oneInput.get("defaultValue");
            DynaProperty dp = null;
            if (defValue != null) {
                dp = new DynaProperty((String) oneInput.get("name"), oneInput.get("defaultValue").getClass());
            } else {
                try {
                    dp = new DynaProperty((String) oneInput.get("name"), Class.forName("java.lang.String"));
                } catch (ClassNotFoundException e) {
                    throw new ClientException("Cannot create String dynaproperty", e);
                }
            }
            dps[inputCount++] = dp;
        }
        BasicDynaClass bd;
        try {
            bd = new BasicDynaClass(modelName, Class.forName("org.apache.commons.beanutils.BasicDynaBean"), dps);
            BasicDynaBean newForm = (BasicDynaBean) bd.newInstance();
            for (Iterator i2 = inputs.iterator(); i2.hasNext(); ) {
                oneInput = (ResponseElementDynaBean) i2.next();
                newForm.set((String) oneInput.get("name"), oneInput.get("defaultValue"));
            }
            wreq.setAttribute("default", newForm);
        } catch (ClassNotFoundException e) {
            throw new ClientException(e);
        } catch (IllegalAccessException e) {
            throw new ClientException(e);
        } catch (InstantiationException e) {
            throw new ClientException(e);
        }
    }
