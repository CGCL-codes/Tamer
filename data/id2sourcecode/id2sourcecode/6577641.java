    public N3Source addSource(URL url, Project project) throws IOException, URISyntaxException {
        N3Source n3 = null;
        if (URLHelper.isLocal(url) && !(new File(url.toURI())).exists()) {
            return new N3Source(url);
        }
        final URLConnection openConnection = makeN3URLConnection(url);
        final String contentType = openConnection.getContentType();
        final InputStream is = openConnection.getInputStream();
        final BufferedInputStream bis = new BufferedInputStream(is);
        final BufferedReader br = new BufferedReader(new InputStreamReader(bis, "UTF8"));
        String firstLine = br.readLine();
        Logger.getLogger("theDefault").info(url.toString() + " : contentType: \"" + contentType + "\"" + " first line: \"" + firstLine + "\"");
        final String file = url.getFile();
        if (file.endsWith(".n3") || file.endsWith(".ttl") || file.endsWith(".nt") || (contentType == null && !looksLikeXMLStart(firstLine))) {
            n3 = new N3Source(url);
        } else if (contentType != null && (contentType.contains("n3") || (contentType.equals("text/plain") && !looksLikeXMLStart(firstLine)))) {
            n3 = new N3Source(url);
        } else if (isXMIURL(file)) {
            n3 = new N3SourceFromXMI(url.toURI(), project);
            n3.prepare(project);
        } else if (contentType != null && contentType.contains("application/rdf+xml") || file.endsWith(".rdf") || file.endsWith(".rdfs")) {
            n3 = new N3SourceFromRDF(url, project);
        } else {
            final FormatRecognizer recognizer = new FormatRecognizer(url);
            if (recognizer.isRDF()) {
                n3 = new N3SourceFromRDF(url, project);
            } else if (recognizer.isOWLXML()) {
                n3 = new N3SourceFromOWL(url, project);
            } else if (recognizer.isPlainXML()) {
                final N3Source n3Gloze = new N3SourceFromXML_Gloze();
                n3Gloze.setURI(url.toString());
                n3Gloze.prepare(project);
                n3 = n3Gloze;
            }
        }
        if (project != null) {
            project.addN3Source(n3);
        }
        return n3;
    }
