    private int yPosToAnyPitch(int yPos, int currentLine) {
        if (stave.getStaveType() == StaveType.BASS) {
            yPos += bassOnlyOffset;
        }
        int middleC = stave.headSpace + (currentLine * stave.lineSpacing) + ((10 * stave.staveSpaceHeight) / 2);
        int yDif = middleC - yPos + verticalAdjustment;
        int modYDif = yDif % ((4 * stave.staveSpaceHeight) - (stave.staveSpaceHeight / 2));
        if (yDif < 0) {
            modYDif = ((4 * stave.staveSpaceHeight) - (stave.staveSpaceHeight / 2)) + modYDif;
        }
        int index = (modYDif + 1) / 2;
        if (index >= allPitchesFromSpacing.length) {
            index = allPitchesFromSpacing.length - 1;
        } else if (index < 0) {
            index = 0;
        }
        int pitch = allPitchesFromSpacing[index];
        int octaveDif = (int) Math.floor(yDif / 28) * 12;
        if (yDif < 0) {
            octaveDif -= 12;
        }
        pitch += octaveDif;
        return pitch;
    }
