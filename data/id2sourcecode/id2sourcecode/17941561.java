    @Override
    protected PopupMenu createPopupMenu() {
        final List connectionMenuContent = getConnectionMenuContent();
        if (connectionMenuContent == null || connectionMenuContent.isEmpty()) {
            return null;
        } else if (connectionMenuContent.size() == 1) {
            List menuContent = getEndMenuContent(connectionMenuContent.get(0));
            if (menuContent == null || menuContent.isEmpty()) {
                return null;
            }
            ILabelProvider labelProvider = getConnectionAndEndLabelProvider(connectionMenuContent.get(0));
            if (handlerEP != null && handlerEP.get() != null) {
                GraphicalViewer graphicalViewer = ((GraphicalViewer) handlerEP.get().getViewer());
                PaletteRoot root = graphicalViewer.getEditDomain().getPaletteViewer().getPaletteRoot();
                menuContent = groupMenuContent(root, menuContent, labelProvider);
            }
            cachedMenu = new WeakReference<PopupMenu>(new PopupMenu(menuContent, labelProvider) {

                /**
         * @see org.eclipse.gmf.runtime.diagram.ui.menus.PopupMenu#getResult()
         */
                @Override
                public Object getResult() {
                    Object endResult = super.getResult();
                    if (endResult == null) {
                        return null;
                    } else {
                        List resultList = new ArrayList(2);
                        resultList.add(connectionMenuContent.get(0));
                        resultList.add((endResult instanceof List) ? ((List) endResult).get(((List) endResult).size() - 1) : endResult);
                        return resultList;
                    }
                }
            });
        } else {
            List menuContent = new ArrayList();
            for (Iterator iter = connectionMenuContent.iterator(); iter.hasNext(); ) {
                Object connectionItem = iter.next();
                List subMenuContent = getEndMenuContent(connectionItem);
                if (subMenuContent.isEmpty()) {
                    continue;
                }
                PopupMenu subMenu = new PopupMenu(subMenuContent, getEndLabelProvider());
                menuContent.add(new PopupMenu.CascadingMenu(connectionItem, subMenu));
            }
            if (!menuContent.isEmpty()) {
                cachedMenu = new WeakReference<PopupMenu>(new PopupMenu(menuContent, getConnectionLabelProvider()));
            }
        }
        return cachedMenu.get();
    }
