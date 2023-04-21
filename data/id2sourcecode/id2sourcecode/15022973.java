    protected RPReply processRequestSupport(RPRequest request, RPRequestAccessController access_controller) {
        try {
            RPObject object = request.getObject();
            String method = request.getMethod();
            if (object == null && method.equals("getSingleton")) {
                RPObject pi = request.createRemotePluginInterface(plugin_interface);
                RPReply reply = new RPReply(pi);
                return (reply);
            } else if (object == null && method.equals("getDownloads")) {
                RPPluginInterface pi = request.createRemotePluginInterface(plugin_interface);
                RPObject dm = (RPObject) pi._process(new RPRequest(null, "getDownloadManager", null)).getResponse();
                RPReply rep = dm._process(new RPRequest(null, "getDownloads", null));
                rep.setProperty("azureus_name", pi.azureus_name);
                rep.setProperty("azureus_version", pi.azureus_version);
                return (rep);
            } else if (object == null) {
                throw new RPNoObjectIDException();
            } else {
                object = RPObject._lookupLocal(object._getOID());
                object._setLocal();
                if (method.equals("_refresh")) {
                    RPReply reply = new RPReply(object);
                    return (reply);
                } else {
                    String name = object._getName();
                    if (access_controller != null) {
                        access_controller.checkAccess(name, request);
                    }
                    RPReply reply = object._process(request);
                    if (name.equals("IPFilter") && method.equals("setInRangeAddressesAreAllowed[boolean]") && request.getClientIP() != null) {
                        String client_ip = request.getClientIP();
                        boolean b = ((Boolean) request.getParams()[0]).booleanValue();
                        LoggerChannel[] channels = plugin_interface.getLogger().getChannels();
                        IPFilter filter = plugin_interface.getIPFilter();
                        if (b) {
                            if (filter.isInRange(client_ip)) {
                                for (int i = 0; i < channels.length; i++) {
                                    channels[i].log(LoggerChannel.LT_INFORMATION, "Adding range for client '" + client_ip + "' as allow/deny flag changed to allow");
                                }
                                filter.createAndAddRange("auto-added for remote interface", client_ip, client_ip, false);
                                filter.save();
                                plugin_interface.getPluginconfig().save();
                            }
                        } else {
                            IPRange[] ranges = filter.getRanges();
                            for (int i = 0; i < ranges.length; i++) {
                                if (ranges[i].isInRange(client_ip)) {
                                    for (int j = 0; j < channels.length; j++) {
                                        channels[j].log(LoggerChannel.LT_INFORMATION, "deleting range '" + ranges[i].getStartIP() + "-" + ranges[i].getEndIP() + "' for client '" + client_ip + "' as allow/deny flag changed to deny");
                                    }
                                    ranges[i].delete();
                                }
                            }
                            filter.save();
                            plugin_interface.getPluginconfig().save();
                        }
                    }
                    return (reply);
                }
            }
        } catch (RPException e) {
            return (new RPReply(e));
        } catch (Exception e) {
            throw new RPInternalProcessException(e);
        }
    }
