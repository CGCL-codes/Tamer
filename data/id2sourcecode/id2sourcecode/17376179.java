    protected void doConfiguredRequest(Melati melati) throws ServletException, IOException {
        String method = melati.getMethod();
        if (method != null) {
            if (method.equals("Upload")) {
                Hashtable fields = null;
                try {
                    InputStream in = melati.getRequest().getInputStream();
                    MultipartDataDecoder decoder = new MultipartDataDecoder(melati, in, melati.getRequest().getContentType(), melati.getConfig().getFormDataAdaptorFactory());
                    fields = decoder.parseData();
                } catch (IOException e) {
                    melati.getWriter().write("There was some error uploading your file:" + ExceptionUtils.stackTrace(e));
                    return;
                }
                MultipartFormField field = (MultipartFormField) fields.get("file");
                if (field == null) {
                    melati.getWriter().write("No file was uploaded");
                    return;
                }
                byte[] data = field.getData();
                melati.getResponse().setContentType(field.getContentType());
                OutputStream output = melati.getResponse().getOutputStream();
                output.write(data);
                output.close();
                return;
            }
        }
        MelatiConfig config = melati.getConfig();
        melati.setResponseContentType("text/html");
        MelatiWriter output = melati.getWriter();
        output.write("<html><head><title>ConfigServlet Test</title></head>\n");
        output.write("<body><h2>ConfigServlet Test</h2>");
        output.write("<p>This servlet tests your basic melati " + "configuration. <br>\n" + "If you can read this message, it means that you have " + "successfully created a Melati using the configuration " + "given in org.melati.MelatiServlet.properties.<br>\n" + "Please note that this " + "servlet does not construct a POEM session or initialise a template " + "engine.</p>\n");
        output.write("<h4>Your Melati is configured with the following properties: " + "</h4>\n<table>");
        output.write("<tr><td>AccessHandler</td><td>" + config.getAccessHandler().getClass().getName() + "</td></tr>\n");
        output.write("<tr><td>TemplateEngine</td><td>" + config.getTemplateEngine().getClass().getName() + "</td></tr>\n");
        output.write("<tr><td>StaticURL</td><td>" + config.getStaticURL() + "</td></tr>\n");
        output.write("<tr><td>JavascriptLibraryURL</td><td>" + config.getJavascriptLibraryURL() + "</td></tr>\n");
        output.write("<tr><td>FormDataAdaptorFactory</td><td>" + config.getFormDataAdaptorFactory().getClass().getName() + "</td></tr>\n");
        output.write("<tr><td>Locale</td><td>" + config.getLocale().getClass().getName() + "</td></tr>\n");
        output.write("<tr><td>TempletLoader</td><td>" + config.getTempletLoader().getClass().getName() + "</td></tr>\n");
        output.write("</table>" + "<h4>This servlet was called with the following Method (taken from " + "melati.getMethod()): " + melati.getMethod() + "</h4>\n");
        output.write("<h4>Further Testing:</h4>\n" + "You can test melati Exception handling by " + "clicking <a href=" + melati.getSameURL() + "/Exception>Exception</a><br>You can test melati Redirect " + "handling by clicking <a href=" + melati.getSameURL() + "/Redirect>Redirect</a><br>You can test your " + "POEM setup (connecting to logical database <tt>melatitest</tt>) by " + "clicking <a href=" + melati.getZoneURL() + "/org.melati.test.PoemServletTest/melatitest/>" + "org.melati.test.PoemServletTest/melatitest/</a><br>" + "<form method=\"post\" action=\"" + melati.getSameURL() + "/Upload\" enctype=\"multipart/form-data\" target=_blank>" + "You can upload a file here:<br>" + "<input type=hidden name=upload value=yes>" + "<input type=\"file\" name=\"file\" enctype=\"multipart/form-data\">" + "<input type=\"submit\" name=\"Submit\" value=\"Upload file\"><br>" + getUploadMessage(melati) + "</form>");
        if (method != null) {
            if (method.equals("Exception")) throw new MelatiBugMelatiException("It got caught!");
            if (method.equals("Redirect")) {
                melati.getResponse().sendRedirect("http://www.melati.org");
                return;
            }
        }
    }
