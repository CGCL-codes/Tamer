    private Panel getXSectionGenerationPanel(final Channel channel) {
        final Node upNode = mapPanel.getNodeManager().getNodes().getNode(channel.getUpNodeId());
        final Node downNode = mapPanel.getNodeManager().getNodes().getNode(channel.getDownNodeId());
        Panel panel = new FlowPanel();
        Button generateXSectionsButton = new Button("Generate XSections");
        Button clearXSectionsButton = new Button("Clear XSections");
        panel.add(generateXSectionsButton);
        panel.add(clearXSectionsButton);
        generateXSectionsButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                List<XSection> generated = ModelUtils.generateCrossSections(channel, upNode, downNode, 5000.0, 3000.0);
                for (XSection x : generated) {
                    channel.addXSection(x);
                }
                mapPanel.getChannelManager().drawXSectionLines(channel, ChannelInfoPanel.this);
                mapPanel.showMessage("Generated xsections with minimum spacing of 5000ft for channel");
                final CrossSectionEditorPanel xsEditorPanel = mapPanel.getChannelManager().getXSectionLineClickHandler().getXsEditorPanel();
                new GenerateProfileForXSection(xsEditorPanel, mapPanel, channel).generateNextProfile();
            }
        });
        clearXSectionsButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                mapPanel.getChannelManager().clearXSections();
                mapPanel.showMessage("Removed all xsections for selected channel");
            }
        });
        return panel;
    }
