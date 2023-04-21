    void freq2time_adapt(byte blockType, Wnd_Shape wnd_shape, float[] freqIn, float[] timeBuff, float[] ftimeOut) throws IOException {
        int transBuffPtr = 0, timeBuffPtr = 0, destPtr = 0, srcPtr = 0;
        int i, j;
        switch(blockType) {
            case NORM_TYPE:
                blockType = ONLY_LONG;
                break;
            case START_TYPE:
                blockType = OLD_START;
                break;
            case SHORT_TYPE:
                blockType = EIGHT_SHORT;
                break;
            case STOP_TYPE:
                blockType = OLD_STOP;
                break;
            default:
                throw new IOException("dolby_adapt.c: Illegal block type " + blockType + " - aborting");
        }
        if (blockType == ONLY_LONG) {
            unfold(freqIn, srcPtr, transBuff, 1, BLOCK_LEN_LONG);
            ITransformBlock(transBuff, LONG_BLOCK, wnd_shape, timeBuff);
            transBuffPtr = 0;
            timeBuffPtr = 0;
            destPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = transBuff[transBuffPtr++] + timeBuff[timeBuffPtr++];
            }
            timeBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeBuff[timeBuffPtr++] = transBuff[transBuffPtr++];
            }
        } else if (blockType == SHORT_START) {
            unfold(freqIn, srcPtr, transBuff, 1, (BLOCK_LEN_SHORT + BLOCK_LEN_LONG) / 2);
            ITransformBlock(transBuff, START_BLOCK, wnd_shape, timeBuff);
            transBuffPtr = 0;
            timeBuffPtr = 0;
            destPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = transBuff[transBuffPtr++] + timeBuff[timeBuffPtr++];
            }
            timeBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_SHORT; i++) {
                timeBuff[timeBuffPtr++] = transBuff[transBuffPtr++];
            }
            srcPtr = ((BLOCK_LEN_LONG + BLOCK_LEN_SHORT) / 2);
            timeBuffPtr = 0;
            for (i = 0; i < N_SHORT_IN_START; i++) {
                unfold(freqIn, srcPtr, transBuff, 1, BLOCK_LEN_SHORT);
                srcPtr += BLOCK_LEN_SHORT;
                ITransformBlock(transBuff, SHORT_BLOCK, wnd_shape, timeBuff);
                transBuffPtr = 0;
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[timeBuffPtr++] += transBuff[transBuffPtr++];
                }
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[timeBuffPtr++] = transBuff[transBuffPtr++];
                }
                timeBuffPtr -= BLOCK_LEN_SHORT;
            }
            dolbyShortOffset = true;
        } else if (blockType == EIGHT_SHORT) {
            if (dolbyShortOffset) destPtr = 0 + 4 * BLOCK_LEN_SHORT; else destPtr = 0 + (BLOCK_LEN_LONG - BLOCK_LEN_SHORT) / 2;
            for (i = 0; i < 8; i++) {
                unfold(freqIn, srcPtr, transBuff, 1, BLOCK_LEN_SHORT);
                srcPtr += BLOCK_LEN_SHORT;
                ITransformBlock(transBuff, SHORT_BLOCK, wnd_shape, timeBuff);
                transBuffPtr = 0;
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[destPtr++] += transBuff[transBuffPtr++];
                }
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[destPtr++] = transBuff[transBuffPtr++];
                }
                destPtr -= BLOCK_LEN_SHORT;
            }
            destPtr = 0;
            timeBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = timeBuff[timeBuffPtr++];
            }
            destPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeBuff[destPtr++] = timeBuff[timeBuffPtr++];
            }
        } else if (blockType == SHORT_STOP) {
            destPtr = 4 * BLOCK_LEN_SHORT;
            srcPtr = 0;
            for (i = 0; i < N_SHORT_IN_STOP; i++) {
                unfold(freqIn, srcPtr, transBuff, 1, BLOCK_LEN_SHORT);
                srcPtr += BLOCK_LEN_SHORT;
                ITransformBlock(transBuff, SHORT_BLOCK, wnd_shape, timeBuff);
                transBuffPtr = 0;
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[destPtr++] += transBuff[transBuffPtr++];
                }
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[destPtr++] = transBuff[transBuffPtr++];
                }
                destPtr -= BLOCK_LEN_SHORT;
            }
            unfold(freqIn, srcPtr, transBuff, 1, (BLOCK_LEN_SHORT + BLOCK_LEN_LONG) / 2);
            ITransformBlock(transBuff, STOP_BLOCK, wnd_shape, timeBuff);
            transBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_SHORT; i++) {
                timeBuff[destPtr++] += transBuff[transBuffPtr++];
            }
            destPtr = 0;
            timeBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = timeBuff[timeBuffPtr];
                timeBuff[timeBuffPtr++] = transBuff[transBuffPtr++];
            }
        } else if (blockType == LONG_START) {
            unfold(freqIn, srcPtr, transBuff, 1, 960);
            ITransformBlock(transBuff, START_ADV_BLOCK, wnd_shape, timeBuff);
            transBuffPtr = 0;
            timeBuffPtr = 0;
            destPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = transBuff[transBuffPtr++] + timeBuff[timeBuffPtr++];
            }
            timeBuffPtr = 0;
            for (i = 0; i < NWINADV; i++) {
                timeBuff[timeBuffPtr++] = transBuff[transBuffPtr++];
            }
            for (; i < (2 * (BLOCK_LEN_LONG)); i++) {
                timeBuff[timeBuffPtr++] = 0;
            }
        } else if (blockType == LONG_STOP) {
            unfold(freqIn, srcPtr, transBuff, 1, 960);
            ITransformBlock(transBuff, STOP_ADV_BLOCK, wnd_shape, timeBuff);
            transBuffPtr = 0;
            timeBuffPtr = 0;
            destPtr = 0;
            for (i = 0; i < (BLOCK_LEN_LONG - 896); i++) {
                timeOut[destPtr++] = timeBuff[timeBuffPtr++];
            }
            for (; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = transBuff[transBuffPtr++] + timeBuff[timeBuffPtr++];
            }
            timeBuffPtr = 0;
            for (; i < (2 * (BLOCK_LEN_LONG)); i++) {
                timeBuff[timeBuffPtr++] = transBuff[transBuffPtr++];
            }
        } else if (blockType == SHORT_EXT_STOP) {
            destPtr = 3 * BLOCK_LEN_SHORT;
            for (i = 0; i < 4; i++) {
                unfold(freqIn, srcPtr, transBuff, 1, BLOCK_LEN_SHORT);
                srcPtr += BLOCK_LEN_SHORT;
                ITransformBlock(transBuff, SHORT_BLOCK, wnd_shape, timeBuff);
                transBuffPtr = 0;
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[destPtr++] += transBuff[transBuffPtr++];
                }
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[destPtr++] = transBuff[transBuffPtr++];
                }
                destPtr -= BLOCK_LEN_SHORT;
            }
            unfold(freqIn, srcPtr, transBuff, 1, (BLOCK_LEN_SHORT + BLOCK_LEN_LONG) / 2);
            ITransformBlock(transBuff, STOP_BLOCK, wnd_shape, timeBuff);
            transBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_SHORT; i++) {
                timeBuff[destPtr++] += transBuff[transBuffPtr++];
            }
            destPtr = 0;
            timeBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = timeBuff[timeBuffPtr];
                timeBuff[timeBuffPtr++] = transBuff[transBuffPtr++];
            }
        } else if (blockType == NINE_SHORT) {
            destPtr = 3 * BLOCK_LEN_SHORT;
            for (i = 0; i < 9; i++) {
                unfold(freqIn, srcPtr, transBuff, 1, BLOCK_LEN_SHORT);
                srcPtr += BLOCK_LEN_SHORT;
                ITransformBlock(transBuff, SHORT_BLOCK, wnd_shape, timeBuff);
                transBuffPtr = 0;
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[destPtr++] += transBuff[transBuffPtr++];
                }
                for (j = 0; j < BLOCK_LEN_SHORT; j++) {
                    timeBuff[destPtr++] = transBuff[transBuffPtr++];
                }
                destPtr -= BLOCK_LEN_SHORT;
            }
            destPtr = 0;
            timeBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = timeBuff[timeBuffPtr++];
            }
            destPtr = 0;
            for (; i < (2 * (BLOCK_LEN_LONG)); i++) {
                timeBuff[destPtr++] = timeBuff[timeBuffPtr++];
            }
            dolbyShortOffset = true;
        } else if (blockType == OLD_START) {
            unfold(freqIn, srcPtr, transBuff, 1, BLOCK_LEN_LONG);
            ITransformBlock(transBuff, START_FLAT_BLOCK, wnd_shape, timeBuff);
            transBuffPtr = 0;
            timeBuffPtr = 0;
            destPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = transBuff[transBuffPtr++] + timeBuff[timeBuffPtr++];
            }
            timeBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeBuff[timeBuffPtr++] = transBuff[transBuffPtr++];
            }
            dolbyShortOffset = false;
        } else if (blockType == OLD_STOP) {
            unfold(freqIn, srcPtr, transBuff, 1, BLOCK_LEN_LONG);
            ITransformBlock(transBuff, STOP_FLAT_BLOCK, wnd_shape, timeBuff);
            transBuffPtr = 0;
            timeBuffPtr = 0;
            destPtr = 0;
            for (i = 0; i < (BLOCK_LEN_LONG - NFLAT); i++) {
                timeOut[destPtr++] = transBuff[transBuffPtr++] + timeBuff[timeBuffPtr++];
            }
            for (; i < BLOCK_LEN_LONG; i++) {
                timeOut[destPtr++] = transBuff[transBuffPtr++];
            }
            timeBuffPtr = 0;
            for (i = 0; i < BLOCK_LEN_LONG; i++) {
                timeBuff[timeBuffPtr++] = transBuff[transBuffPtr++];
            }
        } else {
            throw new IOException("Illegal Block_type " + blockType + " in time2freq_adapt(), aborting ...");
        }
        for (i = 0; i < BLOCK_LEN_LONG; i++) {
            ftimeOut[i] = timeOut[i];
        }
    }
