    public void partOpened(final IWorkbenchPart part) {
        if (part instanceof IEditorPart) {
            Activator.log(Status.INFO, part + " opened");
            final Resource resource = buildResource(((IEditorPart) part).getEditorInput());
            final OpenResourceEvent openEvent = new OpenResourceEvent(resource, Activator.getDefault().getDeveloper());
            final String digest = Activator.getDefault().digest(((IEditorPart) part).getEditorInput());
            openEvent.setDigest(digest);
            try {
                final ICenoTrackerService trackerService = Activator.getDefault().getTrackerService();
                if (trackerService != null) {
                    final Collection<DeveloperResourceState> devResStates = trackerService.findDeveloperResourceStatesOfResource(resource.getFqName());
                    final Developer currentDeveloper = Activator.getDefault().getDeveloper();
                    final Iterator<DeveloperResourceState> drsIter = devResStates.iterator();
                    while (drsIter.hasNext()) {
                        if (drsIter.next().getDeveloper().getName().equals(currentDeveloper.getName())) {
                            drsIter.remove();
                        }
                    }
                    if (!devResStates.isEmpty()) {
                        final OpenOpenedResourceWarningDialog oorwd = new OpenOpenedResourceWarningDialog(part.getSite().getShell(), devResStates, resource);
                        if (oorwd.open() == Dialog.OK) {
                            final String messageText = oorwd.getMessageText();
                            if (oorwd.isSendToEditors()) {
                                sendMulticatMessage(resource, messageText);
                            }
                        }
                    }
                }
                final IEventCommunicator cliService = Activator.getDefault().getCliService();
                if (cliService != null) {
                    cliService.communicate(openEvent);
                }
            } catch (final CommunicationException e) {
                Activator.log(Status.ERROR, "Could not communicate partOpened to ceno", e);
            }
        }
    }
