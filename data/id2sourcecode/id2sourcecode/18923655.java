    private boolean fillStandardCommands(boolean runTest) {
        BasicTab basic = dialog.getTabBasic();
        ICodec codec = basic.getvOut_codec();
        if (codec.isTwoPass()) {
            cmd2 = new ArrayList<String>(cmd);
            pass = 2;
        }
        int start = 0;
        if (runTest) {
            start = dialog.getTabTesting().addTestStartTime(cmd);
            if (pass == 2) {
                dialog.getTabTesting().addTestStartTime(cmd2);
            }
        }
        addCommand(opt + "i", infile);
        OptionsTab tabOptions = dialog.getTabOptions();
        File out = new File(outfile);
        if (out.exists()) {
            if (!tabOptions.getOverWriteButton_s().getSelection()) {
                MessageBox m = new MessageBox(dialog.getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                m.setMessage("OutFile " + out.getName() + " already exists! Press OK to Overwrite or Cancel");
                if (m.open() != SWT.OK) {
                    setError(true);
                    return false;
                } else {
                    addCommand("-y", null);
                }
            } else {
                addCommand("-y", null);
            }
        }
        if (pass == 2) {
            cmd.add("-pass");
            cmd.add("1");
            cmd2.add("-pass");
            cmd2.add("2");
        }
        if (runTest) {
            int duration = basic.getDuration();
            int testLength = dialog.getTabTesting().addTestRunTime(cmd);
            if (duration < testLength + start) {
                dialog.setMessage("Incorrect time of test. Test time is longer than input file.", IMessageProvider.ERROR, null);
                setError(true);
                return false;
            }
            if (pass == 2) {
                dialog.getTabTesting().addTestRunTime(cmd2);
            }
        }
        String acodec = basic.getaOut_codec();
        if (StringUtil.isEmpty(acodec)) {
            dialog.setMessage("Empty Audio Codec - please choose a codec or copy or skip", IMessageProvider.ERROR, null);
            setError(true);
            return false;
        }
        if (acodec.indexOf(IFFmpeg.SKIP) != -1) {
            addCommand("-an", null);
        } else {
            if (pass == 1) {
                addAudioOptions(cmd, basic, acodec);
            } else {
                cmd.add("-an");
                addAudioOptions(cmd2, basic, acodec);
            }
        }
        if (StringUtil.isEmpty(codec.getName())) {
            dialog.setMessage("Empty codec", IMessageProvider.ERROR, null);
            setError(true);
            return false;
        }
        if (codec.getName().indexOf(IFFmpeg.SKIP) != -1) {
            addCommand("-vn", null);
        } else {
            addCommand("-vcodec", codec.getName());
            if (!codec.getName().equals(IFFmpeg.COPY)) {
                String vOut_bitrate = basic.getvOut_bitrate();
                if (StringUtil.isEmpty(vOut_bitrate)) {
                    dialog.setMessage("Empty bitrate", IMessageProvider.ERROR, null);
                    setError(true);
                    return false;
                }
                addCommand("-b", vOut_bitrate);
                String width = basic.getvOut_width();
                String height = basic.getvOut_height();
                if (!StringUtil.isEmpty(width) || !StringUtil.isEmpty(height) || !width.equals(IFFmpeg.COPY) || !height.equals(IFFmpeg.COPY)) {
                    addCommand("-s", width + "x" + height);
                }
                String aspect = basic.getvOut_Aspect();
                if (isValidInput(aspect)) {
                    addCommand("-aspect", aspect);
                }
                String fps = basic.getvOut_fps();
                if (isValidInput(fps)) {
                    addCommand("-r", fps);
                }
            }
        }
        if (!StringUtil.isEmpty(codec.getPresetPath())) {
            String presetPath = null;
            String presetFirstPath = null;
            if (codec.isTwoPass()) {
                presetFirstPath = getPresetPath(codec.getFirstPasspresetPath());
                presetPath = getPresetPath(codec.getPresetPath());
                if (presetPath == null || presetFirstPath == null) {
                    dialog.setMessage("Preset not found", IMessageProvider.ERROR, null);
                    setError(true);
                    return false;
                }
                cmd.add("-fpre");
                cmd.add(presetFirstPath);
                cmd2.add("-fpre");
                cmd2.add(presetPath);
            } else {
                presetPath = getPresetPath(codec.getPresetPath());
                if (presetPath == null) {
                    dialog.setMessage("Preset not found", IMessageProvider.ERROR, null);
                    setError(true);
                    return false;
                }
                cmd.add("-fpre");
                cmd.add(presetPath);
            }
        } else if ((codec.getPreset() != null)) {
            if (codec.isTwoPass()) {
                codec.addfirstPasspresetToCmdList(cmd);
                codec.addPresetToCmdList(cmd2);
            } else {
                codec.addPresetToCmdList(cmd);
            }
        }
        if (addExpert) {
            dialog.getTabExpert().fillOutFileCmd(cmd);
        }
        if (codec.isTwoPass()) {
            tabOptions.addToCmdList(cmd);
            tabOptions.addToCmdList(cmd2);
            cmd.add(outfile);
            cmd2.add(outfile);
        } else {
            tabOptions.addToCmdList(cmd);
            cmd.add(outfile);
        }
        return true;
    }
