        public void run() {
            try {
                URL url = new URL(checkURL);
                url.openConnection().getContent();
                Boot.setOffline(false);
            } catch (MalformedURLException badURL) {
                String title = Resources.bundle.getString("check.online.dialog.title");
                String msg = Resources.bundle.getString("check.online.bad.url.error");
                logger.log(Level.WARNING, msg);
                Boot.showError(title, msg, null);
            } catch (IOException ioExp) {
                ioExp.printStackTrace();
                Boot.setOffline(true);
            }
        }
