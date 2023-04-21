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
