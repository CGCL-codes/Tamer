    @POST
    @Path("/channel/{OwnerID}/{PageGroupID}/{PageID}/{ChannelID}/reorder")
    public Response channelReorder(@PathParam("OwnerID") final String ownerID, @PathParam("PageGroupID") final String pageGroupID, @PathParam("PageID") final String pageID, @PathParam("ChannelID") String channelID, @FormParam("channel_group_id") String newChannelGroupID, @FormParam("relative_channel_id") String relativeChannelID, @FormParam("preceding_relative_channel") @DefaultValue("false") boolean precedingRelativeChannel, @Context final UriInfo uriInfo, @Context final SecurityContext securityContext, @Context final HttpHeaders httpHeaders) throws WWWeeePortal.Exception, WebApplicationException {
        try {
            final String clientUserLogin = (securityContext.getUserPrincipal() != null) ? securityContext.getUserPrincipal().getName() : null;
            final String[] clientUserRoles = getClientUserRoles(wwweeeDB, securityContext);
            final ContentManager.ChannelSpecification<?> reqDBChannelSpecification = getRequestedChannel(ownerID, pageGroupID, pageID, channelID, clientUserLogin, clientUserRoles);
            validateRequestAccess(reqDBChannelSpecification.getContentContainer(), securityContext);
            final ContentManager.ChannelSpecification<?> relativeToChannel = reqDBChannelSpecification.getParentDefinition().getParentDefinition().getChannelSpecification(relativeChannelID);
            final ContentManager.ChannelGroupDefinition newChannelGroup = (relativeToChannel != null) ? relativeToChannel.getParentDefinition() : reqDBChannelSpecification.getParentDefinition().getParentDefinition().getChildDefinition(newChannelGroupID);
            wwweeeDB.updatePositionOfChannelSpecification(reqDBChannelSpecification, newChannelGroup, relativeToChannel, precedingRelativeChannel, true);
            wwweeeDB.deleteEmptyChannelGroups(reqDBChannelSpecification.getParentDefinition().getParentDefinition());
            final Map<String, Object> entityMap = new HashMap<String, Object>();
            return Response.ok().entity(new JSONObject(entityMap)).build();
        } catch (WebApplicationException wae) {
            throw wae;
        } catch (WWWeeePortal.Exception wpe) {
            throw LogAnnotation.log(getLogger(), LogAnnotation.annotate(LogAnnotation.annotate(wpe, "SecurityContext", securityContext, null, false), "UriInfo", uriInfo, null, false), getClass(), wpe);
        } catch (Exception e) {
            throw LogAnnotation.log(getLogger(), LogAnnotation.annotate(LogAnnotation.annotate(new WWWeeePortal.SoftwareException(e), "SecurityContext", securityContext, null, false), "UriInfo", uriInfo, null, false), getClass(), e);
        }
    }
