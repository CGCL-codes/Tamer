    public JGraph buildChannelJGraph(Kind k, ConsoleMagicSheet ms, KeyBinder kb, LiveCue c, ConsoleSettings cs) {
        GraphModel model = new DefaultGraphModel();
        JGraph graph = new JGraph(model);
        kb.bind(graph);
        graph.setInvokesStopCellEditing(true);
        graph.setEditable(false);
        graph.setJumpToDefaultPort(true);
        graph.setBackground(_graphBackgroundColor);
        graph.setHighlightColor(_cellSelectionColor);
        graph.setLockedHandleColor(_cellSelectionColor);
        graph.getGraphLayoutCache().setFactory(new DefaultCellViewFactory() {

            protected VertexView createVertexView(Object v) {
                if (v instanceof GraphCell) return new JGraphMultilineView(v);
                return super.createVertexView(v);
            }
        });
        createGraphNodes(k, ms, graph, c, cs.getChannelsPerLine(), cs.getChannelGrouping(), cs.getLineGrouping());
        graph.clearSelection();
        graph.addGraphSelectionListener(new ChannelSelectionListener(_actInt));
        graph.getModel().addGraphModelListener(new ChannelMoveListener(_actInt));
        return graph;
    }
