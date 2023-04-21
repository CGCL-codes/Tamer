    public static void call(String identifier, String context) {
        Integer behaviour = (Integer) behaviour_mapping.get(identifier);
        if (behaviour == null) {
            throw new IllegalArgumentException("unknown deprecated call identifier: " + identifier);
        }
        int b = behaviour.intValue();
        if (b == IGNORE) {
            return;
        }
        boolean persistent_notify = b == NOTIFY_ONCE;
        boolean notify = b != DIE;
        boolean raise_error = (b == NOTIFY_AND_DIE || b == DIE);
        String persistent_id = context + ":" + identifier;
        if (notify && !instance_warnings.contains(context) && (!persistent_notify || !persistent_warnings.contains(persistent_id))) {
            instance_warnings.add(context);
            if (!persistent_notify && persistent_warnings.remove(persistent_id)) {
                COConfigurationManager.setParameter(CONFIG_KEY, new StringListImpl(persistent_warnings));
            }
            synchronized (PluginDeprecation.class) {
                if (model == null) {
                    final PluginInterface pi = PluginInitializer.getDefaultInterface();
                    model = pi.getUIManager().createBasicPluginViewModel(MessageText.getString("PluginDeprecation.view"));
                    model.getStatus().setVisible(false);
                    model.getProgress().setVisible(false);
                    model.getActivity().setVisible(false);
                    model.getLogArea().appendText(MessageText.getString("PluginDeprecation.log.start", new String[] { Constants.isCVSVersion() ? FORUM_BETA_LINK : FORUM_STABLE_LINK }));
                    UIManagerListener uiml = new UIManagerListener() {

                        public void UIAttached(UIInstance inst) {
                            if (inst instanceof UISWTInstance) {
                                ((UISWTInstance) inst).openView(model);
                                pi.getUIManager().removeUIListener(this);
                            }
                        }

                        public void UIDetached(UIInstance inst) {
                        }
                    };
                    pi.getUIManager().addUIListener(uiml);
                }
                String log_details = MessageText.getString("PluginDeprecation.log.details", new String[] { identifier, context, Debug.getStackTrace(false, false) });
                model.getLogArea().appendText(log_details);
                if (channel == null) {
                    channel = PluginInitializer.getDefaultInterface().getLogger().getChannel("PluginDeprecation");
                }
                channel.logAlert(LoggerChannel.LT_WARNING, MessageText.getString("PluginDeprecation.alert"));
                Debug.out(new PluginDeprecationException("Deprecated plugin call - " + persistent_id).fillInStackTrace());
            }
            if (persistent_notify) {
                persistent_warnings.add(persistent_id);
                COConfigurationManager.setParameter(CONFIG_KEY, new StringListImpl(persistent_warnings));
            }
        }
        if (raise_error) {
            throw new PluginDeprecationException(persistent_id);
        }
    }
