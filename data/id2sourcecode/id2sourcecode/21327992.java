        public void setInputData(final Object[] objects, final IAsyncModelCallback iAsyncModelCallback) {
            ServiceFactory.invoke(ChannelManager.class.getName(), "getChannelTree", new Object[] { null }, new LoadingAsyncCallback() {

                public void success(Object o) {
                    iAsyncModelCallback.setModelElments(o);
                    if (objects != null && ((Boolean) objects[0])) if (ChannelTree.this.iAsyncModelCallback != null) ChannelTree.this.iAsyncModelCallback.setModelElments(o);
                }
            });
        }
