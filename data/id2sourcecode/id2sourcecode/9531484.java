    protected static final Document getChannelIndexDocument(final List<Tree<FileableCmisObject>> channelFolderDescendants) throws WWWeeePortal.Exception {
        for (Tree<FileableCmisObject> channelDocumentCandidateTree : channelFolderDescendants) {
            if (!(channelDocumentCandidateTree.getItem() instanceof Document)) continue;
            final Document channelDocument = (Document) channelDocumentCandidateTree.getItem();
            if ((channelDocument.getName().startsWith("index.")) || (channelDocument.getName().startsWith("default."))) return channelDocument;
        }
        return null;
    }
