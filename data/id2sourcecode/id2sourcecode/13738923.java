    public RolandFile loadFromFile(File sysexFile) throws IllegalArgumentException {
        RolandFile result;
        try {
            result = createFileInstance(mContainer, sysexFile);
        } catch (ConfigurationException e) {
            throw new IllegalArgumentException("Could not instanciate device.", e);
        }
        try {
            FileInputStream stream = new FileInputStream(sysexFile);
            FileChannel channel = stream.getChannel();
            ByteBuffer buffer = null;
            try {
                buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                int index = 0;
                while (buffer.hasRemaining()) {
                    if (buffer.get(index) == ((byte) SysexMessage.SYSTEM_EXCLUSIVE)) {
                        boolean f7Found = false;
                        index++;
                        int frameSize = 1;
                        while (!f7Found && buffer.hasRemaining()) {
                            if (buffer.get(index) == ((byte) SysexMessage.SPECIAL_SYSTEM_EXCLUSIVE)) {
                                f7Found = true;
                            }
                            frameSize++;
                            index++;
                        }
                        if (f7Found) {
                            byte[] frameData = new byte[frameSize];
                            buffer.get(frameData);
                            result.loadSysexData(frameData);
                        } else {
                            Locale language = mContainer.getSettings().getLocale();
                            throw new IllegalArgumentException(I18nUtil.getI18nString("error.missing.f7", language));
                        }
                    } else {
                        Locale language = mContainer.getSettings().getLocale();
                        throw new IllegalArgumentException(I18nUtil.getI18nString("error.missing.f0", language));
                    }
                }
            } finally {
                buffer = null;
                System.gc();
                stream.close();
                channel.close();
            }
        } catch (FileNotFoundException e) {
            Locale language = mContainer.getSettings().getLocale();
            throw new IllegalArgumentException(I18nUtil.getI18nString("error.sysex.fnf", language, sysexFile.getName()), e);
        } catch (IOException e) {
            Locale language = mContainer.getSettings().getLocale();
            throw new IllegalArgumentException(I18nUtil.getI18nString("error.sysex.io", language, sysexFile.getName()), e);
        }
        return result;
    }
