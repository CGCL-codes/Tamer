    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (validator == null) {
            try {
                Set validatorSet = new HashSet();
                validatorSet.add(new JARVSchemaValidator(Configuration.getInstance().getProjectProperty("schema.namespace")));
                validatorSet.add(new SchematronSaxonTransformer(Configuration.getInstance().getSchematronTransformerURL(), Configuration.getInstance().getSchemaExtractorURL()));
                validator = new ValidatorCollector(validatorSet);
            } catch (Exception e) {
                new ServletException(e);
            }
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(validatorPage);
        RequestDispatcher dispatcherXML = request.getRequestDispatcher(validatorPageXML);
        String option = null;
        option = request.getParameter(RequestConst.OPTION_PARAMETER);
        if (option == null) {
            if (option == null) {
                Collection options = (Collection) request.getAttribute(RequestConst.OPTION_LIST_ATTRIBUTE);
                if (!options.iterator().hasNext()) option = "xhtml"; else option = ((OptionBean) options.iterator().next()).getName();
            }
        }
        InputStream in = null;
        if (request instanceof MultipartWrapper) {
            try {
                File file = ((MultipartWrapper) request).getFile(RequestConst.FILE_PARAMETER);
                if (file != null) {
                    in = new FileInputStream(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute(RequestConst.MESSAGE_ATTRIBUTE, "   Unable to handle the uploaded file.");
                dispatcher.forward(request, response);
                return;
            }
        }
        if (in == null) {
            String urlString = request.getParameter(RequestConst.URL_PARAMETER);
            if (RequestConst.REFERER_PARAMETER_VALUE.equals(urlString)) urlString = request.getHeader("referer");
            URL url = null;
            try {
                url = new URL(urlString);
                if (url.getProtocol().toLowerCase().equals("file")) throw new IOException("Protocol 'file' is not allowed.");
            } catch (IOException e) {
                request.setAttribute(RequestConst.MESSAGE_ATTRIBUTE, "Unable to validate '" + urlString + "': " + e.getMessage());
                dispatcher.forward(request, response);
                return;
            }
            try {
                URLConnection connection = url.openConnection();
                connection.setAllowUserInteraction(false);
                in = connection.getInputStream();
            } catch (Exception e) {
                request.setAttribute(RequestConst.MESSAGE_ATTRIBUTE, "   Unable to connect to " + urlString);
                dispatcher.forward(request, response);
                return;
            }
        }
        CollectorErrorHandler errorHandler = new CollectorErrorHandler(SeverityLevel.INFO);
        try {
            FilterProperties filterProperties = new FilterProperties();
            filterProperties.load(request);
            Map source = NumberedResultFactory.create(validator.validate(option, in, filterProperties, errorHandler));
            request.setAttribute(RequestConst.SOURCE_CODE, source);
        } catch (Exception e) {
            e.printStackTrace();
            errorHandler.reportMessage(e.getClass() + " " + e.getMessage(), SeverityLevel.FATAL_ERROR);
        }
        request.setAttribute(RequestConst.RESULT_ATTRIBUTE, errorHandler.getResult());
        if (errorHandler.isValid()) {
            request.setAttribute(RequestConst.MESSAGE_ATTRIBUTE, "Congratulation, your document is valid. Relax.");
        } else {
            request.setAttribute(RequestConst.MESSAGE_ATTRIBUTE, "Your document is invalid.");
        }
        if (request.getParameter(RequestConst.XML_OUTPUT) != null) dispatcherXML.forward(request, response); else dispatcher.forward(request, response);
    }
