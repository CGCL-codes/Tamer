    public String toJSON(DSM2Model model) {
        JSONObject jmodel = new JSONObject();
        jmodel.put("channels", toJSON(model.getChannels()));
        jmodel.put("nodes", toJSON(model.getNodes()));
        jmodel.put("gates", toJSON(model.getGates()));
        jmodel.put("reservoirs", toJSON(model.getReservoirs()));
        return model.toString();
    }
