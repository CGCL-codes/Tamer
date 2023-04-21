        public Response checkAccess() throws ConfigManager.ConfigException {
            final RSAccessControl contentContainerAC = definition.getContentContainer().getAccessControl();
            if ((contentContainerAC != null) && (!contentContainerAC.hasAccess(securityContext))) {
                return createNoAccessResponse(definition.getContentContainer().getProperties());
            }
            final RSAccessControl pageGroupAC = definition.getParentDefinition().getAccessControl();
            if ((pageGroupAC != null) && (!pageGroupAC.hasAccess(securityContext))) {
                return createNoAccessResponse(definition.getParentDefinition().getProperties());
            }
            final RSAccessControl pageAC = definition.getAccessControl();
            if ((pageAC != null) && (!pageAC.hasAccess(securityContext))) {
                return createNoAccessResponse(definition.getProperties());
            }
            if (maximizedChannelKey != null) {
                final ContentManager.ChannelSpecification<?> maximizedChannelSpec = definition.getChannelSpecification(maximizedChannelKey);
                final RSAccessControl channelGroupAC = maximizedChannelSpec.getParentDefinition().getAccessControl();
                if ((channelGroupAC != null) && (!channelGroupAC.hasAccess(securityContext))) {
                    return createNoAccessResponse(maximizedChannelSpec.getParentDefinition().getProperties());
                }
                final RSAccessControl channelSpecAC = maximizedChannelSpec.getAccessControl();
                if ((channelSpecAC != null) && (!channelSpecAC.hasAccess(securityContext))) {
                    return createNoAccessResponse(maximizedChannelSpec.getProperties());
                }
            }
            return null;
        }
