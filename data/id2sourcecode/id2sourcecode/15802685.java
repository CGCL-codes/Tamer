    public int read(byte[] buffer, int off, int len) throws IOException {
        int read = 0;
        List instructionsToProcess = new ArrayList();
        List tmp = new ArrayList();
        DeltaReadInstruction init = new DeltaReadInstruction();
        init.setReadFrom(position);
        init.setReadTo(position + len - 1);
        init.setWriteOffset(off);
        instructionsToProcess.add(init);
        for (int i = layers.size() - 1; i >= 0; i--) {
            DeltaLayer layer = (DeltaLayer) layers.get(i);
            if (layer.getCurrentBucket() == null) {
                layer.readNextBucket();
            }
            for (int b = 0; b < instructionsToProcess.size(); b++) {
                DeltaReadInstruction instruction = (DeltaReadInstruction) instructionsToProcess.get(b);
                long from = instruction.getReadFrom();
                long to = instruction.getReadTo();
                int writeOffset = instruction.getWriteOffset();
                if (layer.getCurrentBucket() == null) {
                    return -1;
                }
                while (!(layer.getCurrentBucket().getFrom() <= from && layer.getCurrentBucket().getTo() >= from)) {
                    Bucket bucket = layer.getCurrentBucket();
                    if (bucket.getSignature() == SIG_NEW) {
                        NewBytesBucket current = (NewBytesBucket) bucket;
                        IOHelper.skipFully(layer.getStream(), bucket.getLength() - current.getReadOffset());
                    }
                    layer.readNextBucket();
                }
                while (layer.getCurrentBucket() != null) {
                    Bucket bucket = layer.getCurrentBucket();
                    long toSkip = from - bucket.getFrom();
                    int toWrite = (int) Math.min(bucket.getLength() - toSkip, to - from + 1);
                    if (bucket.getSignature() == SIG_NEW) {
                        NewBytesBucket current = (NewBytesBucket) bucket;
                        toSkip -= current.getReadOffset();
                        IOHelper.skipFully(layer.getStream(), toSkip);
                        int readBytes = IOHelper.readFully(layer.getStream(), buffer, writeOffset, toWrite);
                        if (readBytes != toWrite) {
                            Logger.defaultLogger().error("Error processing instruction : " + instruction.toString() + ". Bucket is : " + current.toString());
                            throw new DeltaException("Incoherent read length : expected " + toWrite + ", got " + readBytes + " for diff-layer #" + i);
                        }
                        read += toWrite;
                        current.setReadOffset(current.getReadOffset() + toWrite + toSkip);
                    } else {
                        ReadPreviousBucket current = (ReadPreviousBucket) bucket;
                        DeltaReadInstruction newInstruction = new DeltaReadInstruction();
                        newInstruction.setReadFrom(current.getReadFrom() + toSkip);
                        newInstruction.setReadTo(newInstruction.getReadFrom() + toWrite - 1);
                        newInstruction.setWriteOffset(writeOffset);
                        tmp.add(newInstruction);
                    }
                    from += toWrite;
                    writeOffset += toWrite;
                    if (from == to + 1) {
                        break;
                    } else {
                        layer.readNextBucket();
                    }
                }
            }
            instructionsToProcess = tmp;
            tmp = new ArrayList();
        }
        position += read;
        return (read == 0 && len != 0) ? -1 : read;
    }
