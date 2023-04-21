    @Override
    protected void generateFiles(IProgressMonitor monitor) throws CoreException {
        super.generateFiles(monitor);
        String pathToWsdl = page.getPathToWSDL();
        try {
            monitor.setTaskName("PluginContentWsdlGenerationOperation");
            IFolder srcFolder = getSourceFolder(monitor);
            PluginContentWsdlGenerationOperation generationOperation = new PluginContentWsdlGenerationOperation(pathToWsdl, project, srcFolder, true);
            generationOperation.run(monitor);
            List<String> warnings = generationOperation.getWarnings();
            if (!warnings.isEmpty()) {
                StringBuilder message = new StringBuilder();
                for (String warning : warnings) {
                    message.append(warning).append("\n");
                }
                ErrorDialog.openError(page.getShell(), null, null, new Status(IStatus.WARNING, Activator.getDefault().getPluginId(), message.toString()));
            }
            int index = generationOperation.getImplementorName().lastIndexOf('.');
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
            monitor.setTaskName("CxfEndpointConsumerGenerationJob");
            IPath pathCXF = project.getFullPath().append(CXF_ENDPOINT_FILENAME);
            IPath pathClientInvoker = project.getFullPath().append("/src/").append(generationOperation.getImplementorName().substring(0, index).replace(".", "/")).append("/").append("sample").append("/").append(generationOperation.getServiceName() + "ClientInvoker.java");
            CxfEndpointConsumerGenerationJob cxfEndpointJob = new CxfEndpointConsumerGenerationJob(generationOperation.getServiceName(), generationOperation.getNameSpace(), generationOperation.getImplementorName(), serviceURL, pathCXF, pathClientInvoker, page.doGenerateStaticEndpoint(), page.doGenerateExampleCode());
            IStatus result = cxfEndpointJob.runInWorkspace(monitor);
            if (!result.isOK()) {
                throw new CoreException(result);
            }
            IPath pathPackageInfo = project.getLocation().append("/src/").append(generationOperation.getImplementorName().substring(0, index).replace(".", "/")).append("/").append("package-info.java");
            pathPackageInfo.toFile().delete();
            IPath pathClient = project.getLocation().append("/src/").append(generationOperation.getImplementorName().substring(0, index).replace(".", "/"));
            File[] files = pathClient.toFile().listFiles();
            for (File javaFile : files) {
                if (javaFile.getName().endsWith("_Client.java")) {
                    javaFile.delete();
                }
            }
            project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            IStatus status = ErrorUtil.getErrorStatus(Messages.ERROR_JAXWS_CONSUMER_GENERATION, cause);
            throw new CoreException(status);
        } catch (InterruptedException e) {
        } catch (SAXException e) {
            Throwable cause = e.getCause();
            IStatus status = ErrorUtil.getErrorStatus(Messages.ERROR_JAXWS_CONSUMER_GENERATION, cause);
            throw new CoreException(status);
        } catch (ParserConfigurationException e) {
            Throwable cause = e.getCause();
            IStatus status = ErrorUtil.getErrorStatus(Messages.ERROR_JAXWS_CONSUMER_GENERATION, cause);
            throw new CoreException(status);
        } catch (IOException e) {
            Throwable cause = e.getCause();
            IStatus status = ErrorUtil.getErrorStatus(Messages.ERROR_JAXWS_CONSUMER_GENERATION, cause);
            throw new CoreException(status);
        }
    }
