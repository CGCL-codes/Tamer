            public void valueGeneratorUpdate(int rv) {
                if (seq != null) {
                    seq.sendVol(lp.getPart().getChannel() - 1, rv);
                    lp.getPart().setVolume(rv);
                }
            }
