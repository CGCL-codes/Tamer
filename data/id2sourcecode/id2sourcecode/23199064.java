    protected static WebServerContent getWebServerContent(HTTPRequest httpRequest) {
        String resourcePath = httpRequest.getResourcePath();
        int index = resourcePath.indexOf('/');
        final int instanceID = Integer.parseInt(resourcePath.substring(0, index));
        final WebBrowserObject webBrowserObject = (WebBrowserObject) ObjectRegistry.getInstance().get(instanceID);
        if (webBrowserObject == null) {
            return null;
        }
        resourcePath = resourcePath.substring(index + 1);
        String type = resourcePath;
        if ("html".equals(type)) {
            final WebBrowserObject component = (WebBrowserObject) ObjectRegistry.getInstance().get(instanceID);
            if (component == null) {
                return new WebServerContent() {

                    @Override
                    public InputStream getInputStream() {
                        return getInputStream("<html><body></body></html>");
                    }
                };
            }
            return new WebServerContent() {

                @Override
                public InputStream getInputStream() {
                    String javascriptDefinitions = component.getJavascriptDefinitions();
                    javascriptDefinitions = javascriptDefinitions == null ? "" : javascriptDefinitions + LS;
                    String content = "<html>" + LS + "  <head>" + LS + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" + LS + "    <script language=\"JavaScript\" type=\"text/javascript\">" + LS + "      <!--" + LS + "      var sendCommand = " + JWebBrowser.COMMAND_FUNCTION + ";" + LS + "      function postCommand(command) {" + LS + "        var elements = new Array();" + LS + "        for(var i=1; i<arguments.length; i++) {" + LS + "          var element = document.createElement('input');" + LS + "          element.type='text';" + LS + "          element.name='j_arg' + (i-1);" + LS + "          element.value=arguments[i];" + LS + "          document.createElement('j_arg' + (i-1));" + LS + "          elements[i-1] = element;" + LS + "          document.j_form.appendChild(element);" + LS + "        }" + LS + "        document.j_form.j_command.value = command;" + LS + "        document.j_form.submit();" + LS + "        for(var i=0; i<elements.length; i++) {" + LS + "          document.j_form.removeChild(elements[i]);" + LS + "        }" + LS + "      }" + LS + "      function getEmbeddedObject() {" + LS + "        var movieName = \"" + getEmbeddedObjectJavascriptName() + "\";" + LS + "        if(window.document[movieName]) {" + LS + "          return window.document[movieName];" + LS + "        }" + LS + "        if(navigator.appName.indexOf(\"Microsoft Internet\") == -1) {" + LS + "          if(document.embeds && document.embeds[movieName]) {" + LS + "            return document.embeds[movieName];" + LS + "          }" + LS + "        } else {" + LS + "          return document.getElementById(movieName);" + LS + "        }" + LS + "      }" + LS + javascriptDefinitions + "      //-->" + LS + "    </script>" + LS + "    <style type=\"text/css\">" + LS + "      html { background-color: " + webBrowserObject.backgroundColor + "; }" + LS + "      html, object, embed, div, body, table { width: 100%; height: 100%; min-height: 100%; margin: 0; padding: 0; overflow: hidden; text-align: center; }" + LS + "      object, embed, div { position: absolute; left:0; top:0;}" + LS + "      td { vertical-align: middle; }" + LS + "    </style>" + LS + "  </head>" + LS + "  <body height=\"*\">" + LS + "    <iframe style=\"display:none;\" name=\"j_iframe\"></iframe>" + LS + "    <form style=\"display:none;\" name=\"j_form\" action=\"" + WebServer.getDefaultWebServer().getDynamicContentURL(WebBrowserObject.class.getName(), "postCommand/" + instanceID) + "\" method=\"POST\" target=\"j_iframe\">" + LS + "      <input name=\"j_command\" type=\"text\"></input>" + LS + "    </form>" + LS + "    <script src=\"" + WebServer.getDefaultWebServer().getDynamicContentURL(WebBrowserObject.class.getName(), String.valueOf(instanceID), "js") + "\"></script>" + LS + "  </body>" + LS + "</html>" + LS;
                    System.out.println(content);
                    return getInputStream(content);
                }
            };
        }
        if ("js".equals(type)) {
            String url = webBrowserObject.resourcePath;
            File file = Utils.getLocalFile(url);
            if (file != null) {
                url = webBrowserObject.getLocalFileURL(file);
            }
            final String escapedURL = Utils.escapeXML(url);
            return new WebServerContent() {

                @Override
                public String getContentType() {
                    return getDefaultMimeType(".js");
                }

                @Override
                public InputStream getInputStream() {
                    ObjectHTMLConfiguration objectHtmlConfiguration = webBrowserObject.getObjectHtmlConfiguration();
                    StringBuilder objectParameters = new StringBuilder();
                    StringBuilder embedParameters = new StringBuilder();
                    Map<String, String> parameters = objectHtmlConfiguration.getHTMLParameters();
                    HashMap<String, String> htmlParameters = parameters == null ? new HashMap<String, String>() : new HashMap<String, String>(parameters);
                    String windowsParamName = objectHtmlConfiguration.getWindowsParamName();
                    String paramName = objectHtmlConfiguration.getParamName();
                    htmlParameters.remove("width");
                    htmlParameters.remove("height");
                    htmlParameters.remove("type");
                    htmlParameters.remove("name");
                    if (windowsParamName != null) {
                        htmlParameters.remove(windowsParamName);
                    }
                    if (paramName != null) {
                        htmlParameters.remove(paramName);
                    }
                    for (Entry<String, String> param : htmlParameters.entrySet()) {
                        String name = Utils.escapeXML(param.getKey());
                        String value = Utils.escapeXML(param.getValue());
                        embedParameters.append(' ').append(name).append("=\"").append(value).append("\"");
                        objectParameters.append("window.document.write('  <param name=\"").append(name).append("\" value=\"").append(value).append("\"/>');" + LS);
                    }
                    String version = objectHtmlConfiguration.getVersion();
                    String versionParameter = version != null ? " version=\"" + version + "\"" : "";
                    String embeddedObjectJavascriptName = getEmbeddedObjectJavascriptName();
                    String content = "<!--" + LS + "window.document.write('<object classid=\"clsid:" + objectHtmlConfiguration.getWindowsClassID() + "\" id=\"" + embeddedObjectJavascriptName + "\" codebase=\"" + objectHtmlConfiguration.getWindowsInstallationURL() + "\" events=\"true\">');" + LS + (windowsParamName == null ? "" : "window.document.write('  <param name=\"" + windowsParamName + "\" value=\"' + decodeURIComponent('" + escapedURL + "') + '\"/>');" + LS) + objectParameters + "window.document.write('  <embed" + embedParameters + " name=\"" + embeddedObjectJavascriptName + "\"" + (paramName == null ? "" : " " + paramName + "=\"" + escapedURL + "\"") + " type=\"" + objectHtmlConfiguration.getMimeType() + "\" pluginspage=\"" + objectHtmlConfiguration.getInstallationURL() + "\"" + versionParameter + ">');" + LS + "window.document.write('  </embed>');" + LS + "window.document.write('</object>');" + LS + "var embeddedObject = getEmbeddedObject();" + LS + "embeddedObject.style.width = '100%';" + LS + "embeddedObject.style.height = '100%';" + LS + "sendCommand('[Chrriis]WB_setLoaded');" + LS + "window.document.attachEvent(\"onkeydown\", function() {" + LS + "  switch (event.keyCode) {" + LS + "    case 116 :" + LS + "      event.returnValue = false;" + LS + "      event.keyCode = 0;" + LS + "      break;" + LS + "  }" + LS + "});" + LS + "//-->" + LS;
                    System.out.println(content);
                    return getInputStream(content);
                }
            };
        }
        if ("postCommand".equals(type)) {
            HTTPData postData = httpRequest.getHTTPPostDataArray()[0];
            Map<String, String> headerMap = postData.getHeaderMap();
            int size = headerMap.size();
            final String command = headerMap.get("j_command");
            final String[] arguments = new String[size - 1];
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = headerMap.get("j_arg" + i);
            }
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    WebBrowserListener[] webBrowserListeners = webBrowserObject.webBrowser.getWebBrowserListeners();
                    WebBrowserCommandEvent e = null;
                    for (int i = webBrowserListeners.length - 1; i >= 0; i--) {
                        if (e == null) {
                            e = new WebBrowserCommandEvent(webBrowserObject.webBrowser, command, arguments);
                        }
                        webBrowserListeners[i].commandReceived(e);
                    }
                }
            });
            return new WebServerContent() {

                @Override
                public InputStream getInputStream() {
                    String content = "<html>" + LS + "  <body>" + LS + "    Command sent successfully." + LS + "  </body>" + LS + "</html>" + LS;
                    return getInputStream(content);
                }
            };
        }
        final String resource = resourcePath;
        return new WebServerContent() {

            @Override
            public InputStream getInputStream() {
                try {
                    String url = webBrowserObject.resourcePath;
                    File file = Utils.getLocalFile(url);
                    if (file != null) {
                        url = webBrowserObject.getLocalFileURL(file);
                    }
                    url = url.substring(0, url.lastIndexOf('/')) + "/" + resource;
                    return new URL(url).openStream();
                } catch (Exception e) {
                }
                return null;
            }
        };
    }
