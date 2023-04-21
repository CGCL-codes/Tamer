        private BrowseHandler initBrowseHandler() {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                return BrowseHandler.Desktop;
            } else {
                try {
                    Runtime.getRuntime().exec(xdgOpenCmd);
                    return BrowseHandler.Xdg;
                } catch (IOException ioex) {
                }
            }
            return null;
        }
