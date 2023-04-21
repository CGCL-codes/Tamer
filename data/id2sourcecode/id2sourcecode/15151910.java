    public void loop() {
        try {
            if (play != null && start != null) {
                as = constructor.newInstance(new Object[] { url.openStream() });
                Class AudioClass = Class.forName("sun.audio.AudioStream");
                Method getData = AudioClass.getMethod("getData", new Class[] {});
                Object data = getData.invoke(as, new Object[] {});
                Class CASClass = Class.forName("sun.audio.ContinuousAudioDataStream");
                Constructor constructor = CASClass.getConstructor(new Class[] { Class.forName("sun.audio.AudioData") });
                cas = constructor.newInstance(new Object[] { data });
                start.invoke(play, new Object[] { cas });
                return;
            }
            if (au != null) au.loop();
        } catch (Exception ex) {
        }
    }
