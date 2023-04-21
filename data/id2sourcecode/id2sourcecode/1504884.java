        private String getChannelPrefix() {
            if ("rss".equals(this.type)) {
                return "/*/*/";
            } else {
                return "/*/";
            }
        }
