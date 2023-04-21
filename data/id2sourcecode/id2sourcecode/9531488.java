    protected ContentManager.PageDefinition<?> createPage(final ContentManager.PageGroupDefinition pageGroupDefinition, final FileableCmisObject[] path, final List<Tree<FileableCmisObject>> pageFolderDescendants, final ContentManager.PageDefinition<?> templatePageDefinition, final boolean channelDefinitionsRequired, final SecurityContext securityContext, final HttpHeaders httpHeaders) throws WWWeeePortal.Exception {
        final RSProperties pageProperties = new RSProperties(this, pageGroupDefinition.getProperties());
        pageProperties.setProp(Page.TITLE_TEXT_PROP, path[2].getName(), Locale.ROOT, null);
        addProperties(path[2], pageProperties);
        if (templatePageDefinition != null) pageProperties.putAll(templatePageDefinition.getProperties());
        final String pageID = path[2].getName();
        final ContentManager.PageDefinition<Page> pageDefinition = new ContentManager.PageDefinition<Page>(pageGroupDefinition, pageProperties, pageID, null, Page.class);
        if (!channelDefinitionsRequired) return pageDefinition;
        final ContentManager.ChannelSpecification<?> templateChannelSpecification = (templatePageDefinition != null) ? templatePageDefinition.getChannelSpecification("document_template") : null;
        final String templateChannelGroupID = (templateChannelSpecification != null) ? templateChannelSpecification.getParentDefinition().getID() : "body";
        ContentManager.ChannelGroupDefinition templateChannelGroupDefinition = null;
        if (templatePageDefinition != null) {
            for (ContentManager.ChannelGroupDefinition channelGroupDefinition : CollectionUtil.mkNotNull(templatePageDefinition.getChildDefinitions())) {
                if (templateChannelGroupID.equals(channelGroupDefinition.getID())) {
                    templateChannelGroupDefinition = channelGroupDefinition;
                    break;
                }
                channelGroupDefinition.duplicate(pageDefinition, null, true, null);
            }
        }
        if (pageFolderDescendants != null) {
            int channelGroupFolderIndex = -1;
            for (Tree<FileableCmisObject> channelGroupFolderCandidateTree : pageFolderDescendants) {
                if (!(channelGroupFolderCandidateTree.getItem() instanceof Folder)) continue;
                path[3] = channelGroupFolderCandidateTree.getItem();
                channelGroupFolderIndex++;
                createChannelGroup(pageDefinition, path, channelGroupFolderCandidateTree.getChildren(), templateChannelGroupDefinition, channelGroupFolderIndex, securityContext, httpHeaders);
            }
        }
        if (templatePageDefinition != null) {
            boolean afterPlaceholder = false;
            for (ContentManager.ChannelGroupDefinition channelGroupDefinition : CollectionUtil.mkNotNull(templatePageDefinition.getChildDefinitions())) {
                if (!afterPlaceholder) {
                    if (templateChannelGroupID.equals(channelGroupDefinition.getID())) afterPlaceholder = true;
                    continue;
                }
                channelGroupDefinition.duplicate(pageDefinition, null, true, null);
            }
        }
        return pageDefinition;
    }
