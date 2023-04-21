        public CsvData next() {
            CsvData data = null;
            if (this.currentInitialLoadEvent == null && selectFromTableEventsToSend.size() > 0) {
                this.currentInitialLoadEvent = selectFromTableEventsToSend.remove(0);
                TriggerHistory history = this.currentInitialLoadEvent.getTriggerHistory();
                if (this.currentInitialLoadEvent.containsData()) {
                    data = this.currentInitialLoadEvent.getData();
                    this.currentInitialLoadEvent = null;
                    this.currentTable = lookupAndOrderColumnsAccordingToTriggerHistory((String) data.getAttribute(CsvData.ATTRIBUTE_ROUTER_ID), history, currentTable, true);
                } else {
                    this.triggerRouter = this.currentInitialLoadEvent.getTriggerRouter();
                    NodeChannel channel = batch != null ? configurationService.getNodeChannel(batch.getChannelId(), false) : new NodeChannel(this.triggerRouter.getTrigger().getChannelId());
                    this.routingContext = new SimpleRouterContext(batch.getNodeId(), channel);
                    this.currentTable = lookupAndOrderColumnsAccordingToTriggerHistory(triggerRouter.getRouter().getRouterId(), history, currentTable, false);
                    this.startNewCursor(history, triggerRouter);
                    this.currentTable = lookupAndOrderColumnsAccordingToTriggerHistory(triggerRouter.getRouter().getRouterId(), history, currentTable, true);
                }
            }
            if (this.cursor != null) {
                data = this.cursor.next();
                if (data != null) {
                    if (!routerService.shouldDataBeRouted(routingContext, new DataMetaData((Data) data, currentTable, triggerRouter, routingContext.getChannel()), node, true)) {
                        data = next();
                    }
                } else {
                    closeCursor();
                    data = next();
                }
            }
            return data;
        }
