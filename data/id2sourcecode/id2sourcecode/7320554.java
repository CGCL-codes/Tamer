    public String process() {
        DecimalFormat nf = new DecimalFormat("#####.#########");
        String indices = String.valueOf(indexList.size());
        String firstIndex = "";
        for (int i = 0; i < indexList.size(); i++) {
            Integer I = indexList.get(i);
            if (i == 0) {
                firstIndex = I.toString();
            }
            indices += (" " + I.toString());
        }
        String line = null;
        if (tag == null) {
            return line;
        } else {
            if (tag.equals("param")) {
                System.out.println("now tag = param");
                String dim = String.valueOf(1);
                String pvFieldSet = "";
                String pvFieldRB = "";
                Collection<String> handles = node.getHandles();
                System.out.println("handles.size() = " + handles.size());
                for (Iterator<String> handleIter = handles.iterator(); handleIter.hasNext(); ) {
                    String handle = handleIter.next();
                    Channel channel = node.getChannel(handle);
                    if (channel != null) {
                        if (handle.equals("fieldSet")) {
                            pvFieldSet = channel.channelName();
                        } else if (handle.equals("fieldRB")) {
                            pvFieldRB = " " + "rbName=" + channel.channelName();
                        }
                        System.out.println("handle = " + handle);
                    }
                }
                if (!pvFieldSet.equals("")) {
                    line = tag + " " + pvFieldSet + " " + indices + " " + dim + pvFieldRB;
                }
            } else if (tag.equals("quad")) {
                String dim = String.valueOf(1);
                String pvFieldSet = "";
                String pvFieldRB = "";
                Collection<String> handles = node.getHandles();
                for (Iterator<String> handleIter = handles.iterator(); handleIter.hasNext(); ) {
                    String handle = handleIter.next();
                    Channel channel = node.getChannel(handle);
                    if (channel != null) {
                        if (handle.equals("fieldSet")) {
                            pvFieldSet = channel.channelName();
                        } else if (handle.equals("fieldRB")) {
                            pvFieldRB = " " + channel.channelName();
                        }
                        System.out.println("handle = " + handle);
                    } else {
                        System.out.println("channel == null");
                    }
                }
                if (!pvFieldSet.equals("")) {
                    line = tag + " " + pvFieldSet + " " + indices + " " + dim + pvFieldRB;
                }
            } else if (tag.equals("bpm")) {
                String pvX = "";
                String pvY = "";
                Collection<String> handles = node.getHandles();
                for (Iterator<String> handleIter = handles.iterator(); handleIter.hasNext(); ) {
                    String handle = handleIter.next();
                    Channel channel = node.getChannel(handle);
                    if (channel != null) {
                        if (handle.equals("xAvg")) {
                            pvX = channel.channelName();
                        } else if (handle.equals("yAvg")) {
                            pvY = channel.channelName();
                        }
                    }
                }
                if (!pvX.equals("")) {
                    line = tag + " " + pvX + " " + firstIndex + " " + "xAvg";
                }
                if (!pvY.equals("")) {
                    if (!pvX.equals("")) {
                        line += "\n" + tag + " " + pvY + " " + firstIndex + " " + "yAvg";
                    } else {
                        line = tag + " " + pvY + " " + firstIndex + " " + "yAvg";
                    }
                }
            } else if (tag.equals("rebuncher")) {
                String pvAmp = "";
                String pvPhase = "";
                Collection<String> handles = node.getHandles();
                for (Iterator<String> handleIter = handles.iterator(); handleIter.hasNext(); ) {
                    String handle = handleIter.next();
                    Channel channel = node.getChannel(handle);
                    if (channel != null) {
                        if (handle.equals("cavAmpSet")) {
                            pvAmp = channel.channelName();
                        } else if (handle.equals("cavPhaseSet")) {
                            pvPhase = channel.channelName();
                        }
                    }
                }
                if (!pvAmp.equals("")) {
                    line = tag + " " + pvAmp + " " + firstIndex + " " + "cavAmpSet";
                }
                if (!pvPhase.equals("")) {
                    if (!pvAmp.equals("")) {
                        line += "\n" + tag + " " + pvPhase + " " + firstIndex + " " + "cavPhaseSet";
                    } else {
                        line = tag + " " + pvPhase + " " + firstIndex + " " + "cavPhaseSet";
                    }
                }
            }
            return line;
        }
    }
