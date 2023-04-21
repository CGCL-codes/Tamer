    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding(Constants.CHARACTER_ENCODING_UTF8);
        response.setContentType(Constants.MIME_APPLICATION_XML);
        String acceptHeader = request.getHeader(Constants.HTTP_ACCEPT_HEADER);
        String url = request.getRequestURL().toString();
        String regexp = "rest\\/status\\/(.*)";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(url);
        String unParsedSensorIDs = null;
        PrintWriter printWriter = response.getWriter();
        if (matcher.find()) {
            unParsedSensorIDs = matcher.group(1);
            try {
                if (unParsedSensorIDs != null && !"".equals(unParsedSensorIDs)) {
                    printWriter.write(JSONTranslator.translateXMLToJSON(acceptHeader, response, statusCommandService.readFromCache(unParsedSensorIDs)));
                }
            } catch (ControllerException e) {
                logger.error("CommandException occurs", e);
                printWriter.print(JSONTranslator.translateXMLToJSON(acceptHeader, response, e.getErrorCode(), RESTAPI.composeXMLErrorDocument(e.getErrorCode(), e.getMessage())));
            }
        } else {
            printWriter.print(JSONTranslator.translateXMLToJSON(acceptHeader, response, 400, RESTAPI.composeXMLErrorDocument(400, "Bad REST Request, should be /rest/status/{sensor_id},{sensor_id}...")));
        }
        printWriter.flush();
    }
