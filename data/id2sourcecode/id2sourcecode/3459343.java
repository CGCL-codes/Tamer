    private final Sequence processText(MemTreeBuilder builder, Sequence nodes, int width, FunctionCall callback, FunctionCall resultCallback, Sequence extraArgs) throws XPathException {
        StringBuffer str = new StringBuffer();
        NodeValue node;
        List offsets = null;
        NodeProxy firstProxy = null;
        for (SequenceIterator i = nodes.iterate(); i.hasNext(); ) {
            node = (NodeValue) i.nextItem();
            if (node.getImplementationType() == NodeValue.IN_MEMORY_NODE) throw new XPathException(getASTNode(), "Function kwic-display" + " can not be invoked on constructed nodes");
            NodeProxy proxy = (NodeProxy) node;
            if (firstProxy == null) firstProxy = proxy;
            TextImpl text = (TextImpl) proxy.getNode();
            Match next = proxy.getMatches();
            while (next != null) {
                if (next.getNodeId().equals(text.getNodeId())) {
                    if (offsets == null) offsets = new ArrayList();
                    int freq = next.getFrequency();
                    for (int j = 0; j < freq; j++) {
                        Match.Offset offset = next.getOffset(j);
                        offset.setOffset(str.length() + offset.getOffset());
                        offsets.add(offset);
                    }
                }
                next = next.getNextMatch();
            }
            str.append(text.getData());
        }
        ValueSequence result = new ValueSequence();
        DocumentBuilderReceiver receiver = new DocumentBuilderReceiver(builder);
        int nodeNr;
        int currentWidth = 0;
        if (offsets == null) {
            if (width > str.length()) width = str.length();
            nodeNr = builder.characters(str.substring(0, width));
            result.add(builder.getDocument().getNode(nodeNr));
            currentWidth += width;
        } else {
            FastQSort.sort(offsets, 0, offsets.size() - 1);
            int nextOffset = 0;
            int pos = 0;
            int lastNodeNr = -1;
            Sequence params[] = new Sequence[callback.getSignature().getArgumentCount()];
            params[1] = firstProxy;
            params[2] = extraArgs;
            if (str.length() > width) {
                Match.Offset firstMatch = (Match.Offset) offsets.get(nextOffset++);
                if (firstMatch.getOffset() > 0) {
                    int leftWidth = (width - firstMatch.getLength()) / 2;
                    if (firstMatch.getOffset() > leftWidth) {
                        pos = truncateStart(str, firstMatch.getOffset() - leftWidth, firstMatch.getOffset());
                        leftWidth = firstMatch.getOffset() - pos;
                    } else leftWidth = firstMatch.getOffset();
                    nodeNr = builder.characters(str.substring(pos, pos + leftWidth));
                    if (lastNodeNr != nodeNr) result.add(builder.getDocument().getNode(nodeNr));
                    lastNodeNr = nodeNr;
                    currentWidth += leftWidth;
                    pos += leftWidth;
                }
                params[0] = new StringValue(str.substring(firstMatch.getOffset(), firstMatch.getOffset() + firstMatch.getLength()));
                if (callback.getSignature().getArgumentCount() == 4) {
                    params[3] = new ValueSequence();
                    params[3].add(new IntegerValue(nextOffset - 1));
                    params[3].add(new IntegerValue(firstMatch.getOffset()));
                    params[3].add(new IntegerValue(firstMatch.getLength()));
                }
                Sequence callbackResult = callback.evalFunction(null, null, params);
                for (SequenceIterator iter = callbackResult.iterate(); iter.hasNext(); ) {
                    Item next = iter.nextItem();
                    if (Type.subTypeOf(next.getType(), Type.NODE)) {
                        nodeNr = builder.getDocument().getLastNode();
                        try {
                            next.copyTo(context.getBroker(), receiver);
                            result.add(builder.getDocument().getNode(++nodeNr));
                            lastNodeNr = nodeNr;
                        } catch (SAXException e) {
                            throw new XPathException(getASTNode(), "Internal error while copying nodes: " + e.getMessage(), e);
                        }
                    }
                }
                currentWidth += firstMatch.getLength();
                pos += firstMatch.getLength();
            } else width = str.length();
            Match.Offset offset;
            for (int i = nextOffset; i < offsets.size() && currentWidth < width; i++) {
                offset = (Match.Offset) offsets.get(i);
                if (offset.getOffset() > pos) {
                    int len = offset.getOffset() - pos;
                    if (currentWidth + len > width) len = width - currentWidth;
                    nodeNr = builder.characters(str.substring(pos, pos + len));
                    if (lastNodeNr != nodeNr) result.add(builder.getDocument().getNode(nodeNr));
                    currentWidth += len;
                    pos += len;
                }
                if (currentWidth + offset.getLength() < width) {
                    params[0] = new StringValue(str.substring(offset.getOffset(), offset.getOffset() + offset.getLength()));
                    if (callback.getSignature().getArgumentCount() == 4) {
                        params[3] = new ValueSequence();
                        params[3].add(new IntegerValue(i));
                        params[3].add(new IntegerValue(offset.getOffset()));
                        params[3].add(new IntegerValue(offset.getLength()));
                    }
                    Sequence callbackResult = callback.evalFunction(null, null, params);
                    for (SequenceIterator iter = callbackResult.iterate(); iter.hasNext(); ) {
                        Item next = iter.nextItem();
                        if (Type.subTypeOf(next.getType(), Type.NODE)) {
                            nodeNr = builder.getDocument().getLastNode();
                            try {
                                next.copyTo(context.getBroker(), receiver);
                                result.add(builder.getDocument().getNode(++nodeNr));
                                lastNodeNr = nodeNr;
                            } catch (SAXException e) {
                                throw new XPathException(getASTNode(), "Internal error while copying nodes: " + e.getMessage(), e);
                            }
                        }
                    }
                    currentWidth += offset.getLength();
                    pos += offset.getLength();
                } else break;
            }
            if (currentWidth < width && pos < str.length()) {
                boolean truncated = false;
                int len = str.length() - pos;
                if (len > width - currentWidth) {
                    truncated = true;
                    len = width - currentWidth;
                }
                nodeNr = builder.characters(str.substring(pos, pos + len));
                if (lastNodeNr != nodeNr) result.add(builder.getDocument().getNode(nodeNr));
                lastNodeNr = nodeNr;
                currentWidth += len;
                if (truncated) {
                    nodeNr = builder.characters(" ...");
                    if (lastNodeNr != nodeNr) result.add(builder.getDocument().getNode(nodeNr));
                    lastNodeNr = nodeNr;
                }
            }
        }
        if (resultCallback != null) {
            Sequence params[] = new Sequence[3];
            params[0] = result;
            params[1] = new IntegerValue(currentWidth);
            params[2] = extraArgs;
            return resultCallback.evalFunction(null, null, params);
        } else return result;
    }
