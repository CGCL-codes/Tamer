    public void generateGegFile(SSOSubject authenticated, String fromName, GLinkURL fromURL, String fromRelValue, String toName, GLinkURL toURL, String toRelValue, GLinkAction action, HttpServletRequest request, boolean doingToURL) throws IOException, InterruptedException {
        LOG.finest("Generating a GEG!");
        LOG.finest("The individualId is: " + fromURL.getIndividualId());
        String glinkXMLURL = fromURL.getGLinkXMLURL();
        LOG.finer("Generating a GEG for: " + glinkXMLURL);
        if (doingToURL) {
            request.setAttribute("toGLinkURL", glinkXMLURL);
        } else {
            request.setAttribute("fromGLinkURL", glinkXMLURL);
        }
        URL url = new URL(glinkXMLURL);
        URLConnection uc = url.openConnection();
        InputStream in = null;
        try {
            in = uc.getInputStream();
        } catch (Exception ex) {
            if (LOG.isLoggable(Level.FINEST)) {
                StringWriter exWriter = new StringWriter();
                PrintWriter writer = new PrintWriter(exWriter);
                ex.printStackTrace(writer);
                exWriter.close();
                writer.close();
                LOG.finest("generateGegFile Connect exception: " + exWriter.toString());
            }
            String newGLinkXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<g:glinks xmlns:g=\"http://www.gedcombrowser.org/gedapi/glinks-schema\"" + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.0\"" + " xsi:schemaLocation=\"http://www.gedcombrowser.org/gedapi/glinks-schema" + " http://www.gedcombrowser.org/gedapi/glinks-schema/glinks_1_0.xsd\">" + "<g:individual>" + fromURL + "</g:individual>" + "</g:glinks>";
            in = new ByteArrayInputStream(newGLinkXML.getBytes());
        }
        File generatedGegFile = fromURL.getGeneratedGegFile(request.getSession(true).getId());
        LOG.finest("Generating GEG into file: " + generatedGegFile + " while reading from file: " + glinkXMLURL);
        SniffedInputStream inStream = new SniffedInputStream(in);
        Charset charset = inStream.getCharset();
        if (inStream.getWarning() != null) {
            LOG.warning(inStream.getWarning());
        }
        String charsetName = EnvironmentChecker.getProperty(this, "genj.gedcom.charset", null, "checking for forced charset for read of inputStream");
        if (charsetName != null) {
            try {
                charset = Charset.forName(charsetName);
            } catch (Throwable t) {
                LOG.log(Level.WARNING, "Can't force charset " + charset, t);
            }
        }
        InputStreamReader inReader = new InputStreamReader(inStream, charset);
        generatedGegFile.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(generatedGegFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        OutputStreamWriter out = new OutputStreamWriter(bos, charset);
        GLinkPattern pattern = new GLinkPattern("</g:glinks>");
        pattern.setCompareSize(pattern.getToMatch().length());
        pattern.setNumberOfMatches(1);
        GegPullHandler handler = new GegPullHandler();
        handler.setInsertDelta(-(pattern.getToMatch().length()) - 5);
        handler.setToName(toName);
        handler.setToURL(toURL);
        handler.setToRelation(toRelValue);
        handler.setFromRelation(fromRelValue);
        handler.setOutWriter(out);
        handler.setAuthenticated(authenticated);
        GLinkPullParser pullParser = new GLinkPullParser();
        pullParser.setReader(inReader);
        pullParser.addPullHander(pattern, handler);
        pullParser.parse();
        inStream.close();
        inReader.close();
        in.close();
        out.flush();
        out.close();
        bos.flush();
        bos.close();
        fos.flush();
        fos.close();
        LOG.finest("The generated GLink xml file is: " + generatedGegFile);
        if (doingToURL) {
            request.setAttribute("toGeneratedGegFile", generatedGegFile);
        } else {
            request.setAttribute("fromGeneratedGegFile", generatedGegFile);
        }
    }
