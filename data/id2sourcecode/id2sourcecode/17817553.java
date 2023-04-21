    public void loadCapabilities() {
        String request0 = serviceEndPoint + "?" + "SERVICE=" + service + "&" + "request=GetCapabilities&" + "SECTIONS=OperationsMetadata,Contents,ServiceIdentification&" + "ACCEPTFORMATS=text/xml&" + "ACCEPTVERSIONS=";
        String request1 = serviceEndPoint + "?" + "do=GetCapabilities&" + "SERVICE=" + service + "&" + "request=GetCapabilities&" + "SECTIONS=OperationsMetadata,Contents,ServiceIdentification&" + "ACCEPTFORMATS=text/xml&" + "ACCEPTVERSIONS=";
        String requests[] = new String[2];
        requests[0] = request0;
        requests[1] = request1;
        try {
            for (String request : requests) {
                for (int i = 0; i < acceptVersions.length; i++) {
                    if (i > 0) request += ",";
                    request += acceptVersions[i];
                }
                if (Navigator.isVerbose()) {
                    System.out.println("GetCapabilities Request: " + request);
                }
                URL url = new URL(request);
                version = VERSION_NOT_SPECIFIED;
                for (String tryVersion : this.acceptVersions) {
                    if (tryVersion.equals("0.4.1")) {
                        InputStream urlIn_041 = null;
                        try {
                            URLConnection urlc = url.openConnection();
                            urlc.setReadTimeout(Navigator.TIME_OUT);
                            if (getEncoding() != null) {
                                urlc.setRequestProperty("Authorization", "Basic " + getEncoding());
                            }
                            urlIn_041 = urlc.getInputStream();
                            if (urlIn_041 != null) {
                                org.gdi3d.xnavi.services.w3ds.x041.CapabilitiesLoader capLoader = new org.gdi3d.xnavi.services.w3ds.x041.CapabilitiesLoader(this);
                                capabilities = capLoader.load(urlIn_041);
                                version = VERSION_041;
                                if (Navigator.isVerbose()) {
                                    System.out.println("W3DS version is 0.4.1");
                                }
                            }
                        } catch (org.gdi3d.xnavi.services.w3ds.x041.WrongCapabilitiesVersionException wv) {
                            if (Navigator.isVerbose()) {
                                System.out.println("W3DS version is not 0.4.1");
                            }
                        } finally {
                            try {
                                urlIn_041.close();
                            } catch (Exception e) {
                            }
                        }
                    } else if (tryVersion.equals("0.4.0")) {
                        InputStream urlIn_040 = null;
                        try {
                            URLConnection urlc = url.openConnection();
                            urlc.setReadTimeout(Navigator.TIME_OUT);
                            if (getEncoding() != null) {
                                urlc.setRequestProperty("Authorization", "Basic " + getEncoding());
                            }
                            urlIn_040 = urlc.getInputStream();
                            if (urlIn_040 != null) {
                                org.gdi3d.xnavi.services.w3ds.x040.CapabilitiesLoader capLoader = new org.gdi3d.xnavi.services.w3ds.x040.CapabilitiesLoader(this);
                                capabilities = capLoader.load(urlIn_040);
                                version = VERSION_040;
                                if (Navigator.isVerbose()) {
                                    System.out.println("W3DS version is 0.4.0");
                                }
                            }
                        } catch (org.gdi3d.xnavi.services.w3ds.x040.WrongCapabilitiesVersionException wv) {
                            if (Navigator.isVerbose()) {
                                System.out.println("W3DS version is not 0.4.0");
                            }
                        } finally {
                            try {
                                urlIn_040.close();
                            } catch (Exception e) {
                            }
                        }
                    } else if (tryVersion.equals("0.3.0")) {
                        InputStream urlIn_030 = null;
                        try {
                            URLConnection urlc = url.openConnection();
                            urlc.setReadTimeout(Navigator.TIME_OUT);
                            if (getEncoding() != null) {
                                urlc.setRequestProperty("Authorization", "Basic " + getEncoding());
                            }
                            urlIn_030 = urlc.getInputStream();
                            if (urlIn_030 != null) {
                                org.gdi3d.xnavi.services.w3ds.x030.CapabilitiesLoader capLoader = new org.gdi3d.xnavi.services.w3ds.x030.CapabilitiesLoader(urlIn_030);
                                capabilities = capLoader.getCapabilities();
                                version = VERSION_030;
                                if (Navigator.isVerbose()) {
                                    System.out.println("W3DS version is 0.3.0");
                                }
                            }
                        } catch (org.gdi3d.xnavi.services.w3ds.x030.WrongCapabilitiesVersionException wv) {
                            if (Navigator.isVerbose()) {
                                System.out.println("W3DS version is not 0.3.0");
                            }
                        } finally {
                            try {
                                urlIn_030.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                    if (version != VERSION_NOT_SPECIFIED) {
                        break;
                    }
                }
                if (version != VERSION_NOT_SPECIFIED) {
                    break;
                }
            }
            if (version == VERSION_NOT_SPECIFIED) {
                capabilities = null;
                JOptionPane.showMessageDialog(null, "Problem connecting to W3DS. \n" + "Must be version 0.3.0 or 0.4.0. \n " + "Check console for details.", "W3DS Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NoRouteToHostException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while attempting to connect a socket \n" + "to a remote address and port. Typically, the remote host \n" + "cannot be reached because of an intervening firewall, \n" + "or if an intermediate router is down. ", "Connecting Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (java.lang.NullPointerException e) {
            JOptionPane.showMessageDialog(null, "No connection with the server.\n " + "(NullPointer) ???", "Connecting Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            capabilities = null;
            JOptionPane.showMessageDialog(null, "The connection with the server could not be produced.\n" + " Please, check your Internet connection or Internet settings. ", "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
