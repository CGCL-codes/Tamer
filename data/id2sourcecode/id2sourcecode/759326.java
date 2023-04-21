        public void run() {
            try {
                stream = AudioSystem.getAudioInputStream(sound);
                info = new DataLine.Info(SourceDataLine.class, stream.getFormat(), (int) (stream.getFrameLength() * stream.getFormat().getFrameSize()));
                line = (SourceDataLine) AudioSystem.getLine(info);
                mute_control = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
                mute_control.setValue(muted);
                line.open(stream.getFormat());
                line.start();
                num_read = 0;
                buf = new byte[line.getBufferSize()];
                offset = 0;
                while ((num_read = stream.read(buf, 0, buf.length)) >= 0) {
                    while (offset < num_read) {
                        offset += line.write(buf, offset, num_read - offset);
                    }
                    offset = 0;
                }
                line.drain();
                line.stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
