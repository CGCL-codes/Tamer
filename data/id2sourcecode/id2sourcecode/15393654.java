        public void dump(Writer writer) {
            PrintWriter pw = new PrintWriter(new BufferedWriter(writer));
            pw.println("#========== Wave ==========");
            pw.println("#Type: NULL");
            pw.println("#Sample_Rate: " + (int) audioFormat.getSampleRate());
            pw.println("#Num_of_Samples: " + samples.length / 2);
            pw.println("#Num_of_Channels: " + audioFormat.getChannels());
            if (samples != null) {
                for (int i = 0; i < samples.length; i += 2) {
                    pw.println(WaveUtils.bytesToShort(samples[i], samples[i + 1]));
                }
            }
            pw.flush();
        }
