    private String convertBlueMixerOut(Mixer mixer, String arrangementId, String input, int nchnls) {
        if (input.indexOf("blueMixerOut") < 0) {
            return input;
        }
        StrBuilder buffer = new StrBuilder();
        String[] lines = NEW_LINES.split(input);
        for (String line : lines) {
            if (line.trim().startsWith("blueMixerOut")) {
                String argText = line.trim().substring(12);
                String[] args = argText.split(",");
                if (args[0].trim().matches("\".*\"")) {
                    if (mixer == null || !mixer.isEnabled()) {
                        buffer.append("outc ");
                        for (int i = 1; i < args.length; i++) {
                            if (i > 1) {
                                buffer.append(",");
                            }
                            buffer.append(args[i]);
                        }
                        buffer.append("\n");
                    } else {
                        String subChannelName = args[0].trim();
                        subChannelName = subChannelName.substring(1, subChannelName.length() - 1);
                        mixer.addSubChannelDependency(subChannelName);
                        for (int i = 1; i < nchnls + 1 && i < args.length; i++) {
                            String arg = args[i];
                            String var = Mixer.getSubChannelVar(subChannelName, i - 1);
                            buffer.append(var).append(" = ");
                            buffer.append(var).append(" + ").append(arg).append("\n");
                        }
                    }
                } else {
                    if (mixer == null || !mixer.isEnabled()) {
                        buffer.append(line.replaceAll("blueMixerOut", "outc"));
                        buffer.append("\n");
                    } else {
                        for (int i = 0; i < nchnls && i < args.length; i++) {
                            String arg = args[i];
                            String var = Mixer.getChannelVar(arrangementId, i);
                            buffer.append(var).append(" = ");
                            buffer.append(var).append(" + ").append(arg).append("\n");
                        }
                    }
                }
            } else {
                buffer.append(line).append("\n");
            }
        }
        return buffer.toString();
    }
