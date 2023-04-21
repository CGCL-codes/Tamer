    protected void actionsOnRun() throws Exception {
        if (gate != null) {
            String key = RECEIVED_OUTPUT_KEY;
            String field = RECORD_FIELD_KEY;
            if (gate.getChannel() != null) {
                key += "." + gate.getChannel();
                field += "." + gate.getChannel();
            }
            OutputSet set = addOutputSet(key);
            set.put(field, gate.getData());
            XmlDatabase.getInstance().addStat(user.getUserFqn(), "receiveAction", gate.getTopic());
        }
    }
