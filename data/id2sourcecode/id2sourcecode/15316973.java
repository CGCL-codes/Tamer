        public void setInputData(Object[] objects, final IAsyncModelCallback iAsyncModelCallback) {
            ServiceFactory.invoke(ChannelManager.class.getName(), "getChannelTree", new Object[] { null }, new FishAsyncCallback() {

                public void onSuccess(Object o) {
                    iAsyncModelCallback.setModelElments(o);
                    if (mainPanel.getWidget() != null) {
                        mainPanel.getWidget().removeFromParent();
                    }
                    channelTree.expandAll(false);
                }
            });
        }
