    private Collection<NetworkLinkDescriptor> collectAllLinks(View view, Map<EObject, View> domain2NotationMap) {
        if (!NetworkEditPart.MODEL_ID.equals(NetworkVisualIDRegistry.getModelID(view))) {
            return Collections.emptyList();
        }
        LinkedList<NetworkLinkDescriptor> result = new LinkedList<NetworkLinkDescriptor>();
        switch(NetworkVisualIDRegistry.getVisualID(view)) {
            case NetworkEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(NetworkDiagramUpdater.getNetwork_1000ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case NodeEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(NetworkDiagramUpdater.getNode_2001ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
            case ChannelEditPart.VISUAL_ID:
                {
                    if (!domain2NotationMap.containsKey(view.getElement())) {
                        result.addAll(NetworkDiagramUpdater.getChannel_4003ContainedLinks(view));
                    }
                    if (!domain2NotationMap.containsKey(view.getElement()) || view.getEAnnotation("Shortcut") == null) {
                        domain2NotationMap.put(view.getElement(), view);
                    }
                    break;
                }
        }
        for (Iterator children = view.getChildren().iterator(); children.hasNext(); ) {
            result.addAll(collectAllLinks((View) children.next(), domain2NotationMap));
        }
        for (Iterator edges = view.getSourceEdges().iterator(); edges.hasNext(); ) {
            result.addAll(collectAllLinks((View) edges.next(), domain2NotationMap));
        }
        return result;
    }
