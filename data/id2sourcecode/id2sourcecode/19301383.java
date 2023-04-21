    final Response doViewRequest(final javax.ws.rs.core.Request rsRequest, final UriInfo uriInfo, final SecurityContext securityContext, final HttpHeaders httpHeaders, final Map<String, Object> requestAttributes, final Map<String, Object> sessionAttributes, final DataSource entity, final ContentManager.ChannelSpecification.Key maximizedChannelKey) throws WWWeeePortal.Exception, WebApplicationException {
        activeRequestLock.readLock().lock();
        try {
            if (!initialized) throw new IllegalStateException("Attempt to have uninitialized Page handle a request");
            final List<Map.Entry<ContentManager.ChannelSpecification<?>, FutureTask<Channel.ViewResponse>>> channelViewTasks = new ArrayList<Map.Entry<ContentManager.ChannelSpecification<?>, FutureTask<Channel.ViewResponse>>>(CollectionUtil.size(channels));
            final Request request = new Request(new ConcurrentRequestWrapper(rsRequest), new ConcurrentUriInfoWrapper(uriInfo), new ConcurrentSecurityContextWrapper(securityContext), new ConcurrentHttpHeadersWrapper(httpHeaders), requestAttributes, sessionAttributes, entity, this, maximizedChannelKey, channelViewTasks);
            final Response noAccessResponse = request.checkAccess();
            if (noAccessResponse != null) {
                return noAccessResponse;
            }
            final TransformableDocument responseDocument = createResponseDocument(request);
            final Channel maximizedChannel = getChannel(maximizedChannelKey);
            final ContentManager.ChannelSpecification<?> maximizedChannelSpecification = definition.getChannelSpecification(maximizedChannelKey);
            final ContentManager.ChannelGroupDefinition maximizedChannelGroup = (maximizedChannelSpecification != null) ? maximizedChannelSpecification.getParentDefinition() : null;
            final int priorityNeededToView = (maximizedChannel != null) ? maximizedChannel.getMaximizationMaximizedPriority(request) : 0;
            for (ContentManager.ChannelGroupDefinition channelGroup : CollectionUtil.mkNotNull(definition.getMatchingChildDefinitions(null, null, null, true, securityContext, httpHeaders, -1, false, true))) {
                if (channelGroup == null) continue;
                final Element channelGroupElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, HTMLUtil.HTML_NS_PREFIX, "div", DOMUtil.getChildElement(responseDocument.getDocument().getDocumentElement(), HTMLUtil.HTML_NS_URI, "body"));
                DOMUtil.createAttribute(null, null, "id", ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portal.getPortalID(), "page", "group", channelGroup.getID())), channelGroupElement);
                final StringBuffer channelGroupClassBuffer = new StringBuffer();
                channelGroupClassBuffer.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portal.getPortalID(), "page", "group")));
                if (channelGroup.equals(maximizedChannelGroup)) {
                    channelGroupClassBuffer.append(' ');
                    channelGroupClassBuffer.append(ConversionUtil.invokeConverter(MarkupManager.MARKUP_ENCODE_CONVERTER, Arrays.asList(portal.getPortalID(), "page", "group", "maximized")));
                }
                for (String classAttr : ConfigManager.getConfigProps(channelGroup.getProperties(), CHANNEL_GROUP_CLASS_BY_NUM_PATTERN, request.getSecurityContext(), request.getHttpHeaders(), RSProperties.RESULT_STRING_CONVERTER, false, false).values()) {
                    channelGroupClassBuffer.append(' ');
                    channelGroupClassBuffer.append(classAttr);
                }
                DOMUtil.createAttribute(null, null, "class", channelGroupClassBuffer.toString(), channelGroupElement);
                for (ContentManager.ChannelSpecification<?> channelSpecification : CollectionUtil.mkNotNull(channelGroup.getMatchingChildDefinitions(null, null, null, true, securityContext, httpHeaders, -1, false, true))) {
                    if (channelSpecification == null) continue;
                    final Channel channel = getChannel(channelSpecification.getID());
                    if (channel == null) continue;
                    if ((maximizedChannelKey != null) && (!maximizedChannelKey.equals(channelSpecification.getKey()))) {
                        final int channelPriority = channel.getMaximizationNormalPriority(request);
                        if (channelPriority < priorityNeededToView) {
                            continue;
                        }
                    }
                    final RSAccessControl channelAC = channel.getDefinition().getAccessControl();
                    if ((channelAC != null) && (!channelAC.hasAccess(securityContext))) {
                        continue;
                    }
                    final FutureTask<Channel.ViewResponse> channelViewTask = new FutureTaskGC<Channel.ViewResponse>(new Callable<Channel.ViewResponse>() {

                        @Override
                        public Channel.ViewResponse call() throws Exception {
                            return channel.doViewRequest(request, channelGroupElement);
                        }
                    });
                    channelViewTasks.add(new AbstractMap.SimpleImmutableEntry<ContentManager.ChannelSpecification<?>, FutureTask<Channel.ViewResponse>>(channelSpecification, channelViewTask));
                }
            }
            final ExecutorService executorService = portal.getExecutorService();
            if (executorService != null) {
                for (Map.Entry<ContentManager.ChannelSpecification<?>, FutureTask<Channel.ViewResponse>> channelViewTask : channelViewTasks) {
                    executorService.submit(channelViewTask.getValue());
                }
            }
            try {
                for (Map.Entry<ContentManager.ChannelSpecification<?>, FutureTask<Channel.ViewResponse>> channelViewTask : channelViewTasks) {
                    final Channel.ViewResponse channelViewResponse = getChannelViewResponse(channelViewTask.getValue());
                    final Node channelResponseContainerNode = channelViewResponse.getResponseContainerNode();
                    final Element channelResponseRootElement = channelViewResponse.getResponseRootElement();
                    channelResponseContainerNode.appendChild(channelResponseRootElement);
                }
            } catch (Exception e) {
                for (Map.Entry<ContentManager.ChannelSpecification<?>, FutureTask<Channel.ViewResponse>> channelViewTask : channelViewTasks) {
                    channelViewTask.getValue().cancel(true);
                }
                throw e;
            }
            finalizeResponseDocument(request, responseDocument);
            return buildResponse(request, responseDocument);
        } catch (WebApplicationException wae) {
            throw wae;
        } catch (WWWeeePortal.Exception wpe) {
            throw LogAnnotation.log(getLogger(), LogAnnotation.annotate(LogAnnotation.annotate(LogAnnotation.annotate(wpe, "SecurityContext", securityContext, null, false), "UriInfo", uriInfo, null, false), "Page", this, null, false), getClass(), wpe);
        } catch (Exception e) {
            throw LogAnnotation.log(getLogger(), LogAnnotation.annotate(LogAnnotation.annotate(LogAnnotation.annotate(new WWWeeePortal.SoftwareException(e), "SecurityContext", securityContext, null, false), "UriInfo", uriInfo, null, false), "Page", this, null, false), getClass(), e);
        } finally {
            activeRequestLock.readLock().unlock();
        }
    }
