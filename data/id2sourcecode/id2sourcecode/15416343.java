    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e);
        if (e.getSource().getClass() == CreateMenuItem.class) {
            CreateMenuItem mi = (CreateMenuItem) e.getSource();
            try {
                if (!mi.isAReplaceCall) {
                    Channel chan = (Channel) mi.classType.newInstance();
                    addTextureNode(new TextureGraphNode(chan), mousePosition.x - desktopX, mousePosition.y - desktopY);
                    repaint();
                } else {
                    TextureGraphNode node = graph.selectedNodes.get(0);
                    if (node != null) {
                        TextureGraphNode newNode = new TextureGraphNode((Channel) mi.classType.newInstance());
                        replaceTextureNode(node, newNode);
                        repaint();
                    } else {
                        Logger.logWarning(this, "No node selected for replace.");
                    }
                }
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource() == newChannelInsertMenuItem) {
            if (toCopyTextureGraphNode == null) {
                Logger.logError(this, "No node copied to insert.");
            } else {
                addTextureNode(toCopyTextureGraphNode.cloneThisNode(), mousePosition.x - desktopX, mousePosition.y - desktopY);
                repaint();
            }
        } else if (e.getSource() == copyChannelMenuItem) {
            if (graph.selectedNodes.size() > 0) {
                toCopyTextureGraphNode = graph.selectedNodes.get(0).cloneThisNode();
            } else {
                Logger.logError(this, "no selection in copyChannel popup menu.");
            }
        } else if (e.getSource() == replacepasteChannelMenuItem) {
            if (toCopyTextureGraphNode == null) {
                Logger.logError(this, "No node copied to replace paste.");
            } else if (graph.selectedNodes.size() > 0) {
                replaceTextureNode(graph.selectedNodes.get(0), toCopyTextureGraphNode.cloneThisNode());
                repaint();
            } else {
                Logger.logError(this, "no selection in insert-replaceChannel popup menu.");
            }
        } else if (e.getSource() == previewChannelMenuItem) {
            if (graph.selectedNodes.size() > 0) {
                addPreviewWindow(graph.selectedNodes.get(0));
                repaint();
            }
        } else if (e.getSource() == cloneChannelMenuItem) {
            if (graph.selectedNodes.size() > 0) {
                TextureGraphNode orig = graph.selectedNodes.get(0);
                TextureGraphNode n = new TextureGraphNode(Channel.cloneChannel(graph.selectedNodes.get(0).getChannel()));
                addTextureNode(n, orig.getX() + 32, orig.getY() + 32);
                repaint();
            } else {
                Logger.logError(this, "no selection in cloneChannel popup menu.");
            }
        } else if (e.getSource() == swapInputsChannelMenuItem) {
            TextureGraphNode node = graph.selectedNodes.get(0);
            if (node != null) {
                if (node.getChannel().getNumInputChannels() < 2) return;
                ConnectionPoint p0 = node.getInputConnectionPointByChannelIndex(0);
                ConnectionPoint p1 = node.getInputConnectionPointByChannelIndex(1);
                TextureNodeConnection c0 = graph.getConnectionAtInputPoint(p0);
                TextureNodeConnection c1 = graph.getConnectionAtInputPoint(p1);
                graph.removeConnection(c0);
                graph.removeConnection(c1);
                if (c0 != null && c1 != null) {
                    ConnectionPoint temp = c0.target;
                    c0.target = c1.target;
                    c1.target = temp;
                    graph.addConnection(c0);
                    graph.addConnection(c1);
                } else if (c1 != null) {
                    c1.target = p0;
                    graph.addConnection(c1);
                } else if (c0 != null) {
                    c0.target = p1;
                    graph.addConnection(c0);
                } else {
                    return;
                }
                repaint();
            }
        } else if (e.getSource() == addToPresetsChannelMenuItem) {
            if (graph.selectedNodes.size() > 0) {
                if (graph.selectedNodes.get(0).getChannel() instanceof Pattern) TextureEditor.INSTANCE.m_PatternSelector.addPatternPreset((Pattern) Channel.cloneChannel((Pattern) graph.selectedNodes.get(0).getChannel())); else Logger.logError(this, "Invalid action 'Add to Presets': selected node is not a pattern");
            } else Logger.logError(this, "Invalid action 'Add To Presets': no selected nodes exists.");
        } else if (e.getSource() == deleteChannelMenuItem) {
            action_DeleteSelectedNodes();
        } else if (e.getActionCommand().equals("arbitraryResolutionExport")) {
            String resolution = JOptionPane.showInputDialog(this, "Specify your desried resolution (for example 1024x1024)", "What Resolution?", JOptionPane.QUESTION_MESSAGE);
            if (resolution != null && resolution.matches("\\d+x\\d+")) {
                int resX = Integer.parseInt(resolution.substring(0, resolution.indexOf('x')));
                int resY = Integer.parseInt(resolution.substring(resolution.indexOf('x') + 1, resolution.length()));
                askFileAndExportTexture(resX, resY);
            }
        } else if (e.getActionCommand().matches("\\d+x\\d+")) {
            String s = e.getActionCommand();
            int resX = Integer.parseInt(s.substring(0, s.indexOf('x')));
            int resY = Integer.parseInt(s.substring(s.indexOf('x') + 1, s.length()));
            askFileAndExportTexture(resX, resY);
        } else {
            if (TextureEditor.GL_ENABLED) {
                TextureGraphNode n = graph.selectedNodes.get(0);
                if (n.getChannel().chechkInputChannels()) {
                    if (e.getSource() == openGLDiffuseMenuItem) {
                        TextureEditor.INSTANCE.m_OpenGLPreviewPanel.setDiffuseTextureNode(n);
                        repaint();
                    } else if (e.getSource() == openGLNormalMenuItem) {
                        TextureEditor.INSTANCE.m_OpenGLPreviewPanel.setNormalTextureNode(n);
                        repaint();
                    } else if (e.getSource() == openGLSpecularMenuItem) {
                        TextureEditor.INSTANCE.m_OpenGLPreviewPanel.setSpecWeightTextureNode(n);
                        repaint();
                    } else if (e.getSource() == openGLHeightmapMenuItem) {
                        TextureEditor.INSTANCE.m_OpenGLPreviewPanel.setHeightmapTextureNode(n);
                        repaint();
                    }
                } else Logger.logWarning(this, "Incomplete channel for preview.");
            }
        }
    }
