    protected void makeNewInterval() {
        Random randNumber = new Random();
        int string = randNumber.nextInt(6);
        int fret = randNumber.nextInt(13);
        rootMidi = FretboardObject.convertPostionToMidiNote(fret, string);
        intervalMidi = 0;
        while ((rootMidi + currentInterval) > 76) {
            string = randNumber.nextInt(6);
            fret = randNumber.nextInt(13);
            rootMidi = FretboardObject.convertPostionToMidiNote(fret, string);
        }
        switch(string) {
            case 0:
                fretboardG.positionDot(FretboardObject.convertIntToFret(fret), FretboardObject.convertIntToString(string));
                fretboardG.positionSecondDot(FretboardObject.convertIntToFret(fret + currentInterval), FretboardObject.convertIntToString(string));
                intervalMidi = rootMidi + currentInterval;
                break;
            default:
                int string2 = randNumber.nextInt(string + 1);
                String intervalNote = FretboardObject.convertPostionToString(fret + currentInterval, string);
                int fret2 = 0;
                while (!(FretboardObject.convertPostionToString(fret2, string2).equals(intervalNote)) || (FretboardObject.convertPostionToMidiNote(fret2, string2) < rootMidi)) {
                    fret2++;
                    if (fret2 > 12) {
                        string2--;
                        fret2 = 0;
                    }
                }
                intervalMidi = FretboardObject.convertPostionToMidiNote(fret2, string2);
                fretboardG.positionDot(FretboardObject.convertIntToFret(fret), FretboardObject.convertIntToString(string));
                fretboardG.positionSecondDot(FretboardObject.convertIntToFret(fret2), FretboardObject.convertIntToString(string2));
                break;
        }
        playNotes(rootMidi, intervalMidi);
    }
