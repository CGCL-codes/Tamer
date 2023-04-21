    public void setTarget(ScrEvent e) {
        target = e;
        if (e != null) {
            timeEditor.setValue(e.getTime());
            pitchEditor.setValue(e.getPitch());
            durationEditor.setValue(e.getDuration());
            velocityEditor.setValue(e.getVelocity());
            channelEditor.setValue(e.getChannel());
        } else {
            timeEditor.setValue("");
            pitchEditor.setValue("");
            durationEditor.setValue("");
            velocityEditor.setValue("");
            channelEditor.setValue("");
        }
    }
