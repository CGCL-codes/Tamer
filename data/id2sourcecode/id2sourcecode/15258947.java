    public NatChecker(AzureusCore azureus_core, InetAddress bind_ip, int port, boolean http_test) {
        String check = "azureus_rand_" + String.valueOf((int) (Math.random() * 100000));
        if (port < 0 || port > 65535 || port == 6880) {
            result = NAT_UNABLE;
            additional_info = "Invalid port";
            return;
        }
        NatCheckerServer server;
        try {
            server = new NatCheckerServer(bind_ip, port, check, http_test);
        } catch (Throwable e) {
            result = NAT_UNABLE;
            additional_info = "Can't initialise server: " + Debug.getNestedExceptionMessage(e);
            return;
        }
        PluginInterface pi_upnp = azureus_core.getPluginManager().getPluginInterfaceByClass(UPnPPlugin.class);
        UPnPMapping new_mapping = null;
        String upnp_str = null;
        if (pi_upnp != null) {
            UPnPPlugin upnp = (UPnPPlugin) pi_upnp.getPlugin();
            UPnPMapping mapping = upnp.getMapping(true, port);
            if (mapping == null) {
                new_mapping = mapping = upnp.addMapping("NAT Tester", true, port, true);
                try {
                    Thread.sleep(500);
                } catch (Throwable e) {
                    Debug.printStackTrace(e);
                }
            }
            UPnPPluginService[] services = upnp.getServices();
            if (services.length > 0) {
                upnp_str = "";
                for (int i = 0; i < services.length; i++) {
                    UPnPPluginService service = services[i];
                    upnp_str += (i == 0 ? "" : ",") + service.getInfo();
                }
            }
        }
        try {
            server.start();
            String urlStr = Constants.NAT_TEST_SERVER_HTTP + (http_test ? "httptest" : "nattest") + "?port=" + String.valueOf(port) + "&check=" + check;
            if (upnp_str != null) {
                urlStr += "&upnp=" + URLEncoder.encode(upnp_str, "UTF8");
            }
            NetworkAdminASN net_asn = NetworkAdmin.getSingleton().getCurrentASN();
            String as = net_asn.getAS();
            String asn = net_asn.getASName();
            if (as.length() > 0) {
                urlStr += "&as=" + URLEncoder.encode(as, "UTF8");
                urlStr += "&asn=" + URLEncoder.encode(asn, "UTF8");
            }
            urlStr += "&locale=" + MessageText.getCurrentLocale().toString();
            String ip_override = TRTrackerUtils.getPublicIPOverride();
            if (ip_override != null) {
                urlStr += "&ip=" + ip_override;
            }
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            ByteArrayOutputStream message = new ByteArrayOutputStream();
            InputStream is = con.getInputStream();
            byte[] data = new byte[1024];
            int expected_length = -1;
            while (true) {
                int len = is.read(data);
                if (len <= 0) {
                    break;
                }
                message.write(data, 0, len);
                if (expected_length == -1 && message.size() >= 4) {
                    byte[] bytes = message.toByteArray();
                    ByteBuffer bb = ByteBuffer.wrap(bytes);
                    expected_length = bb.getInt();
                    message = new ByteArrayOutputStream();
                    if (bytes.length > 4) {
                        message.write(bytes, 4, bytes.length - 4);
                    }
                }
                if (expected_length != -1 && message.size() == expected_length) {
                    break;
                }
            }
            Map map = BDecoder.decode(message.toByteArray());
            int reply_result = ((Long) map.get("result")).intValue();
            switch(reply_result) {
                case 0:
                    {
                        byte[] reason = (byte[]) map.get("reason");
                        if (reason != null) {
                            Logger.log(new LogEvent(LOGID, LogEvent.LT_ERROR, "NAT CHECK FAILED: " + new String(reason)));
                        }
                        result = NAT_KO;
                        additional_info = reason == null ? "Unknown" : new String(reason, "UTF8");
                        break;
                    }
                case 1:
                    {
                        result = NAT_OK;
                        byte[] reply = (byte[]) map.get("reply");
                        if (reply != null) {
                            additional_info = new String(reply, "UTF8");
                        }
                        break;
                    }
                default:
                    {
                        result = NAT_UNABLE;
                        additional_info = "Invalid response";
                        break;
                    }
            }
            byte[] ip_bytes = (byte[]) map.get("ip_address");
            if (ip_bytes != null) {
                try {
                    ip_address = InetAddress.getByAddress(ip_bytes);
                } catch (Throwable e) {
                }
            }
        } catch (Exception e) {
            result = NAT_UNABLE;
            additional_info = "Error: " + Debug.getNestedExceptionMessage(e);
        } finally {
            server.stopIt();
            if (new_mapping != null) {
                new_mapping.destroy();
            }
        }
    }
