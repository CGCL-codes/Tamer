    private void loadWebPage() {
        final WebInstrument webInstrument = (WebInstrument) iobject;
        if (webInstrument.getUrl().length() > 0) {
            CommandTools.performAsync(new Command() {

                public void perform() {
                    try {
                        html.getDocument().putProperty(Document.StreamDescriptionProperty, null);
                        String urlSpecTmp = webInstrument.getUrl();
                        if (!urlSpecTmp.startsWith("http://") && !urlSpecTmp.startsWith("https://")) {
                            urlSpecTmp = "http://" + urlSpecTmp;
                        }
                        final String urlSpec = urlSpecTmp;
                        final URL url = new URL(urlSpec);
                        URLConnection urlConnection = url.openConnection();
                        if ("text/html".equals(urlConnection.getContentType())) {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    try {
                                        html.setPage(new URL(urlSpec));
                                    } catch (Exception x) {
                                        html.setText(x.toString());
                                    }
                                }
                            });
                        } else {
                            SwingUtilities.invokeLater(new Runnable() {

                                public void run() {
                                    html.setContentType("text/html");
                                    html.setText("<html><body><img src=\"" + urlSpec + "\"></body></html>");
                                }
                            });
                        }
                    } catch (final Exception x) {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                html.setText(x.toString());
                            }
                        });
                    }
                }
            });
        }
    }
