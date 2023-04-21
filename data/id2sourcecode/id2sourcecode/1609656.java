    public void doRequest(InternalRequest requestContext, DataSourceProvider dataSourceProvider, RequestInterface requestInterface, ResponseInterface responseInterface, Object renderObject, Event executedEvent) throws Exception {
        while (true) {
            DataSource dataSource = dataSourceProvider.getDataSource(this, requestContext);
            if (dataSource != null) {
                String mimeType = dataSource.getMimeType();
                if (mimeType == null) {
                    mimeType = "text/plain";
                }
                requestContext.setResponseMIMEType(mimeType);
                LOG.debug("Initial mime type: " + requestContext.getResponseMIMEType());
            }
            Request.setCurrent(requestContext);
            try {
                if (executedEvent != null) {
                    Event event = executedEvent;
                    executedEvent = null;
                    throw event;
                }
                if (renderObject == null) {
                    renderObject = pageResolver.peekPageObject(requestContext);
                }
                requestContext.setRequestedPage(renderObject);
                if (website != null) {
                    website.onRequest(requestContext);
                }
                try {
                    if (renderObject != null) {
                        pageResolver.onRequest(requestContext);
                    }
                    if (dataSource != null) {
                        if (renderObject != null) {
                            try {
                                try {
                                    MethodActionLink.callAction(renderObject, requestContext);
                                    ListenerActionLink.callAction(requestContext);
                                } catch (OperationNotAllowedException e) {
                                    Event event = e.getExecutedEvent();
                                    e.printStackTrace();
                                    if (event != null) {
                                        throw event;
                                    }
                                    responseInterface.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    Writer writer = responseInterface.openWriter();
                                    writer.write("The operation was not allowed");
                                    responseInterface.closeWriter(writer);
                                    return;
                                }
                                serveDynamicFile(requestContext, dataSource, renderObject, responseInterface, dataSourceProvider);
                            } catch (Event event) {
                                throw event;
                            } catch (Exception e) {
                                e.printStackTrace();
                                pageResolver.onException(e, requestContext);
                            } finally {
                                pageResolver.onRequestFinished(requestContext);
                            }
                            Event firedEvent = requestContext.getAfterRequestEvent();
                            if (firedEvent != null) {
                                if (firedEvent instanceof RedirectEvent) {
                                    throw (RedirectEvent) firedEvent;
                                }
                                throw firedEvent;
                            }
                        } else {
                            responseInterface.setMimeType(requestContext.getResponseMIMEType());
                            InputStream in = dataSource.openStream();
                            streamOutput(in, responseInterface);
                            in.close();
                        }
                    } else {
                        LOG.info("Could not serve: '" + requestContext.getPath() + "' (not found from server)");
                        responseInterface.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;
                } catch (Event event) {
                    throw event;
                } catch (Exception exception) {
                    if (website != null) {
                        website.onException(exception, requestContext);
                    } else {
                        throw exception;
                    }
                } finally {
                    if (website != null) {
                        website.onRequestFinished(requestContext);
                    }
                }
            } catch (StatusEvent event) {
                responseInterface.setStatus(event.getStatusCode());
                break;
            } catch (RedirectEvent event) {
                if (event instanceof SessionClosedEvent) {
                    pageResolver.onSessionEnd(requestContext);
                    requestContext.getHttpRequest().getSession().invalidate();
                }
                Link redirectPage = event.getRedirectPage();
                String redirectURL = redirectPage.toURL(requestContext);
                String processedURL = redirectURL;
                if (LightBoundUtil.isRelativeURL(processedURL)) {
                    processedURL = responseInterface.encodeInternalRedirectURL(processedURL);
                }
                if (event.isInlineRedirect()) {
                    requestContext = requestInterface.getRedirectRequest(requestContext, redirectURL, this);
                    if (requestContext != null) {
                        if (redirectPage instanceof PageLink) {
                            PageLink pageLink = (PageLink) redirectPage;
                            Class<?> pageClass = pageLink.getPageClass();
                            try {
                                renderObject = pageResolver.getPageObject(pageClass, requestContext);
                                continue;
                            } catch (InstantiationException e) {
                                throw new TranslationException(e);
                            } catch (IllegalAccessException e) {
                                throw new TranslationException(e);
                            } catch (BeanInterfaceException e) {
                                throw new TranslationException(e);
                            }
                        }
                        URL url = new URL(requestContext.getURL(processedURL));
                        LOG.debug("Redirect inlinely to " + url.toExternalForm());
                        InputStream in = url.openStream();
                        try {
                            streamOutput(in, responseInterface);
                        } finally {
                            LightBoundUtil.closeQuietly(in);
                        }
                    }
                } else {
                    responseInterface.sendRedirect(processedURL);
                }
                return;
            } catch (Event event) {
                throw new TranslationException("unrecognised event", event);
            } catch (LinkExpiredException e) {
                responseInterface.setStatus(HttpServletResponse.SC_OK);
                Writer writer = responseInterface.openWriter();
                writer.write("Sorry, but this link has expired.");
                writer.close();
                return;
            } finally {
                Request.removeCurrent();
            }
        }
    }
