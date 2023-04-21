    private void setUri(String uri, String proxy, int proxyport) {
        try {
            String oldUri = this.uri;
            this.uri = uri;
            URL url = new URL(uri);
            URLConnection urlCon;
            if (proxy != null) urlCon = url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, proxyport))); else urlCon = url.openConnection();
            BufferedInputStream in = new BufferedInputStream(urlCon.getInputStream());
            CalendarBuilder builder = new CalendarBuilder();
            iCalendar = builder.build(in);
            updateAppointments();
            propertyChangeSupport.firePropertyChange("uri", oldUri, this.uri);
        } catch (Throwable t) {
            t.printStackTrace();
            LOG.fatal("FATAL Error: " + t.getMessage());
        }
    }
