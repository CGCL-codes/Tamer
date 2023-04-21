    private boolean mergeImpl(TaskMonitor monitor) throws IOException, DeltaProcessorException, TaskCancelledException {
        int read = 0;
        int highWaterMark = 0;
        List instructionsToProcess = new ArrayList();
        List tmp = new ArrayList();
        DeltaReadInstruction init = new DeltaReadInstruction();
        init.setReadFrom(position);
        init.setReadTo(position + BUFFER_SIZE - 1);
        init.setWriteOffset(0);
        instructionsToProcess.add(init);
        for (int i = layers.size() - 1; i >= 0; i--) {
            DeltaLayer layer = (DeltaLayer) layers.get(i);
            if (layer.getCurrentBucket() == null) {
                layer.readNextBucket();
            }
            for (int b = 0; b < instructionsToProcess.size(); b++) {
                if (monitor != null) {
                    monitor.checkTaskState();
                }
                DeltaReadInstruction instruction = (DeltaReadInstruction) instructionsToProcess.get(b);
                long from = instruction.getReadFrom();
                long to = instruction.getReadTo();
                int writeOffset = instruction.getWriteOffset();
                if (layer.getCurrentBucket() == null) {
                    return false;
                }
                while (layer.getCurrentBucket() != null && (!(layer.getCurrentBucket().getFrom() <= from && layer.getCurrentBucket().getTo() >= from))) {
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
                        int readBytes = IOHelper.readFully(layer.getStream(), buff, writeOffset, toWrite);
                        if (readBytes != toWrite) {
                            Logger.defaultLogger().error("Error processing instruction : " + instruction.toString() + ". Bucket is : " + current.toString());
                            throw new DeltaException("Incoherent read length : expected " + toWrite + ", got " + readBytes + " for diff-layer #" + i);
                        }
                        highWaterMark = Math.max(highWaterMark, writeOffset + toWrite);
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
        if (in != null) {
            for (int b = 0; b < instructionsToProcess.size(); b++) {
                DeltaReadInstruction instruction = (DeltaReadInstruction) instructionsToProcess.get(b);
                long from = instruction.getReadFrom();
                long to = instruction.getReadTo();
                int writeOffset = instruction.getWriteOffset();
                long toSkip = from - baseStreamPosition;
                IOHelper.skipFully(in, toSkip);
                baseStreamPosition += toSkip;
                int toWrite = (int) (to - from + 1);
                int readBytes = IOHelper.readFully(in, buff, writeOffset, toWrite);
                if (readBytes != toWrite) {
                    Logger.defaultLogger().error("Error processing instruction : " + instruction.toString() + ". Current base stream position is : " + baseStreamPosition + ". Current position is : " + position);
                    throw new DeltaException("Incoherent read length : expected " + toWrite + ", got " + readBytes + " for base stream.");
                }
                highWaterMark = Math.max(highWaterMark, writeOffset + toWrite);
                read += toWrite;
                baseStreamPosition += toWrite;
            }
            instructionsToProcess.clear();
        }
        int lastOffset = 0;
        for (int b = 0; b < instructionsToProcess.size(); b++) {
            DeltaReadInstruction instruction = (DeltaReadInstruction) instructionsToProcess.get(b);
            proc.newBytes(buff, lastOffset, instruction.getWriteOffset() - lastOffset);
            proc.blockFound(instruction.getReadFrom(), instruction.getReadTo());
            lastOffset = (int) (instruction.getWriteOffset() + instruction.getReadTo() - instruction.getReadFrom() + 1);
        }
        if (highWaterMark >= lastOffset) {
            proc.newBytes(buff, lastOffset, highWaterMark - lastOffset);
        }
        position += BUFFER_SIZE;
        return true;
    }
