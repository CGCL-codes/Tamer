        void implOpen(AudioFormat format, int bufferSize) throws LineUnavailableException {
            if (Printer.trace) Printer.trace(">> DirectDL: implOpen(" + format + ", " + bufferSize + " bytes)");
            Toolkit.isFullySpecifiedAudioFormat(format);
            if (!isSource) {
                JSSecurityManager.checkRecordPermission();
            }
            int encoding = PCM;
            if (format.getEncoding().equals(AudioFormat.Encoding.ULAW)) {
                encoding = ULAW;
            } else if (format.getEncoding().equals(AudioFormat.Encoding.ALAW)) {
                encoding = ALAW;
            }
            if (bufferSize <= AudioSystem.NOT_SPECIFIED) {
                bufferSize = (int) Toolkit.millis2bytes(format, DEFAULT_LINE_BUFFER_TIME);
            }
            DirectDLI ddli = null;
            if (info instanceof DirectDLI) {
                ddli = (DirectDLI) info;
            }
            if (isSource) {
                if (!format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                    controls = new Control[0];
                } else if (format.getChannels() > 2 || format.getSampleSizeInBits() > 16) {
                    controls = new Control[0];
                } else {
                    if (format.getChannels() == 1) {
                        controls = new Control[2];
                    } else {
                        controls = new Control[4];
                        controls[2] = balanceControl;
                        controls[3] = panControl;
                    }
                    controls[0] = gainControl;
                    controls[1] = muteControl;
                }
            }
            if (Printer.debug) Printer.debug("DirectAudioDevice: got " + controls.length + " controls.");
            hardwareFormat = format;
            softwareConversionSize = 0;
            if (ddli != null && !ddli.isFormatSupportedInHardware(format)) {
                AudioFormat newFormat = getSignOrEndianChangedFormat(format);
                if (ddli.isFormatSupportedInHardware(newFormat)) {
                    hardwareFormat = newFormat;
                    softwareConversionSize = format.getFrameSize() / format.getChannels();
                    if (Printer.debug) {
                        Printer.debug("DirectAudioDevice: softwareConversionSize " + softwareConversionSize + ":");
                        Printer.debug("  from " + format);
                        Printer.debug("  to   " + newFormat);
                    }
                }
            }
            bufferSize = ((int) bufferSize / format.getFrameSize()) * format.getFrameSize();
            synchronized (lockLast) {
                id = nOpen(mixerIndex, deviceID, isSource, encoding, hardwareFormat.getSampleRate(), hardwareFormat.getSampleSizeInBits(), hardwareFormat.getFrameSize(), hardwareFormat.getChannels(), hardwareFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED), hardwareFormat.isBigEndian(), bufferSize);
                if (id == 0) {
                    if (lastOpened != null && hardwareFormat.matches(lastOpened.hardwareFormat)) {
                        lastOpened.implClose();
                        lastOpened = null;
                        id = nOpen(mixerIndex, deviceID, isSource, encoding, hardwareFormat.getSampleRate(), hardwareFormat.getSampleSizeInBits(), hardwareFormat.getFrameSize(), hardwareFormat.getChannels(), hardwareFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED), hardwareFormat.isBigEndian(), bufferSize);
                    }
                    if (id == 0) {
                        throw new LineUnavailableException("line with format " + format + " not supported.");
                    }
                }
                lastOpened = this;
            }
            this.bufferSize = nGetBufferSize(id, isSource);
            if (this.bufferSize < 1) {
                this.bufferSize = bufferSize;
            }
            this.format = format;
            waitTime = (int) Toolkit.bytes2millis(format, this.bufferSize) / 4;
            if (waitTime < 10) {
                waitTime = 1;
            } else if (waitTime > 1000) {
                waitTime = 1000;
            }
            bytePosition = 0;
            stoppedWritten = false;
            doIO = false;
            calcVolume();
            if (Printer.trace) Printer.trace("<< DirectDL: implOpen() succeeded");
        }
