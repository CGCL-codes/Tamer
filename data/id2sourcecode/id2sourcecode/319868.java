        @Override
        public Void doInBackground() throws IOException, InterruptedException {
            double progress = 0;
            setProgress(0);
            double max = 0;
            try {
                max = video.getVideoSize();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            URL download = new URL(video.getDownloadURL());
            BufferedInputStream in = new BufferedInputStream(download.openStream());
            FileOutputStream fos = new FileOutputStream(filename);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            int oneChar;
            while ((oneChar = in.read()) != -1) {
                if (isCancelled()) {
                    setProgress(0);
                    break;
                }
                fos.write(oneChar);
                progress++;
                setProgress((int) (progress * 100 / max));
            }
            in.close();
            fos.close();
            bout.close();
            in.close();
            if (chkConvert.isSelected()) {
                COptions opt = options.get(cmbConvert.getSelectedIndex());
                cfilename = txtSave.getText() + File.separator;
                cfilename += video.getVideoTitle() + "." + options.get(cmbConvert.getSelectedIndex()).getExt();
                String acodec = opt.getAcodec();
                String channel = opt.getChannel();
                String frequency = opt.getFrequency();
                File source = new File(filename);
                File target = new File(cfilename);
                AudioAttributes audio = new AudioAttributes();
                audio.setCodec(acodec);
                audio.setChannels(Integer.parseInt(channel));
                audio.setSamplingRate(Integer.parseInt(frequency));
                VideoAttributes videoa = null;
                EncodingAttributes attrs = new EncodingAttributes();
                attrs.setFormat(opt.getExt());
                attrs.setAudioAttributes(audio);
                if (!opt.getCategory().equalsIgnoreCase("audio")) {
                    videoa = new VideoAttributes();
                    videoa.setCodec(opt.getVcodec());
                    if (opt.getTag() != null) {
                        videoa.setTag(opt.getTag());
                        ;
                    }
                    attrs.setVideoAttributes(videoa);
                }
                Encoder encoder = new Encoder();
                try {
                    encoder.encode(source, target, attrs);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    showError("Conversion failed. Illegal Argument Exception");
                } catch (InputFormatException e) {
                    e.printStackTrace();
                    showError("Conversion failed. Input Exception");
                } catch (EncoderException e) {
                    showError("Conversion failed. " + e.getMessage());
                    e.printStackTrace();
                }
                source.delete();
            }
            return null;
        }
