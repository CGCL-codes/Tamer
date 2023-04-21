        public Object put(Object key, Object value) {
            if ((key == null) || (value == null)) {
                return value;
            }
            String ks = key.toString();
            String vs = value.toString();
            if (ks.startsWith(RTKey.CONSTANT_PREFIX)) {
                if (this.debugMode) {
                    Print.logInfo("(DEBUG) Found Constant key: " + ks);
                }
                if (ks.equalsIgnoreCase("%debugMode")) {
                    this.debugMode = StringTools.parseBoolean(vs, false);
                    if (this.debugMode) {
                        Print.logInfo("(DEBUG) 'debugMode' set to " + this.debugMode);
                    }
                    return value;
                } else if (ks.equalsIgnoreCase(KEY_INCLUDE_URL) || ks.equalsIgnoreCase(KEY_INCLUDE_URL_OPT)) {
                    String v = RTConfig.insertKeyValues(vs, this.orderedMap);
                    if (StringTools.isBlank(v)) {
                        Print.logError("Invalid/blank 'include' URL: " + vs);
                    } else if (this.recursionLevel >= MAX_INCLUDE_RECURSION) {
                        Print.logWarn("Excessive 'include' recursion [%s] ...", v);
                    } else {
                        InputStream uis = null;
                        URL url = null;
                        try {
                            if (this.debugMode) {
                                Print.logInfo("(DEBUG) Including: " + v);
                            }
                            url = new URL(v);
                            String parent = (this.inputURL != null) ? this.inputURL.toString() : "";
                            String parProto = (this.inputURL != null) ? this.inputURL.getProtocol().toLowerCase() : "";
                            String urlProto = url.getProtocol().toLowerCase();
                            String urlPath = url.getPath();
                            if (StringTools.isBlank(parProto)) {
                            } else if (parProto.equals(INCLUDE_PROTOCOL_FILE)) {
                                if (urlProto.equals(INCLUDE_PROTOCOL_FILE) && !urlPath.startsWith("/")) {
                                    int ls = parent.lastIndexOf("/");
                                    if (ls > 0) {
                                        url = new URL(parent.substring(0, ls + 1) + urlPath);
                                    }
                                }
                            } else if (parProto.startsWith(INCLUDE_PROTOCOL_HTTP)) {
                                if (urlProto.equals(INCLUDE_PROTOCOL_FILE)) {
                                    Print.logError("Invalid 'include' URL protocol: " + url);
                                    url = null;
                                } else if (urlProto.equals(parProto) && !urlPath.startsWith("/")) {
                                    int cs = parent.indexOf("://");
                                    int ls = parent.lastIndexOf("/");
                                    if ((cs > 0) && (ls >= (cs + 3))) {
                                        url = new URL(parent.substring(0, ls + 1) + urlPath);
                                    }
                                }
                            } else {
                            }
                            if (url != null) {
                                if (this.debugMode) {
                                    Print.logInfo("(DEBUG) Including URL: [" + vs + "] " + url);
                                }
                                uis = url.openStream();
                                OrderedProperties props = new OrderedProperties(this.recursionLevel + 1, url);
                                props.put(RTKey.CONFIG_URL, url.toString());
                                props.load(uis);
                                props.remove(RTKey.CONFIG_URL);
                                this.orderedMap.putAll(props.getOrderedMap());
                            }
                        } catch (MalformedURLException mue) {
                            Print.logException("Invalid URL: " + url, mue);
                        } catch (IllegalArgumentException iae) {
                            Print.logException("Invalid URL arguments: " + url, iae);
                        } catch (Throwable th) {
                            if (!ks.equalsIgnoreCase(KEY_INCLUDE_URL_OPT)) {
                                Print.logException("Error including properties: " + url, th);
                            } else {
                            }
                        } finally {
                            if (uis != null) {
                                try {
                                    uis.close();
                                } catch (IOException ioe) {
                                }
                            }
                        }
                    }
                    return value;
                } else if (ks.equalsIgnoreCase(RTKey.LOG)) {
                    if (RTProperties.this.getConfigLogMessagesEnabled()) {
                        StringBuffer sb = new StringBuffer();
                        if (this.inputURL != null) {
                            String filePath = this.inputURL.getPath();
                            int p = filePath.lastIndexOf("/");
                            String fileName = (p >= 0) ? filePath.substring(p + 1) : filePath;
                            sb.append("[").append(fileName).append("] ");
                        }
                        RTProperties tempProps = new RTProperties(this);
                        RTConfig.pushTemporaryProperties(tempProps);
                        Print.resetVars();
                        sb.append(RTConfig.insertKeyValues(vs, this.orderedMap)).append("\n");
                        Print._writeLog(sb.toString());
                        RTConfig.popTemporaryProperties(tempProps);
                    }
                    return value;
                } else if (ks.equalsIgnoreCase(RTKey.CONFIG_URL)) {
                    Object rtn = super.put(key, value);
                    this.orderedMap.put(ks, vs);
                    return rtn;
                } else {
                    Print.logError("Invalid/unrecognized key specified: " + ks);
                    return value;
                }
            } else {
                Object rtn = super.put(key, value);
                this.orderedMap.put(ks, vs);
                return rtn;
            }
        }
