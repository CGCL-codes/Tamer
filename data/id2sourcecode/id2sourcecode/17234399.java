        private void autoLogin(HotspotStatus hsStatus, WifiManager wifiMan, boolean hsStateChanged) {
            int iconResId = R.drawable.hsli_red;
            String msg = "unknown hsStatus: " + hsStatus;
            Intent intent = new Intent(HotspotLogin_Service.this, HotspotLoginActivity.class);
            switch(hsStatus) {
                case WIFIDISABLED:
                    {
                        iconResId = R.drawable.hsli_red;
                        msg = "wifi is disabled, please enable in settings";
                        intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        sleepSeconds = WIFIDISABLED_POLL_INTERVAL;
                        if (hsStateChanged) HotspotUtilities.playSound();
                        break;
                    }
                case SSIDNOTFOUND:
                    {
                        iconResId = R.drawable.hsli_red;
                        msg = "waiting for SSID '" + params.getSSID() + "'";
                        sleepSeconds = SSIDNOTFOUND_POLL_INTERVAL;
                        if (System.currentTimeMillis() - lastSsidScan > RESCAN_INTERVAL * 1000) {
                            wifiMan.startScan();
                            lastSsidScan = System.currentTimeMillis();
                        }
                        if (hsStateChanged) HotspotUtilities.playSound();
                        break;
                    }
                case NETCONFIGNOTFOUND:
                    {
                        iconResId = R.drawable.hsli_yellow;
                        msg = "no config found for '" + params.getSSID() + "', please connect manually";
                        intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        sleepSeconds = NETCONFIGNOTFOUND_POLL_INTERVAL;
                        if (hsStateChanged) HotspotUtilities.playSound();
                        break;
                    }
                case DISCONNECTED:
                    {
                        iconResId = R.drawable.hsli_yellow;
                        String connectResult = HotspotUtilities.connect(params, wifiMan);
                        msg = "connect to '" + params.getSSID() + "': " + connectResult;
                        sleepSeconds = DISCONNECTED_POLL_INTERVAL;
                        break;
                    }
                case CONNECTING:
                    {
                        String info = " - state=?";
                        try {
                            WifiInfo conInfo = wifiMan.getConnectionInfo();
                            if (conInfo != null) {
                                SupplicantState supState = conInfo.getSupplicantState();
                                if (supState != null) info = " - state=" + supState.toString();
                            }
                        } catch (Exception ignore) {
                        }
                        iconResId = R.drawable.hsli_yellow;
                        msg = "connecting to '" + params.getSSID() + "'" + info;
                        sleepSeconds = CONNECTING_POLL_INTERVAL;
                        break;
                    }
                case SWITCHTOWIFI:
                    {
                        iconResId = R.drawable.hsli_yellow;
                        msg = "waiting for WIFI to become active";
                        sleepSeconds = SWITCHTOWIFI_POLL_INTERVAL;
                        break;
                    }
                case HOTSPOTCONNECTED:
                    {
                        if (cntFailedLogins == -1) {
                            iconResId = R.drawable.hsli_yellow;
                            msg = "login failed, wrong username/password";
                            sleepSeconds = LOGINFAILED_POLL_INTERVAL;
                            break;
                        }
                        if (cntFailedLogins >= MAX_FAILED_LOGINS) {
                            iconResId = R.drawable.hsli_yellow;
                            msg = "login failed, max retry (" + MAX_FAILED_LOGINS + ") reached";
                            sleepSeconds = LOGINFAILED_POLL_INTERVAL;
                            break;
                        }
                        String loginResult = HotspotUtilities.login(params, login, pw);
                        if (loginResult.equals("OK")) {
                            cntFailedLogins = 0;
                            iconResId = R.drawable.hsli_yellow;
                            msg = "login succeeded";
                            sleepSeconds = LOGINOK_POLL_INTERVAL;
                            break;
                        }
                        if (loginResult.equals("WRONG")) {
                            cntFailedLogins = -1;
                            iconResId = R.drawable.hsli_yellow;
                            msg = "login failed, wrong username/password";
                            sleepSeconds = LOGINFAILED_POLL_INTERVAL;
                            break;
                        }
                        cntFailedLogins += 1;
                        iconResId = R.drawable.hsli_yellow;
                        msg = "unknown login response";
                        sleepSeconds = LOGINERROR_POLL_INTERVAL;
                        break;
                    }
                case INTERNET:
                    {
                        iconResId = R.drawable.hsli_green;
                        msg = "";
                        sleepSeconds = INTERNET_POLL_INTERVAL;
                        if (hsStateChanged) HotspotUtilities.playSound();
                        break;
                    }
                case UNKNOWNHOTSPOT:
                    {
                        iconResId = R.drawable.hsli_red;
                        msg = "unknown hotspot type";
                        sleepSeconds = ERROR_POLL_INTERVAL;
                        break;
                    }
                case ERROR:
                    {
                        HotspotUtilities.disconnect(params, wifiMan);
                        iconResId = R.drawable.hsli_red;
                        msg = "error querying hotspot state";
                        sleepSeconds = ERROR_POLL_INTERVAL;
                        break;
                    }
            }
            if ((hsStatus != HotspotStatus.WIFIDISABLED) && (hsStatus != HotspotStatus.SSIDNOTFOUND) && (hsStatus != HotspotStatus.NETCONFIGNOTFOUND) && (hsStatus != HotspotStatus.INTERNET)) {
                HotspotUtilities.playSound();
            }
            showNotification(iconResId, msg, intent);
        }
