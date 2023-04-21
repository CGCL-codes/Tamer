        public void saveState(DataOutput output) throws IOException {
            output.writeInt(drive.ordinal());
            output.writeInt(driveFlags);
            output.writeByte(perpendicular);
            output.writeByte(head);
            output.writeInt(headCount);
            output.writeByte(track);
            output.writeByte(sector);
            output.writeByte(sectorCount);
            output.writeByte(direction);
            output.writeByte(readWrite);
            output.writeInt(flags);
            output.writeInt(maxTrack);
            output.writeInt(bps);
            output.writeInt(readOnly);
        }
