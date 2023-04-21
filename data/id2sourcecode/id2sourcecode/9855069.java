    public void run() {
        while (true) {
            try {
                int methodType = in.read();
                switch(Protocol.values()[methodType]) {
                    case Protocol.READ:
                        long sectorNumber = in.readLong();
                        int toRead = Math.min(in.readInt(), buffer.length / 512);
                        int result = target.read(sectorNumber, buffer, toRead);
                        out.writeByte(0);
                        out.writeInt(result);
                        out.writeInt(toRead * 512);
                        out.write(buffer, 0, toRead * 512);
                        break;
                    case WRITE:
                        long writesectorNumber = in.readLong();
                        int toWrite = Math.min(in.readInt(), buffer.length);
                        in.read(buffer, 0, toWrite);
                        int writeresult = target.write(writesectorNumber, buffer, toWrite);
                        out.writeByte(0);
                        out.writeInt(writeresult);
                        break;
                    case TOTAL_SECTORS:
                        long totalSectors = target.getTotalSectors();
                        out.writeLong(totalSectors);
                        break;
                    case CYLINDERS:
                        int cylinders = target.getCylinders();
                        out.writeInt(cylinders);
                        break;
                    case HEADS:
                        int heads = target.getHeads();
                        out.writeInt(heads);
                        break;
                    case SECTORS:
                        int sectors = target.getSectors();
                        out.writeInt(sectors);
                        break;
                    case TYPE:
                        int type = target.getType().ordinal();
                        out.writeInt(type);
                        break;
                    case INSERTED:
                        boolean inserted = target.isInserted();
                        out.writeBoolean(inserted);
                        break;
                    case LOCKED:
                        boolean locked = target.isLocked();
                        out.writeBoolean(locked);
                        break;
                    case READ_ONLY:
                        boolean readOnly = target.isReadOnly();
                        out.writeBoolean(readOnly);
                        break;
                    case SET_LOCKED:
                        boolean setlock = in.readBoolean();
                        target.setLock(setlock);
                        break;
                    case CLOSE:
                        target.close();
                        break;
                    default:
                        LOGGING.log(Level.WARNING, "socket closed due to protocol error");
                        return;
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
