        public String getChannelTitle() {
            return this.feed.getRootElement().element("channel").element("title").getText();
        }
