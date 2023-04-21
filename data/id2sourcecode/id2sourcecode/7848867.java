        private boolean keepSearching(Token token) {
            if (token.getType() < 0) {
                return false;
            }
            for (int ignoreChannel : ignoreChannels) {
                if (ignoreChannel == token.getChannel()) {
                    return true;
                }
            }
            return false;
        }
