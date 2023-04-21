    public void run() {
        try {
            try {
                BufferedReader is = new BufferedReader(new InputStreamReader(url_.openStream()));
                webSource_.getWrapper().wrapResults(is, this);
                is.close();
            } catch (NullPointerException ex) {
                logIt("WebSourcePage.run() nullpointer: " + ex);
                fInError = true;
            }
            totalTime_ = (new Date()).getTime() - startTime_;
            fAllResultsExtracted = true;
        } catch (NoRouteToHostException ex) {
            logIt("WebSourcePage.run() NoRouteToHost: " + ex);
            fInError = true;
        } catch (ConnectException ex) {
            logIt("WebSourcePage.run(): " + ex);
            fInError = true;
        } catch (MalformedURLException ex) {
            logIt("WebSourcePage.run(): " + ex);
            fInError = true;
        } catch (IOException ex) {
            logIt("WebSourcePage.run(): " + ex);
            fInError = true;
        }
    }
