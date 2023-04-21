    public void write(DataOutput out) throws IOException {
        sequence.write(out);
        quality.write(out);
        int presentFlags = 0;
        if (instrument != null) presentFlags |= Instrument_Present;
        if (runNumber != null) presentFlags |= RunNumber_Present;
        if (flowcellId != null) presentFlags |= FlowcellId_Present;
        if (lane != null) presentFlags |= Lane_Present;
        if (tile != null) presentFlags |= Tile_Present;
        if (xpos != null) presentFlags |= Xpos_Present;
        if (ypos != null) presentFlags |= Ypos_Present;
        if (read != null) presentFlags |= Read_Present;
        if (filterPassed != null) presentFlags |= FilterPassed_Present;
        if (controlNumber != null) presentFlags |= ControlNumber_Present;
        if (indexSequence != null) presentFlags |= IndexSequence_Present;
        WritableUtils.writeVInt(out, presentFlags);
        if (instrument != null) WritableUtils.writeString(out, instrument);
        if (runNumber != null) WritableUtils.writeVInt(out, runNumber);
        if (flowcellId != null) WritableUtils.writeString(out, flowcellId);
        if (lane != null) WritableUtils.writeVInt(out, lane);
        if (tile != null) WritableUtils.writeVInt(out, tile);
        if (xpos != null) WritableUtils.writeVInt(out, xpos);
        if (ypos != null) WritableUtils.writeVInt(out, ypos);
        if (read != null) WritableUtils.writeVInt(out, read);
        if (filterPassed != null) WritableUtils.writeVInt(out, filterPassed ? 1 : 0);
        if (controlNumber != null) WritableUtils.writeVInt(out, controlNumber);
        if (indexSequence != null) WritableUtils.writeString(out, indexSequence);
    }
