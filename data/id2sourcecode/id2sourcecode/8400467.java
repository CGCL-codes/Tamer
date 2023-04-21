        private OpenHandler initOpenHandler() {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                return OpenHandler.Desktop;
            } else {
                try {
                    Runtime.getRuntime().exec(xdgOpenCmd);
                    return OpenHandler.Xdg;
                } catch (IOException ioex) {
                }
            }
            return null;
        }
