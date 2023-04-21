    private void appendSinfoVariables(String retVal) {
        Matcher matcher = sinfoPattern.matcher(retVal);
        if (sinfoPattern != null) {
            while (matcher.find()) {
                String key = matcher.group();
                String result = key.substring(1, key.length() - 1);
                String objectName = matcher.group(1);
                String prop = matcher.group(2);
                CFileIn cfilein;
                try {
                    cfilein = (CFileIn) cm.getStateData().get(objectName);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                String val = null;
                if (prop.equals("sr")) {
                    val = Float.toString(cfilein.getSampleRate());
                } else if (prop.equals("frames")) {
                    val = Integer.toString(cfilein.getFrames());
                } else if (prop.equals("dur")) {
                    val = Float.toString(cfilein.getDuration());
                } else if (prop.equals("chn")) {
                    val = Integer.toString(cfilein.getChannels());
                } else {
                    String errMessage = "[" + BlueSystem.getString("message.error") + "] - " + BlueSystem.getString("ceciliaModule.propNotFound") + " ";
                    System.err.println(errMessage + prop);
                    break;
                }
                if (val == null) {
                    String errMessage = "[" + BlueSystem.getString("message.error") + "] - " + BlueSystem.getString("ceciliaModule.valueNotFound") + " ";
                    System.err.println(errMessage + prop);
                    break;
                }
                ceciliaVariables.put(result, val);
            }
        }
    }
