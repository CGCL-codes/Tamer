    public boolean repairCanvasTraceFormat() {
        boolean handled = false;
        try {
            handled = true;
            InkTraceFormat format = doc.getInk().getCurrentContext().getCanvasTraceFormat();
            InkTraceFormat new_format = format.clone(doc.getInk());
            new_format.getChannel(ChannelName.T).setUnits("s");
            new_format.getChannel(ChannelName.F).setMax("255");
            new_format.getChannel(ChannelName.F).setMin("0");
            new_format.getChannel(ChannelName.F).setIntermittent(false);
            new_format.setId("inkAnnoCanvasFormat");
            new_format.setFinal();
            doc.getInk().getCurrentContext().getCanvas().setInkTraceFormat(new_format);
            InkTraceFormat logitechFormat = ((InkTraceFormat) doc.getInk().getDefinitions().get("Logitechformat")).clone(doc.getInk());
            logitechFormat.getChannel(ChannelName.T).setUnits("s");
            logitechFormat.getChannel(ChannelName.F).setMax("255");
            logitechFormat.getChannel(ChannelName.F).setMin("0");
            doc.getInk().getDefinitions().remove("Logitechformat");
            logitechFormat.setId("Logitechformat");
            logitechFormat.setFinal();
            InkInkSource source = doc.getInk().getCurrentContext().getInkSource();
            if (source != null) {
                source.setTraceFormat(logitechFormat);
            } else {
                doc.getInk().getDefinitions().enterElement(logitechFormat);
            }
            if (doc.getInk().containsAnnotation("PenId")) {
                source.setSerialNo(doc.getInk().getAnnotation("PenId"));
            }
            source.setSampleRate(0.013);
            source.setSampleRateUniform(false);
        } catch (InkMLComplianceException e) {
        }
        return handled;
    }
