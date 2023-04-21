    public boolean open(String filename) throws IOException {
        try {
            dis = new DataInputStream(new FileInputStream(filename));
            info = new JSndInfo();
            if (dis == null) return false;
            byte[] bInfo = new byte[44];
            dis.read(bInfo, 0, 44);
            String id = new String(bInfo, 0, 4);
            String typ = new String(bInfo, 8, 4);
            String fmt = new String(bInfo, 12, 4);
            if (!id.equals("RIFF") || !typ.equals("WAVE") || !fmt.startsWith("fmt")) return false;
            info.setChannels(tools.bytesToInt(bInfo, 22, 2));
            info.setSamplerate(tools.bytesToInt(bInfo, 24, 4));
            int af = tools.bytesToInt(bInfo, 20, 2);
            int size = tools.bytesToInt(bInfo, 16, 4);
            int bps = tools.bytesToInt(bInfo, 34, 2);
            info.setBitsPerSamples(bps);
            int ssize = tools.bytesToInt(bInfo, 40, 4);
            info.setFrames(ssize / (bps >> 3) / info.getChannels());
            int fm = JSndFileFormat.JSND_FORMAT_WAV;
            int sm = tools.bytesToInt(bInfo, 20, 2);
            info.setBlockalign(tools.bytesToInt(bInfo, 32, 2));
            if (sm == 16) {
                switch(bps) {
                    case 8:
                        fm |= JSndFileFormat.JSND_FORMAT_PCM_S8;
                        break;
                    case 16:
                        fm |= JSndFileFormat.JSND_FORMAT_PCM_16;
                        break;
                    case 24:
                        fm |= JSndFileFormat.JSND_FORMAT_PCM_24;
                        break;
                    case 32:
                        fm |= JSndFileFormat.JSND_FORMAT_PCM_32;
                        break;
                    default:
                        break;
                }
                info.setFormat(sm);
            }
            info.setSections((double) info.getFrames() / info.getSamplerate());
            return true;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
