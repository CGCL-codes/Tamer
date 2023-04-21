    public void handleEvent(Event event) {
        if (event instanceof EntryAddedEvent || event instanceof EntryUpdatedEvent) {
            EntryEvent entryEvent = (EntryEvent) event;
            String text = entryEvent.getEntry().getDescription();
            if (!BlojsomUtils.checkNullOrBlank(text) && BlojsomUtils.checkMapForKey(entryEvent.getEntry().getMetaData(), PINGBACK_PLUGIN_METADATA_SEND_PINGBACKS)) {
                String pingbackURL;
                StringBuffer sourceURI = new StringBuffer().append(entryEvent.getBlog().getBlogURL()).append(entryEvent.getEntry().getBlogCategory().getName()).append(entryEvent.getEntry().getPostSlug());
                String targetURI;
                Pattern hrefPattern = Pattern.compile(HREF_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.DOTALL);
                Matcher hrefMatcher = hrefPattern.matcher(text);
                if (_logger.isDebugEnabled()) {
                    _logger.debug("Checking for href's in entry: " + entryEvent.getEntry().getId());
                }
                while (hrefMatcher.find()) {
                    targetURI = hrefMatcher.group(1);
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("Found potential targetURI: " + targetURI);
                    }
                    try {
                        HttpURLConnection urlConnection = (HttpURLConnection) new URL(targetURI).openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();
                        pingbackURL = urlConnection.getHeaderField(X_PINGBACK_HEADER);
                        if (pingbackURL == null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), BlojsomConstants.UTF8));
                            StringBuffer content = new StringBuffer();
                            String input;
                            while ((input = bufferedReader.readLine()) != null) {
                                content.append(input).append(BlojsomConstants.LINE_SEPARATOR);
                            }
                            bufferedReader.close();
                            Pattern pingbackLinkPattern = Pattern.compile(PINGBACK_LINK_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.DOTALL);
                            Matcher pingbackLinkMatcher = pingbackLinkPattern.matcher(content.toString());
                            if (pingbackLinkMatcher.find()) {
                                pingbackURL = pingbackLinkMatcher.group(1);
                            }
                        }
                        if (pingbackURL != null && targetURI != null) {
                            Vector parameters = new Vector();
                            parameters.add(sourceURI.toString());
                            parameters.add(targetURI);
                            try {
                                if (_logger.isDebugEnabled()) {
                                    _logger.debug("Sending pingback to: " + pingbackURL + " sourceURI: " + sourceURI + " targetURI: " + targetURI);
                                }
                                XmlRpcClient xmlRpcClient = new XmlRpcClient(pingbackURL);
                                xmlRpcClient.executeAsync(PINGBACK_METHOD, parameters, _callbackHandler);
                            } catch (MalformedURLException e) {
                                if (_logger.isErrorEnabled()) {
                                    _logger.error(e);
                                }
                            }
                        }
                    } catch (IOException e) {
                        if (_logger.isErrorEnabled()) {
                            _logger.error(e);
                        }
                    }
                }
            } else {
                if (_logger.isDebugEnabled()) {
                    _logger.debug("No text in blog entry or " + PINGBACK_PLUGIN_METADATA_SEND_PINGBACKS + " not enabled.");
                }
            }
        } else if (event instanceof PingbackAddedEvent) {
            HtmlEmail email = new HtmlEmail();
            PingbackAddedEvent pingbackAddedEvent = (PingbackAddedEvent) event;
            if (pingbackAddedEvent.getBlog().getBlogEmailEnabled().booleanValue()) {
                try {
                    setupEmail(pingbackAddedEvent.getBlog(), pingbackAddedEvent.getEntry(), email);
                    Map emailTemplateContext = new HashMap();
                    emailTemplateContext.put(BlojsomConstants.BLOJSOM_BLOG, pingbackAddedEvent.getBlog());
                    emailTemplateContext.put(BLOJSOM_PINGBACK_PLUGIN_PINGBACK, pingbackAddedEvent.getPingback());
                    emailTemplateContext.put(BLOJSOM_PINGBACK_PLUGIN_BLOG_ENTRY, pingbackAddedEvent.getEntry());
                    String htmlText = mergeTemplate(PINGBACK_PLUGIN_EMAIL_TEMPLATE_HTML, pingbackAddedEvent.getBlog(), emailTemplateContext);
                    String plainText = mergeTemplate(PINGBACK_PLUGIN_EMAIL_TEMPLATE_TEXT, pingbackAddedEvent.getBlog(), emailTemplateContext);
                    email.setHtmlMsg(htmlText);
                    email.setTextMsg(plainText);
                    String emailPrefix = (String) pingbackAddedEvent.getBlog().getProperties().get(PINGBACK_PREFIX_IP);
                    if (BlojsomUtils.checkNullOrBlank(emailPrefix)) {
                        emailPrefix = DEFAULT_PINGBACK_PREFIX;
                    }
                    email = (HtmlEmail) email.setSubject(emailPrefix + pingbackAddedEvent.getEntry().getTitle());
                    email.send();
                } catch (EmailException e) {
                    if (_logger.isErrorEnabled()) {
                        _logger.error(e);
                    }
                }
            }
        }
    }
