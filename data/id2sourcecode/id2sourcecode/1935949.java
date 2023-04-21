    public int getChannelSource(short address) {
        int sources;
        if (faderValues[address] >= 0) {
            if (submasterValues[address] > 0) {
                if (cueValues[address] > 0) {
                    if (faderValues[address] >= submasterValues[address]) {
                        if (submasterValues[address] >= cueValues[address]) sources = FADER_SOURCE * 100 + SUBMASTER_SOURCE * 10 + CUE_SOURCE; else sources = FADER_SOURCE * 100 + CUE_SOURCE * 10 + SUBMASTER_SOURCE;
                    } else {
                        if (submasterValues[address] >= cueValues[address]) {
                            sources = FADER_SOURCE * 100 + SUBMASTER_SOURCE * 10 + CUE_SOURCE;
                        } else {
                            sources = CUE_SOURCE * 100 + SUBMASTER_SOURCE * 10 + FADER_SOURCE;
                        }
                    }
                } else {
                    sources = FADER_SOURCE * 10 + SUBMASTER_SOURCE;
                }
            } else {
                if (cueValues[address] > 0) {
                    sources = FADER_SOURCE * 10 + CUE_SOURCE;
                } else {
                    sources = FADER_SOURCE;
                }
            }
        } else {
            if (submasterValues[address] > 0) {
                if (cueValues[address] > 0) {
                    if (submasterValues[address] >= cueValues[address]) sources = SUBMASTER_SOURCE * 10 + CUE_SOURCE; else sources = CUE_SOURCE * 10 + SUBMASTER_SOURCE;
                } else {
                    sources = SUBMASTER_SOURCE;
                }
            } else {
                if (cueValues[address] > 0) sources = CUE_SOURCE; else sources = 0;
            }
        }
        if (faderValues[address] == -100) sources = 10 * sources + FADER_SOURCE;
        if (cueValues[address] == -100) sources = 10 * sources + CUE_SOURCE;
        return sources;
    }
