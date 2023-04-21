            @Override
            public void run() {
                while (serverSocketThreadRunning) {
                    try {
                        SSLSocket federationClient = (SSLSocket) federationServerSocket.accept();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(federationClient.getInputStream()));
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(federationClient.getOutputStream()));
                        FederationRequestType requestType = FederationRequestType.getType(reader.readLine());
                        FederationRequest federationRequest = new FederationRequest(federationClient, reader, writer, requestType);
                        try {
                            FederationSecurityShield.getInstance().verifyRequestPermission(federationRequest);
                        } catch (NoPromptHandlerDefinedException ex) {
                            writer.write(FederationServiceMessageCodes.ACCESS_DENIED.toString() + '\n');
                            writer.flush();
                            reader.close();
                            writer.close();
                            federationClient.close();
                            federationRequest = null;
                            throw ex;
                        } catch (FederationRequestAccessDeniedException ex) {
                            writer.write(FederationServiceMessageCodes.ACCESS_DENIED.toString() + '\n');
                            writer.flush();
                            reader.close();
                            writer.close();
                            federationClient.close();
                            federationRequest = null;
                            throw ex;
                        }
                        FederationRequestHandlerFactory handlerFactory = FederationRequestHandlerFactory.getInstance();
                        FederationRequestHandler requestHandler = handlerFactory.getFederationRequestHandler(requestType);
                        if (requestHandler == null) {
                            writer.write(FederationServiceMessageCodes.UNSUPPORTED_SERVICE.toString() + '\n');
                            writer.flush();
                            reader.close();
                            writer.close();
                            federationClient.close();
                            federationRequest = null;
                            throw new IOException("No handler defined for request: " + requestType);
                        }
                        writer.write(FederationServiceMessageCodes.SERVICE_AVAILABLE.toString() + '\n');
                        writer.flush();
                        FederationServiceStateRegistry.getInstance().addToRegistry(federationRequest);
                        if (uiNotifiers != null) {
                            for (FederationRequestHandlerUINotifier uin : uiNotifiers) requestHandler.addUINotifier(uin);
                        }
                        requestHandler.handleRequest(federationRequest);
                    } catch (IOException ioe) {
                        System.err.println("IOException in " + "federationServerSocketThread: " + ioe);
                        ioe.printStackTrace();
                    } catch (FederationRequestAccessDeniedException fex) {
                        System.err.println("FederationRequestAccessDeniedException in " + "federationServerSocketThread: " + fex);
                        fex.printStackTrace();
                    } catch (NoPromptHandlerDefinedException nex) {
                        System.err.println("NoPromptHandlerDefinedException in " + "federationServerSocketThread: " + nex);
                        nex.printStackTrace();
                    }
                }
            }
