    public void handleEvent(Event event) {
        if (!BlojsomUtils.checkNullOrBlank(_twitterUpdateURL)) {
            if ((event instanceof EntryAddedEvent) || (event instanceof EntryUpdatedEvent)) {
                EntryEvent entryEvent = (EntryEvent) event;
                Blog blog = entryEvent.getBlog();
                String author = entryEvent.getEntry().getAuthor();
                User user;
                try {
                    user = _fetcher.loadUser(blog, author);
                } catch (FetcherException e) {
                    if (_logger.isErrorEnabled()) {
                        _logger.error("Error loading User object to retrieve Twitter properties", e);
                    }
                    return;
                }
                if (!BlojsomUtils.checkNullOrBlank((String) blog.getProperty(TWITTER_SIGN_IN_IP)) && !BlojsomUtils.checkNullOrBlank((String) blog.getProperty(TWITTER_PASSWORD_IP))) {
                    String signIn = (String) blog.getProperty(TWITTER_SIGN_IN_IP);
                    String password = (String) blog.getProperty(TWITTER_PASSWORD_IP);
                    String updateText = (String) blog.getProperty(TWITTER_STATUS_UPDATE_TEXT_IP);
                    if (BlojsomUtils.checkNullOrBlank(updateText)) {
                        updateText = TWITTER_DEFAULT_STATUS_UPDATE_TEXT;
                    }
                    if (("true".equals(blog.getProperty(TWITTER_UPDATE_ON_ENTRY_ADDED_IP))) || ("true".equals(blog.getProperty(TWITTER_UPDATE_ON_ENTRY_UPDATED_IP)))) {
                        String title = entryEvent.getEntry().getTitle();
                        String twitterUpdate = BlojsomUtils.urlEncode(BlojsomUtils.escapeString(MessageFormat.format(updateText, new Object[] { title })));
                        Authenticator.setDefault(new TwitterAuthenticator(signIn, password));
                        try {
                            URL url = new URL(_twitterUpdateURL);
                            URLConnection urlConnection = url.openConnection();
                            urlConnection.setUseCaches(false);
                            urlConnection.setDoInput(true);
                            urlConnection.setDoOutput(true);
                            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            String twitterData = TWITTER_STATUS_PARAMETER + "=" + twitterUpdate;
                            OutputStreamWriter twitterWriter = new OutputStreamWriter(urlConnection.getOutputStream());
                            twitterWriter.write(twitterData);
                            twitterWriter.flush();
                            twitterWriter.close();
                            BufferedReader twitterReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            StringBuffer twitterReply = new StringBuffer();
                            String input;
                            while ((input = twitterReader.readLine()) != null) {
                                twitterReply.append(input);
                            }
                            twitterReader.close();
                            if (BlojsomUtils.checkNullOrBlank(twitterReply.toString())) {
                                if (_logger.isErrorEnabled()) {
                                    _logger.error("Error communicating update to Twitter");
                                }
                            } else {
                                if (_logger.isDebugEnabled()) {
                                    _logger.debug("Successfully sent update to Twitter");
                                }
                            }
                        } catch (IOException e) {
                            if (_logger.isErrorEnabled()) {
                                _logger.error(e);
                            }
                        }
                    } else {
                        if (_logger.isDebugEnabled()) {
                            _logger.debug("Twitter notification update not enabled for either add or update entry events");
                        }
                    }
                } else {
                    if (_logger.isDebugEnabled()) {
                        _logger.debug("Twitter sign in and/or password is null or blank");
                    }
                }
            }
        }
    }
