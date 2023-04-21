    @Override
    protected void generateFiles(IProgressMonitor monitor) throws CoreException {
        super.generateFiles(monitor);
        String pathToWsdl = page.getPathToWSDL();
        try {
            monitor.setTaskName("PluginContentWsdlGenerationOperation");
            IFolder srcFolder = getSourceFolder(monitor);
            PluginContentWsdlGenerationOperation generationOperation = new PluginContentWsdlGenerationOperation(pathToWsdl, project, srcFolder, false);
            generationOperation.run(monitor);
            List<String> warnings = generationOperation.getWarnings();
            if (!warnings.isEmpty()) {
                StringBuilder message = new StringBuilder();
                for (String warning : warnings) {
                    message.append(warning).append("\n");
                }
                ErrorDialog.openError(page.getShell(), null, null, new Status(IStatus.WARNING, Activator.getDefault().getPluginId(), message.toString()));
            }
            Document document = null;
            final URL url = new URL("file", null, pathToWsdl);
            InputStream stream = url.openStream();
            if (stream != null) {
                try {
                    document = DOMUtils.readXml(stream);
                } finally {
                    stream.close();
                }
            }
            String serviceURL = "http://localhost:8197/" + generationOperation.getServiceName() + "/";
            List<Element> elementList = DOMUtils.findAllElementsByTagNameNS(document.getDocumentElement(), "http://schemas.xmlsoap.org/wsdl/", "port");
            for (Element el : elementList) {
                Element soapAddress = DOMUtils.findAllElementsByTagNameNS(el, "http://schemas.xmlsoap.org/wsdl/soap/", "address").iterator().next();
                serviceURL = soapAddress.getAttribute("location");
            }
            monitor.setTaskName("CxfEndpointGenerationJob");
            IPath path = project.getFullPath().append(CXF_ENDPOINT_FILENAME);
            CxfEndpointGenerationJob cxfEndpointJob = new CxfEndpointGenerationJob(generationOperation.getServiceName(), generationOperation.getNameSpace(), generationOperation.getImplementorName(), serviceURL, path);
            IStatus result = cxfEndpointJob.runInWorkspace(monitor);
            if (!result.isOK()) {
                throw new CoreException(result);
            }
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            IStatus status = ErrorUtil.getErrorStatus(Messages.ERROR_JAXWS_PROVIDER_GENERATION, cause);
            throw new CoreException(status);
        } catch (InterruptedException e) {
        } catch (SAXException e) {
            Throwable cause = e.getCause();
            IStatus status = ErrorUtil.getErrorStatus(Messages.ERROR_JAXWS_PROVIDER_GENERATION, cause);
            throw new CoreException(status);
        } catch (ParserConfigurationException e) {
            Throwable cause = e.getCause();
            IStatus status = ErrorUtil.getErrorStatus(Messages.ERROR_JAXWS_PROVIDER_GENERATION, cause);
            throw new CoreException(status);
        } catch (IOException e) {
            Throwable cause = e.getCause();
            IStatus status = ErrorUtil.getErrorStatus(Messages.ERROR_JAXWS_PROVIDER_GENERATION, cause);
            throw new CoreException(status);
        }
    }
