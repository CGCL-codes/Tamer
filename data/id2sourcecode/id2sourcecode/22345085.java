    public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
        String content = pageHeader;
        InputStream is = null;
        try {
            Authenticator.setDefault(new SimpleAuthenticator(username, password));
            System.setProperty("http.proxyHost", proxy);
            System.setProperty("http.proxyPort", port);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Parser p = new Parser(connection);
            HasAttributeFilter suppliertableFilter = new HasAttributeFilter();
            suppliertableFilter.setAttributeName("class");
            suppliertableFilter.setAttributeValue("suppliertable");
            HasAttributeFilter linksFilter = new HasAttributeFilter();
            linksFilter.setAttributeName("class");
            linksFilter.setAttributeValue("links");
            NotFilter notFilter = new NotFilter(linksFilter);
            AndFilter andFilter = new AndFilter(suppliertableFilter, notFilter);
            NodeList list = p.parse(andFilter);
            SimpleNodeIterator iterator = list.elements();
            while (iterator.hasMoreNodes()) {
                content += iterator.nextNode().toHtml();
            }
            content = content.replaceAll("/cgi-bin/", "http://www.chmoogle.com/cgi-bin/");
            content += pageFooter;
            result.setContent(content.trim());
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
            }
        }
        return Status.OK_STATUS;
    }
