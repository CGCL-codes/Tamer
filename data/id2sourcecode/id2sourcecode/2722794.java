    public boolean visit(final IResourceDelta delta) {
        final IResource res = delta.getResource();
        if (res instanceof IFile) {
            final IFile resourceFile = (IFile) res;
            if (!resourceFile.getFileExtension().equals("class")) {
                final Resource modifiedResource = Activator.buildResource(resourceFile);
                final ModifyResourceEvent event = new ModifyResourceEvent(modifiedResource, Activator.getDefault().getDeveloper());
                if (Activator.getDefault().getDigesterService() != null) {
                    try {
                        final String digest = Activator.getDefault().getDigesterService().digest(resourceFile.getContents());
                        event.setDigest(digest);
                    } catch (final Exception e) {
                        Activator.log(Status.ERROR, "Could not generate digest for resource " + modifiedResource);
                    }
                }
                try {
                    Activator.getDefault().getCliService().communicate(event);
                } catch (final CommunicationException e) {
                    Activator.log(Status.ERROR, "Could not communicate modifiedResource to ceno", e);
                }
            }
            return false;
        }
        return true;
    }
