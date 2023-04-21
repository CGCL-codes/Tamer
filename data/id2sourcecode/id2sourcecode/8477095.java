    public void initialize(PluginInterface _plugin_interface) throws PluginException {
        plugin_interface = _plugin_interface;
        plugin_config = plugin_interface.getPluginconfig();
        Properties plugin_properties = plugin_interface.getPluginProperties();
        if (plugin_properties != null) {
            Object o = plugin_properties.get("plugin." + PR_ROOT_DIR.replaceAll(" ", "_"));
            if (o instanceof String) {
                properties.put(PR_ROOT_DIR, o);
            }
        }
        Boolean pr_enable = (Boolean) properties.get(PR_ENABLE);
        if (pr_enable != null) {
            CONFIG_ENABLE_DEFAULT = pr_enable.booleanValue();
        }
        Integer pr_port = (Integer) properties.get(PR_PORT);
        if (pr_port != null) {
            CONFIG_PORT_DEFAULT = pr_port.intValue();
        }
        String pr_bind_ip = (String) properties.get(PR_BIND_IP);
        if (pr_bind_ip != null) {
            CONFIG_BIND_IP_DEFAULT = pr_bind_ip.trim();
        }
        String pr_root_resource = (String) properties.get(PR_ROOT_RESOURCE);
        if (pr_root_resource != null) {
            CONFIG_ROOT_RESOURCE_DEFAULT = pr_root_resource;
        }
        String pr_root_dir = (String) properties.get(PR_ROOT_DIR);
        if (pr_root_dir != null) {
            CONFIG_ROOT_DIR_DEFAULT = pr_root_dir;
        }
        String pr_access = (String) properties.get(PR_ACCESS);
        if (pr_access != null) {
            CONFIG_ACCESS_DEFAULT = pr_access;
        }
        Boolean pr_hide_resource_config = (Boolean) properties.get(PR_HIDE_RESOURCE_CONFIG);
        log = (LoggerChannel) properties.get(PR_LOG);
        if (log == null) {
            log = plugin_interface.getLogger().getChannel("WebPlugin");
        }
        UIManager ui_manager = plugin_interface.getUIManager();
        view_model = (BasicPluginViewModel) properties.get(PR_VIEW_MODEL);
        if (view_model == null) {
            view_model = ui_manager.createBasicPluginViewModel(plugin_interface.getPluginName());
        }
        String sConfigSectionID = "plugins." + plugin_interface.getPluginID();
        view_model.setConfigSectionID(sConfigSectionID);
        view_model.getStatus().setText("Running");
        view_model.getActivity().setVisible(false);
        view_model.getProgress().setVisible(false);
        log.addListener(new LoggerChannelListener() {

            public void messageLogged(int type, String message) {
                view_model.getLogArea().appendText(message + "\n");
            }

            public void messageLogged(String str, Throwable error) {
                view_model.getLogArea().appendText(str + "\n");
                view_model.getLogArea().appendText(error.toString() + "\n");
            }
        });
        config_model = (BasicPluginConfigModel) properties.get(PR_CONFIG_MODEL);
        if (config_model == null) {
            String[] cm_params = (String[]) properties.get(PR_CONFIG_MODEL_PARAMS);
            if (cm_params == null || cm_params.length == 0) {
                config_model = ui_manager.createBasicPluginConfigModel(ConfigSection.SECTION_PLUGINS, sConfigSectionID);
            } else if (cm_params.length == 1) {
                config_model = ui_manager.createBasicPluginConfigModel(cm_params[0]);
            } else {
                config_model = ui_manager.createBasicPluginConfigModel(cm_params[0], cm_params[1]);
            }
        }
        boolean save_needed = false;
        if (!plugin_config.getPluginBooleanParameter(CONFIG_MIGRATED, false)) {
            plugin_config.setPluginParameter(CONFIG_MIGRATED, true);
            save_needed = true;
            plugin_config.setPluginParameter(CONFIG_PASSWORD_ENABLE, plugin_config.getBooleanParameter("Tracker Password Enable Web", CONFIG_PASSWORD_ENABLE_DEFAULT));
            plugin_config.setPluginParameter(CONFIG_USER, plugin_config.getStringParameter("Tracker Username", CONFIG_USER_DEFAULT));
            plugin_config.setPluginParameter(CONFIG_PASSWORD, plugin_config.getByteParameter("Tracker Password", CONFIG_PASSWORD_DEFAULT));
        }
        if (!plugin_config.getPluginBooleanParameter(PROPERTIES_MIGRATED, false)) {
            plugin_config.setPluginParameter(PROPERTIES_MIGRATED, true);
            Properties props = plugin_interface.getPluginProperties();
            if (props.getProperty("port", "").length() > 0) {
                save_needed = true;
                String prop_port = props.getProperty("port", "" + CONFIG_PORT_DEFAULT);
                String prop_protocol = props.getProperty("protocol", CONFIG_PROTOCOL_DEFAULT);
                String prop_home = props.getProperty("homepage", CONFIG_HOME_PAGE_DEFAULT);
                String prop_rootdir = props.getProperty("rootdir", CONFIG_ROOT_DIR_DEFAULT);
                String prop_rootres = props.getProperty("rootresource", CONFIG_ROOT_RESOURCE_DEFAULT);
                String prop_mode = props.getProperty("mode", CONFIG_MODE_DEFAULT);
                String prop_access = props.getProperty("access", CONFIG_ACCESS_DEFAULT);
                int prop_port_int = CONFIG_PORT_DEFAULT;
                try {
                    prop_port_int = Integer.parseInt(prop_port);
                } catch (Throwable e) {
                }
                plugin_config.setPluginParameter(CONFIG_PORT, prop_port_int);
                plugin_config.setPluginParameter(CONFIG_PROTOCOL, prop_protocol);
                plugin_config.setPluginParameter(CONFIG_HOME_PAGE, prop_home);
                plugin_config.setPluginParameter(CONFIG_ROOT_DIR, prop_rootdir);
                plugin_config.setPluginParameter(CONFIG_ROOT_RESOURCE, prop_rootres);
                plugin_config.setPluginParameter(CONFIG_MODE, prop_mode);
                plugin_config.setPluginParameter(CONFIG_ACCESS, prop_access);
                File props_file = new File(plugin_interface.getPluginDirectoryName(), "plugin.properties");
                PrintWriter pw = null;
                try {
                    File backup = new File(plugin_interface.getPluginDirectoryName(), "plugin.properties.bak");
                    props_file.renameTo(backup);
                    pw = new PrintWriter(new FileWriter(props_file));
                    pw.println("plugin.class=" + props.getProperty("plugin.class"));
                    pw.println("plugin.name=" + props.getProperty("plugin.name"));
                    pw.println("plugin.version=" + props.getProperty("plugin.version"));
                    pw.println("plugin.id=" + props.getProperty("plugin.id"));
                    pw.println("");
                    pw.println("# configuration has been migrated to plugin config - see view->config->plugins");
                    pw.println("# in the SWT user interface");
                    log.logAlert(LoggerChannel.LT_INFORMATION, plugin_interface.getPluginName() + " - plugin.properties settings migrated to plugin configuration.");
                } catch (Throwable e) {
                    Debug.printStackTrace(e);
                    log.logAlert(LoggerChannel.LT_ERROR, plugin_interface.getPluginName() + " - plugin.properties settings migration failed.");
                } finally {
                    if (pw != null) {
                        pw.close();
                    }
                }
            }
        }
        if (save_needed) {
            plugin_config.save();
        }
        Boolean disablable = (Boolean) properties.get(PR_DISABLABLE);
        final BooleanParameter param_enable;
        if (disablable != null && disablable) {
            param_enable = config_model.addBooleanParameter2(CONFIG_ENABLE, "webui.enable", CONFIG_ENABLE_DEFAULT);
            plugin_enabled = param_enable.getValue();
        } else {
            param_enable = null;
            plugin_enabled = true;
        }
        param_port = config_model.addIntParameter2(CONFIG_PORT, "webui.port", CONFIG_PORT_DEFAULT);
        param_bind = config_model.addStringParameter2(CONFIG_BIND_IP, "webui.bindip", CONFIG_BIND_IP_DEFAULT);
        param_protocol = config_model.addStringListParameter2(CONFIG_PROTOCOL, "webui.protocol", new String[] { "http", "https" }, CONFIG_PROTOCOL_DEFAULT);
        ParameterListener update_server_listener = new ParameterListener() {

            public void parameterChanged(Parameter param) {
                setupServer();
            }
        };
        param_port.addListener(update_server_listener);
        param_bind.addListener(update_server_listener);
        param_protocol.addListener(update_server_listener);
        p_upnp_enable = config_model.addBooleanParameter2(CONFIG_UPNP_ENABLE, "webui.upnpenable", CONFIG_UPNP_ENABLE_DEFAULT);
        p_upnp_enable.addListener(new ParameterListener() {

            public void parameterChanged(Parameter param) {
                setupUPnP();
            }
        });
        plugin_interface.addListener(new PluginListener() {

            public void initializationComplete() {
                setupUPnP();
            }

            public void closedownInitiated() {
            }

            public void closedownComplete() {
            }
        });
        final String p_sid = (String) properties.get(PR_PAIRING_SID);
        final LabelParameter pairing_info;
        final BooleanParameter pairing_enable;
        final HyperlinkParameter pairing_test;
        final HyperlinkParameter connection_test;
        if (p_sid != null) {
            final PairingManager pm = PairingManagerFactory.getSingleton();
            pairing_info = config_model.addLabelParameter2("webui.pairing.info." + (pm.isEnabled() ? "y" : "n"));
            pairing_enable = config_model.addBooleanParameter2(CONFIG_PAIRING_ENABLE, "webui.pairingenable", CONFIG_PAIRING_ENABLE_DEFAULT);
            if (!plugin_config.getPluginBooleanParameter(PAIRING_MIGRATED, false)) {
                boolean has_pw_enabled = plugin_config.getPluginBooleanParameter(CONFIG_PASSWORD_ENABLE, CONFIG_PASSWORD_ENABLE_DEFAULT);
                if (has_pw_enabled) {
                    plugin_config.setPluginParameter(CONFIG_PAIRING_AUTO_AUTH, false);
                }
                plugin_config.setPluginParameter(PAIRING_MIGRATED, true);
            }
            param_port_or = config_model.addIntParameter2(CONFIG_PORT_OVERRIDE, "webui.port.override", 0);
            param_auto_auth = config_model.addBooleanParameter2(CONFIG_PAIRING_AUTO_AUTH, "webui.pairing.autoauth", CONFIG_PAIRING_AUTO_AUTH_DEFAULT);
            param_auto_auth.addListener(new ParameterListener() {

                public void parameterChanged(Parameter param) {
                    if (pairing_enable.getValue() && pm.isEnabled()) {
                        setupAutoAuth();
                    }
                }
            });
            connection_test = config_model.addHyperlinkParameter2("webui.connectiontest", getConnectionTestURL(p_sid));
            pairing_test = config_model.addHyperlinkParameter2("webui.pairingtest", "http://remote.vuze.com/?sid=" + p_sid);
        } else {
            pairing_info = null;
            pairing_enable = null;
            param_auto_auth = null;
            param_port_or = null;
            pairing_test = null;
            connection_test = null;
        }
        config_model.createGroup("ConfigView.section.Pairing", new Parameter[] { pairing_info, pairing_enable, param_port_or, param_auto_auth, connection_test, pairing_test });
        config_model.createGroup("ConfigView.section.server", new Parameter[] { param_port, param_bind, param_protocol, p_upnp_enable });
        param_home = config_model.addStringParameter2(CONFIG_HOME_PAGE, "webui.homepage", CONFIG_HOME_PAGE_DEFAULT);
        param_rootdir = config_model.addStringParameter2(CONFIG_ROOT_DIR, "webui.rootdir", CONFIG_ROOT_DIR_DEFAULT);
        param_rootres = config_model.addStringParameter2(CONFIG_ROOT_RESOURCE, "webui.rootres", CONFIG_ROOT_RESOURCE_DEFAULT);
        if (pr_hide_resource_config != null && pr_hide_resource_config.booleanValue()) {
            param_home.setVisible(false);
            param_rootdir.setVisible(false);
            param_rootres.setVisible(false);
        } else {
            ParameterListener update_resources_listener = new ParameterListener() {

                public void parameterChanged(Parameter param) {
                    setupResources();
                }
            };
            param_home.addListener(update_resources_listener);
            param_rootdir.addListener(update_resources_listener);
            param_rootres.addListener(update_resources_listener);
        }
        LabelParameter a_label1 = config_model.addLabelParameter2("webui.mode.info");
        StringListParameter param_mode = config_model.addStringListParameter2(CONFIG_MODE, "webui.mode", new String[] { "full", "view" }, CONFIG_MODE_DEFAULT);
        LabelParameter a_label2 = config_model.addLabelParameter2("webui.access.info");
        param_access = config_model.addStringParameter2(CONFIG_ACCESS, "webui.access", CONFIG_ACCESS_DEFAULT);
        param_access.addListener(new ParameterListener() {

            public void parameterChanged(Parameter param) {
                setupAccess();
            }
        });
        pw_enable = config_model.addBooleanParameter2(CONFIG_PASSWORD_ENABLE, "webui.passwordenable", CONFIG_PASSWORD_ENABLE_DEFAULT);
        p_user_name = config_model.addStringParameter2(CONFIG_USER, "webui.user", CONFIG_USER_DEFAULT);
        p_password = config_model.addPasswordParameter2(CONFIG_PASSWORD, "webui.password", PasswordParameter.ET_SHA1, CONFIG_PASSWORD_DEFAULT);
        pw_enable.addEnabledOnSelection(p_user_name);
        pw_enable.addEnabledOnSelection(p_password);
        ParameterListener auth_change_listener = new ParameterListener() {

            public void parameterChanged(Parameter param) {
                if (param_auto_auth != null) {
                    if (!setting_auto_auth) {
                        log("Disabling pairing auto-authentication as overridden by user");
                        param_auto_auth.setValue(false);
                    }
                }
            }
        };
        p_user_name.addListener(auth_change_listener);
        p_password.addListener(auth_change_listener);
        pw_enable.addListener(auth_change_listener);
        config_model.createGroup("webui.group.access", new Parameter[] { a_label1, param_mode, a_label2, param_access, pw_enable, p_user_name, p_password });
        if (p_sid != null) {
            final PairingManager pm = PairingManagerFactory.getSingleton();
            pairing_enable.addListener(new ParameterListener() {

                public void parameterChanged(Parameter param) {
                    boolean enabled = pairing_enable.getValue();
                    param_auto_auth.setEnabled(pm.isEnabled() && enabled);
                    param_port_or.setEnabled(pm.isEnabled() && enabled);
                    boolean test_ok = pm.isEnabled() && pairing_enable.getValue() && pm.peekAccessCode() != null && !pm.hasActionOutstanding();
                    pairing_test.setEnabled(test_ok);
                    connection_test.setEnabled(test_ok);
                    setupPairing(p_sid, enabled);
                }
            });
            pairing_listener = new PairingManagerListener() {

                public void somethingChanged(PairingManager pm) {
                    pairing_info.setLabelKey("webui.pairing.info." + (pm.isEnabled() ? "y" : "n"));
                    if (plugin_enabled) {
                        pairing_enable.setEnabled(pm.isEnabled());
                        param_auto_auth.setEnabled(pm.isEnabled() && pairing_enable.getValue());
                        param_port_or.setEnabled(pm.isEnabled() && pairing_enable.getValue());
                        boolean test_ok = pm.isEnabled() && pairing_enable.getValue() && pm.peekAccessCode() != null && !pm.hasActionOutstanding();
                        pairing_test.setEnabled(test_ok);
                        connection_test.setEnabled(test_ok);
                    }
                    connection_test.setHyperlink(getConnectionTestURL(p_sid));
                    setupPairing(p_sid, pairing_enable.getValue());
                }
            };
            pairing_listener.somethingChanged(pm);
            pm.addListener(pairing_listener);
            setupPairing(p_sid, pairing_enable.getValue());
            ParameterListener update_pairing_listener = new ParameterListener() {

                public void parameterChanged(Parameter param) {
                    updatePairing(p_sid);
                    setupUPnP();
                }
            };
            param_port.addListener(update_pairing_listener);
            param_port_or.addListener(update_pairing_listener);
            param_protocol.addListener(update_pairing_listener);
        }
        if (param_enable != null) {
            final List<Parameter> changed_params = new ArrayList<Parameter>();
            if (!plugin_enabled) {
                Parameter[] params = config_model.getParameters();
                for (Parameter param : params) {
                    if (param == param_enable) {
                        continue;
                    }
                    if (param.isEnabled()) {
                        changed_params.add(param);
                        param.setEnabled(false);
                    }
                }
            }
            param_enable.addListener(new ParameterListener() {

                public void parameterChanged(Parameter e_p) {
                    plugin_enabled = ((BooleanParameter) e_p).getValue();
                    if (plugin_enabled) {
                        for (Parameter p : changed_params) {
                            p.setEnabled(true);
                        }
                    } else {
                        changed_params.clear();
                        Parameter[] params = config_model.getParameters();
                        for (Parameter param : params) {
                            if (param == e_p) {
                                continue;
                            }
                            if (param.isEnabled()) {
                                changed_params.add(param);
                                param.setEnabled(false);
                            }
                        }
                    }
                    setupServer();
                    setupUPnP();
                    if (p_sid != null) {
                        setupPairing(p_sid, pairing_enable.getValue());
                    }
                }
            });
        }
        setupResources();
        setupAccess();
        setupServer();
    }
