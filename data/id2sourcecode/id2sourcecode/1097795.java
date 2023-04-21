        public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) throws ProxyException, SOAPFaultException {
            if (method.getDeclaringClass().equals(Object.class) || method.getDeclaringClass().equals(Stub.class)) {
                try {
                    return method.invoke(this, args);
                } catch (IllegalArgumentException e) {
                    throw new ProxyException("Illegal argument for target method!", e);
                } catch (IllegalAccessException e) {
                    throw new ProxyException("Illegal access to target method!", e);
                } catch (InvocationTargetException e) {
                    Throwable target = e.getTargetException();
                    if (target == null) {
                        target = e;
                    }
                    throw new ProxyException("Error invoking target method!", target);
                }
            }
            if (args == null || args.length == 0) {
                throw new ProxyException("Method has no arguments!");
            }
            SOAPMessage request = new SOAPMessage();
            boolean isMultipart = false;
            for (int i = 0; i < args.length; ++i) {
                Object obj = args[i];
                if (obj instanceof DataHandler) {
                    isMultipart = true;
                    try {
                        request.addAttachment((DataHandler) obj);
                    } catch (IOException e) {
                        throw new ProxyException("Error adding attachment to request!", e);
                    }
                }
            }
            if (isMultipart) {
                request.setMimeHeader(HTTPConstants.CONTENT_TYPE, HTTPConstants.MULTIPART_RELATED);
            } else {
                request.setMimeHeader(HTTPConstants.CONTENT_TYPE, HTTPConstants.TEXT_XML);
            }
            Object requestVal = args[0];
            if (!(requestVal instanceof DataHandler)) {
                Collection namespaces = new ArrayList();
                if (_clientConfig.containsKey(_loader)) {
                    Client client = (Client) _clientConfig.get(_loader);
                    namespaces = client.getNamespaces();
                }
                Marshaller m = getMarshaller();
                if (m == null) {
                    m = Marshaller.Factory.newInstance();
                }
                Node node = m.marshall(requestVal, namespaces);
                if (node instanceof Document) {
                    request.setBodyElement(((Document) node).getDocumentElement());
                } else if (node instanceof Element) {
                    request.setBodyElement((Element) node);
                } else {
                    throw new ProxyException("Parsed request is not of type Document or Element!");
                }
            }
            addSOAPAction(request);
            for (Iterator iter = getRequestInterceptors(); iter.hasNext(); ) {
                Interceptor ic = (Interceptor) iter.next();
                ic.intercept(request, requestVal);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(">>> SOAP request");
                LOG.debug(request);
            }
            SOAPTransport transport = getTransport();
            transport.setTimeout(getTimeout());
            try {
                ContainerUtil.start(_epLocator);
                transport.send(request, _epLocator);
                ContainerUtil.stop(_epLocator);
            } catch (TransportException e) {
                throw new ProxyException("Caught transport exception during send!", e);
            } catch (Exception e) {
                throw new ProxyException("Caught locator exception during send!", e);
            }
            SOAPMessage response = null;
            try {
                response = transport.receive();
            } catch (TransportException e) {
                throw new ProxyException("Caught transport exception during receive!", e);
            }
            if (response.isFault()) {
                SOAPFault fault = null;
                try {
                    fault = new SOAPFault(response.getBodyElement());
                } catch (SOAPException e) {
                    throw new ProxyException("Caught client-side SOAP exception while constructing SOAPFault!", e);
                }
                for (Iterator iter = getResponseInterceptors(); iter.hasNext(); ) {
                    Interceptor ic = (Interceptor) iter.next();
                    ic.intercept(fault);
                }
                Exception ex = fault.toException();
                if (ex == null) {
                    ex = createException(method, fault);
                    if ((ex != null) && (ex instanceof SOAPFaultException)) {
                        throw (SOAPFaultException) ex;
                    }
                } else if (ex instanceof SOAPFaultException) {
                    throw (SOAPFaultException) ex;
                } else {
                    LOG.error(ex);
                }
                throw new SOAPFaultException(fault);
            }
            Object returnVal = null;
            if (method.getReturnType().equals(Void.TYPE)) {
                returnVal = null;
            } else if (method.getReturnType().equals(DataHandler.class)) {
                if (response.getAttachmentCount() > 0) {
                    try {
                        returnVal = response.getAttachmentAt(0);
                    } catch (IOException e) {
                        throw new ProxyException("Error getting attachment from response!", e);
                    }
                }
            } else {
                Unmarshaller um = getUnmarshaller();
                if (um == null) {
                    um = Unmarshaller.Factory.newInstance();
                }
                if (response.getBodyElement() != null) {
                    returnVal = um.unmarshall(response.getBodyElement(), method.getReturnType());
                }
            }
            for (Iterator iter = getResponseInterceptors(); iter.hasNext(); ) {
                Interceptor ic = (Interceptor) iter.next();
                ic.intercept(response, returnVal);
            }
            return returnVal;
        }
