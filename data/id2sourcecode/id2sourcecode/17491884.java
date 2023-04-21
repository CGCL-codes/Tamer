    public static void main(String[] args) {
        XMPPConnection.DEBUG_ENABLED = true;
        XMPPConnection connection = new XMPPConnection("127.0.0.1");
        try {
            connection.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        try {
            connection.login("server", "7Dj3S", "Smack");
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        connection.DEBUG_ENABLED = true;
        XMPPBean gotThereBeanPrototype = new GoThereBean();
        XMPPBean joinGameBeanPrototype = new JoinGameBean();
        XMPPBean startRoundBeanPrototype = new StartRoundBean();
        XMPPBean uncoverCardBeanPrototype = new UncoverCardBean();
        XMPPBean keepAliveBeanPrototype = new KeepAliveBean();
        (new BeanProviderAdapter(gotThereBeanPrototype)).addToProviderManager();
        (new BeanProviderAdapter(joinGameBeanPrototype)).addToProviderManager();
        (new BeanProviderAdapter(startRoundBeanPrototype)).addToProviderManager();
        (new BeanProviderAdapter(uncoverCardBeanPrototype)).addToProviderManager();
        (new BeanProviderAdapter(keepAliveBeanPrototype)).addToProviderManager();
        PacketListener myListener = new PacketListener() {

            @Override
            public void processPacket(Packet packet) {
                NetworkFingerprintDAO dao = new NetworkFingerprintDAO();
                System.out.println("Packet empfangen: " + packet.toString());
                try {
                    if (packet instanceof BeanIQAdapter) {
                        XMPPBean b = ((BeanIQAdapter) packet).getBean();
                        if (b instanceof GoThereBean) {
                            GoThereBean bean = (GoThereBean) b;
                            if (bean.getType() == XMPPBean.TYPE_SET) {
                                System.out.println("GoThereBean vom typ SET empfangen");
                            } else if (bean.getType() == XMPPBean.TYPE_RESULT) {
                                System.out.println("GoThereBean vom typ GET empfangen");
                            } else if (bean.getType() == XMPPBean.TYPE_ERROR) {
                            }
                        } else if (b instanceof StartRoundBean) {
                            StartRoundBean bean = (StartRoundBean) b;
                            if (bean.getType() == XMPPBean.TYPE_SET) {
                                System.out.println("StartRoundBean vom typ SET empfangen");
                            } else if (bean.getType() == XMPPBean.TYPE_RESULT) {
                                System.out.println("StartRoundBean vom typ RESULT empfangen");
                            } else if (bean.getType() == XMPPBean.TYPE_ERROR) {
                                System.out.println("StartRoundBean vom typ ERROR empfangen");
                            }
                        } else if (b instanceof UncoverCardBean) {
                            UncoverCardBean bean = (UncoverCardBean) b;
                            System.out.println("UncoverCardBean empfangen");
                            if (bean.getType() == XMPPBean.TYPE_SET) {
                                System.out.println("UncoverCardBean vom typ SET empfangen");
                                NetworkFingerPrint fp = bean.getNetworkFingerPrint();
                                dao.addFingerprint(fp);
                                dao.close();
                            } else if (bean.getType() == XMPPBean.TYPE_RESULT) {
                                System.out.println("UncoverCardBean vom typ GET empfangen");
                            } else if (bean.getType() == XMPPBean.TYPE_ERROR) {
                                System.out.println("UncoverCardBean vom typ ERROR empfangen");
                            }
                        } else if (b instanceof JoinGameBean) {
                            JoinGameBean bean = (JoinGameBean) b;
                            if (bean.getType() == XMPPBean.TYPE_SET) {
                                System.out.println("JoinGameBean vom typ SET empfangen");
                                System.out.println(bean.toXML());
                            } else if (bean.getType() == XMPPBean.TYPE_RESULT) {
                                System.out.println("JoinGameBean vom typ GET empfangen");
                            } else if (bean.getType() == XMPPBean.TYPE_ERROR) {
                            }
                        } else if (b instanceof KeepAliveBean) {
                            KeepAliveBean bean = (KeepAliveBean) b;
                            if (bean.getType() == XMPPBean.TYPE_SET) {
                                System.out.println("KAB vom typ SET empfangen");
                                System.out.println(bean.toXML());
                            } else if (bean.getType() == XMPPBean.TYPE_RESULT) {
                                System.out.println("KAB vom typ GET empfangen");
                            } else if (bean.getType() == XMPPBean.TYPE_ERROR) {
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        connection.addPacketListener(myListener, new OrFilter(new BeanFilterAdapter(gotThereBeanPrototype), new OrFilter(new BeanFilterAdapter(uncoverCardBeanPrototype), new OrFilter(new BeanFilterAdapter(startRoundBeanPrototype), new BeanFilterAdapter(joinGameBeanPrototype)))));
        try {
            java.util.Date date = Calendar.getInstance().getTime();
            NetworkFingerPrint fp = new NetworkFingerPrint();
            fp.setPosition(new GeoPosition(121, 231, 4));
            Map<String, Integer> fps = new HashMap<String, Integer>();
            fps.put("id01", -10);
            fps.put("id02", -20);
            fps.put("id03", -30);
            fps.put("id04", -40);
            fp.setNetworkFingerPrint(fps);
            UncoverCardBean ucb = new UncoverCardBean("awe", fp, StartRoundBean.sdf.format(date), "hugo");
            ucb.setTo("alpha@141.30.203.90/MXA");
            ucb.setTo("server@141.30.203.90/Smack");
            ucb.setFrom(connection.getUser());
            ucb.setType(XMPPBean.TYPE_SET);
            System.out.println("HAARHH " + ucb.toXML());
            connection.sendPacket(new BeanIQAdapter(ucb));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JoinGameBean resultBean = new JoinGameBean();
        resultBean.setTo("reik@141.30.203.90/Client");
        resultBean.setFrom("Server");
        resultBean.setType(XMPPBean.TYPE_RESULT);
        System.out.println("JoinGameBean vom typ RESULT an " + "alpha@141.30.203.90/MXA" + " gesendet");
        System.out.println("Senden der Testbeans:");
        System.out.println("StartRoundBean gesendet");
        Map<Integer, Long> scores = new HashMap<Integer, Long>();
        scores.put(new Integer(1), new Long(1));
        StartRoundBean testbean1 = new StartRoundBean(true, StartRoundBean.sdf.format(Calendar.getInstance().getTime()), 3000, scores, "yadsjfa");
        testbean1.setTo("alpha@141.30.203.90/MXA");
        testbean1.setTo("server@141.30.203.90/Smack");
        testbean1.setFrom(connection.getUser());
        testbean1.setType(XMPPBean.TYPE_SET);
        System.out.println("StartRoundBean: " + testbean1.toXML());
        System.out.println("ID: " + testbean1.getId());
        connection.sendPacket(new BeanIQAdapter(testbean1));
        KeepAliveBean kab = new KeepAliveBean("ssdfad", new GeoPosition(23, 234, 5));
        kab.setTo("server@141.30.203.90/Smack");
        kab.setType(XMPPBean.TYPE_SET);
        connection.sendPacket(new BeanIQAdapter(kab));
        java.util.Date date = Calendar.getInstance().getTime();
        NetworkFingerPrint nfp = new NetworkFingerPrint();
        nfp.addFingerPrint("asfd", 123);
        UncoverCardBean ucb = new UncoverCardBean("awe", nfp, StartRoundBean.sdf.format(date), "hugo");
        ucb.setTo("alpha@141.30.203.90/MXA");
        ucb.setTo("server@141.30.203.90/Smack");
        ucb.setFrom(connection.getUser());
        ucb.setType(XMPPBean.TYPE_SET);
        System.out.println(ucb.toXML());
    }
