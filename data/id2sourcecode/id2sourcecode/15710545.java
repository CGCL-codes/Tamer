    public synchronized void setItemSelection(final ItemSelection newSelection) throws JIException, FatalInitializationException, Exception {
        final List<CompositeItem> addedComposite = new ArrayList<CompositeItem>();
        final List<CompositeItem> removedComposite = new ArrayList<CompositeItem>();
        final Map<String, CompositeItem> remainingComposite = new HashMap<String, CompositeItem>(selection.composite.size());
        for (CompositeItem c : selection.composite) {
            remainingComposite.put(c.getName(), c);
        }
        for (CompositeItem newComp : newSelection.composite) {
            final String name = newComp.getName();
            final CompositeItem oldComp = remainingComposite.remove(name);
            if (oldComp == null) {
                addedComposite.add(newComp);
            } else if (!oldComp.equals(newComp)) {
                removedComposite.add(oldComp);
                addedComposite.add(newComp);
            }
        }
        removedComposite.addAll(remainingComposite.values());
        final Map<String, String[]> addedOpcItemsId = new HashMap<String, String[]>();
        final List<String> removedOpcItemsId = new ArrayList<String>();
        final List<String> remainingOPCItems = new ArrayList<String>(selection.opcItemsId.keySet());
        for (Entry<String, String[]> e : newSelection.opcItemsId.entrySet()) {
            final String id = e.getKey();
            final int pos = remainingOPCItems.indexOf(id);
            if (pos < 0) {
                addedOpcItemsId.put(id, e.getValue());
            } else {
                remainingOPCItems.remove(pos);
            }
        }
        removedOpcItemsId.addAll(remainingOPCItems);
        if (addedComposite.isEmpty() && removedComposite.isEmpty() && addedOpcItemsId.isEmpty() && removedOpcItemsId.isEmpty()) {
            logger.info("No selection change");
            return;
        }
        synchronized (opcDataProviderLock) {
            final Group group = opcDataProvider.getGroup();
            final Map<String, Exception> blackList = new HashMap<String, Exception>();
            final Map<CompositeItem, Exception> blackListComp = new HashMap<CompositeItem, Exception>();
            synchronized (sleepMutex) {
                opcDataProvider.stop();
                writer.stop();
                for (CompositeItem c2rm : removedComposite) {
                    for (CompositeItem c : selection.composite) {
                        if (c.getName().equals(c2rm.getName())) {
                            selection.composite.remove(c);
                            break;
                        }
                    }
                }
                final String[] emptyPropPath = new String[0];
                final Map<String, String[]> itemIdsComp = new HashMap<String, String[]>(addedComposite.size());
                for (CompositeItem comp : addedComposite) {
                    Collection<String> ids = comp.getElementIds();
                    for (String id : ids) {
                        if (!item2infoMap.containsKey(id)) {
                            itemIdsComp.put(id, emptyPropPath);
                        }
                    }
                }
                final List<AbstractOPCItemInfo> itemInfo4Comp = validateAdd(group, itemIdsComp, null, blackList);
                final List<CompositeItem> compList = prepareComposite(addedComposite, blackListComp);
                selection.composite.addAll(compList);
                for (String id : removedOpcItemsId) {
                    selection.opcItemsId.remove(id);
                }
                selection.opcItemsId.putAll(addedOpcItemsId);
                Map<String, String[]> missingOPCItems = new HashMap<String, String[]>();
                for (Entry<String, String[]> e : addedOpcItemsId.entrySet()) {
                    final String id = e.getKey();
                    AbstractOPCItemInfo info = item2infoMap.get(id);
                    if (info != null) {
                        if (info.isArray()) {
                            List<int[]> indexes = newSelection.arrayIndexes.get(id);
                            if (indexes == null || indexes.isEmpty()) {
                                List<int[]> oldIndexes = selection.arrayIndexes.get(id);
                                if (oldIndexes == null || oldIndexes.isEmpty()) {
                                    indexes = ((ArrayOPCItemInfo) info).getElementIndexes();
                                } else {
                                    indexes = OPCArrayManager.getAllArrayIndexes(info.getOPCItem());
                                }
                            }
                            if (indexes == null || indexes.isEmpty()) {
                                I18NException ex = new I18NException(bundle, "The_opc_array_item_{0}_must_have_at_least_one_selected_element", id);
                                handleError(blackList, id, ex);
                                continue;
                            }
                            ((ArrayOPCItemInfo) info).setIndexes(indexes);
                        }
                    } else {
                        missingOPCItems.put(id, e.getValue());
                    }
                }
                List<AbstractOPCItemInfo> newItemsInfo = validateAdd(group, missingOPCItems, newSelection.arrayIndexes, blackList);
                selection.arrayIndexes.clear();
                selection.arrayIndexes.putAll(newSelection.arrayIndexes);
                List<AbstractOPCItemInfo> opcItems = new ArrayList<AbstractOPCItemInfo>(selection.opcItemsId.size());
                for (String id : selection.opcItemsId.keySet()) {
                    final AbstractOPCItemInfo i = item2infoMap.get(id);
                    if (i != null) {
                        opcItems.add(i);
                    }
                }
                final Set<String> used = new HashSet<String>(itemIdsComp.size());
                for (AbstractOPCItemInfo info : opcItems) {
                    used.add(info.getOPCItem().getId());
                }
                for (CompositeItem comp : selection.composite) {
                    used.addAll(comp.getElementIds());
                }
                for (String id : removedOpcItemsId) {
                    if (!used.contains(id)) {
                        AbstractOPCItemInfo i = item2infoMap.remove(id);
                        i.getOPCItem().getGroup().removeItem(id);
                    }
                }
                for (CompositeItem comp : removedComposite) {
                    for (String id : comp.getElementIds()) {
                        if (!used.contains(id)) {
                            AbstractOPCItemInfo i = item2infoMap.remove(id);
                            if (i != null) {
                                i.getOPCItem().getGroup().removeItem(id);
                            }
                        }
                    }
                }
                if (!blackList.isEmpty() || !blackListComp.isEmpty()) {
                    if (decisionMaker != null && !decisionMaker.continueOnOPCItemError(blackList, blackListComp)) {
                        throw new FatalInitializationException(new I18NException(bundle, "OPC2Out_aborted"));
                    }
                    for (String id : blackList.keySet()) {
                        if (!used.contains(id)) {
                            AbstractOPCItemInfo i = item2infoMap.remove(id);
                            i.getOPCItem().getGroup().removeItem(id);
                        }
                    }
                    for (CompositeItem comp : blackListComp.keySet()) {
                        for (String id : comp.getElementIds()) {
                            if (!used.contains(id)) {
                                AbstractOPCItemInfo i = item2infoMap.remove(id);
                                if (i != null) {
                                    i.getOPCItem().getGroup().removeItem(id);
                                }
                            }
                        }
                    }
                }
                prepareOPCDataProvider(group);
                final CompositeItem[] composite = selection.composite.toArray(new CompositeItem[selection.composite.size()]);
                writer.initialize(opcItems.toArray(new AbstractOPCItemInfo[opcItems.size()]), composite, readProperties);
            }
        }
        logger.log(Level.INFO, "Selection changed: {0} OPC-DA items added, {1} OPC-DA items removed, {2} composite items added, {3} composite items removed", new Integer[] { addedOpcItemsId.size(), removedOpcItemsId.size(), addedComposite.size(), removedComposite.size() });
    }
