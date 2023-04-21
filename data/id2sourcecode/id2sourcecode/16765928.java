    public LinkedList getChannelList() throws ServletException {
        if (listID.length() > 0) {
            ListNode node = (ListNode) listIDMap.get(listID);
            if (node != null) {
                ViewEntity ve = (ViewEntity) node.getEntityMap().get(toExpand);
                if (ve != null) ve.setExpandSubList(!ve.isExpandSubList());
                currentServer = node.getCurrentServer();
                return node.getList();
            }
        }
        try {
            sink.OpenRBNBConnection(ServerAddress, "WebTurbine.Viewer");
            String serverName = sink.GetServerName();
            ChannelMap requestMap = new ChannelMap(), fetchedMap = new ChannelMap();
            requestMap.Add(channels == null ? "..." : channels);
            sink.RequestRegistration(requestMap);
            sink.Fetch(10000, fetchedMap);
            sink.CloseRBNBConnection();
            if (fetchedMap.NumberOfChannels() == 0) {
                if (fetchedMap.GetIfFetchTimedOut()) errorString = "Timeout receiving registration."; else errorString = "No channels match the specified channel string." + "&nbsp; Please	try a more general search expression.";
                listID = "";
                currentServer = "";
                return new LinkedList();
            } else {
                ChannelTree tree = ChannelTree.createFromChannelMap(fetchedMap);
                ArrayList al = new ArrayList();
                for (Iterator iter = tree.iterator(); iter.hasNext(); ) {
                    ChannelTree.Node node = (ChannelTree.Node) iter.next();
                    if (node.getType() == ChannelTree.SERVER) al.add(node.getFullName());
                }
                String children[] = new String[al.size()];
                al.toArray(children);
                int childIndex = 0;
                if (channels.charAt(0) == '/') {
                    currentServer = children[0];
                    for (; childIndex < children.length && channels.startsWith(children[childIndex]); ++childIndex) currentServer = children[childIndex];
                } else currentServer = serverName;
                ListNode node = doSearch(fetchedMap, keywords);
                final LinkedList nodeList = node.getList();
                recurseList(nodeList, "", node.getEntityMap());
                ListIterator lIter = nodeList.listIterator();
                int lastSlash = currentServer.lastIndexOf('/');
                String parentName = currentServer.substring(0, lastSlash);
                if (lastSlash != 0) {
                    ViewEntity ve = new ViewEntity(parentName, null);
                    ve.setParent(true);
                    lIter.add(ve);
                }
                for (int ii = childIndex; ii < children.length; ++ii) {
                    {
                        if (children[ii].charAt(0) != '/') children[ii] = currentServer + '/' + children[ii];
                        ViewEntity ve = new ViewEntity(children[ii], null);
                        ve.setChild(true);
                        lIter.add(ve);
                    }
                }
                listID = node.toString();
                listIDMap.put(listID, node);
                return nodeList;
            }
        } catch (Exception e) {
            sink.CloseRBNBConnection();
            throw new ServletException(e);
        }
    }
