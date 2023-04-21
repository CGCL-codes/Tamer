    public void writeImage(ImagePlus imp, String path, int compression, int jpegQuality) throws IOException {
        if (compression != NO_COMPRESSION && compression != JPEG_COMPRESSION && compression != PNG_COMPRESSION) throw new IllegalArgumentException("Unsupported Compression 0x" + Integer.toHexString(compression));
        this.biCompression = compression;
        if (jpegQuality < 0) jpegQuality = 0;
        if (jpegQuality > 100) jpegQuality = 100;
        this.jpegQuality = jpegQuality;
        File file = new File(path);
        raFile = new RandomAccessFile(file, "rw");
        raFile.setLength(0);
        imp.startTiming();
        boolean isComposite = imp.isComposite();
        boolean isHyperstack = imp.isHyperStack();
        boolean isOverlay = imp.getOverlay() != null && !imp.getHideOverlay();
        xDim = imp.getWidth();
        yDim = imp.getHeight();
        zDim = imp.getStackSize();
        boolean saveFrames = false, saveSlices = false, saveChannels = false;
        int channels = imp.getNChannels();
        int slices = imp.getNSlices();
        int frames = imp.getNFrames();
        int channel = imp.getChannel();
        int slice = imp.getSlice();
        int frame = imp.getFrame();
        if (isHyperstack || isComposite) {
            if (frames > 1) {
                saveFrames = true;
                zDim = frames;
            } else if (slices > 1) {
                saveSlices = true;
                zDim = slices;
            } else if (channels > 1) {
                saveChannels = true;
                zDim = channels;
            } else isHyperstack = false;
        }
        if (imp.getType() == ImagePlus.COLOR_RGB || isComposite || biCompression == JPEG_COMPRESSION || isOverlay) bytesPerPixel = 3; else bytesPerPixel = 1;
        boolean writeLUT = bytesPerPixel == 1;
        linePad = 0;
        int minLineLength = bytesPerPixel * xDim;
        if (biCompression == NO_COMPRESSION && minLineLength % 4 != 0) linePad = 4 - minLineLength % 4;
        frameDataSize = (bytesPerPixel * xDim + linePad) * yDim;
        int microSecPerFrame = (int) Math.round((1.0 / getFrameRate(imp)) * 1.0e6);
        writeString("RIFF");
        chunkSizeHere();
        writeString("AVI ");
        writeString("LIST");
        chunkSizeHere();
        writeString("hdrl");
        writeString("avih");
        writeInt(0x38);
        writeInt(microSecPerFrame);
        writeInt(0);
        writeInt(0);
        writeInt(0x10);
        writeInt(zDim);
        writeInt(0);
        writeInt(1);
        writeInt(0);
        writeInt(xDim);
        writeInt(yDim);
        writeInt(0);
        writeInt(0);
        writeInt(0);
        writeInt(0);
        writeString("LIST");
        chunkSizeHere();
        writeString("strl");
        writeString("strh");
        writeInt(56);
        writeString("vids");
        writeString("DIB ");
        writeInt(0);
        writeInt(0);
        writeInt(0);
        writeInt(1);
        writeInt((int) Math.round(getFrameRate(imp)));
        writeInt(0);
        writeInt(zDim);
        writeInt(0);
        writeInt(-1);
        writeInt(0);
        writeShort((short) 0);
        writeShort((short) 0);
        writeShort((short) 0);
        writeShort((short) 0);
        writeString("strf");
        chunkSizeHere();
        writeInt(40);
        writeInt(xDim);
        writeInt(yDim);
        writeShort(1);
        writeShort((short) (8 * bytesPerPixel));
        writeInt(biCompression);
        int biSizeImage = (biCompression == NO_COMPRESSION) ? 0 : xDim * yDim * bytesPerPixel;
        writeInt(biSizeImage);
        writeInt(0);
        writeInt(0);
        writeInt(writeLUT ? 256 : 0);
        writeInt(0);
        if (writeLUT) writeLUT(imp.getProcessor());
        chunkEndWriteSize();
        writeString("strn");
        writeInt(16);
        writeString("ImageJ AVI     \0");
        chunkEndWriteSize();
        chunkEndWriteSize();
        writeString("JUNK");
        chunkSizeHere();
        raFile.seek(4096);
        chunkEndWriteSize();
        writeString("LIST");
        chunkSizeHere();
        long moviPointer = raFile.getFilePointer();
        writeString("movi");
        if (biCompression == NO_COMPRESSION) bufferWrite = new byte[frameDataSize]; else raOutputStream = new RaOutputStream(raFile);
        int dataSignature = biCompression == NO_COMPRESSION ? FOURCC_00db : FOURCC_00dc;
        int maxChunkLength = 0;
        int[] dataChunkOffset = new int[zDim];
        int[] dataChunkLength = new int[zDim];
        for (int z = 0; z < zDim; z++) {
            IJ.showProgress(z, zDim);
            IJ.showStatus(z + "/" + zDim);
            ImageProcessor ip = null;
            if (isComposite || isHyperstack || isOverlay) {
                if (saveFrames) imp.setPositionWithoutUpdate(channel, slice, z + 1); else if (saveSlices) imp.setPositionWithoutUpdate(channel, z + 1, frame); else if (saveChannels) imp.setPositionWithoutUpdate(z + 1, slice, frame);
                ImagePlus imp2 = imp;
                if (isOverlay) {
                    if (!(saveFrames || saveSlices || saveChannels)) imp.setSliceWithoutUpdate(z + 1);
                    imp2 = imp.flatten();
                }
                ip = new ColorProcessor(imp2.getImage());
            } else ip = zDim == 1 ? imp.getProcessor() : imp.getStack().getProcessor(z + 1);
            int chunkPointer = (int) raFile.getFilePointer();
            writeInt(dataSignature);
            chunkSizeHere();
            if (biCompression == NO_COMPRESSION) {
                if (bytesPerPixel == 1) writeByteFrame(ip); else writeRGBFrame(ip);
            } else writeCompressedFrame(ip);
            dataChunkOffset[z] = (int) (chunkPointer - moviPointer);
            dataChunkLength[z] = (int) (raFile.getFilePointer() - chunkPointer - 8);
            if (maxChunkLength < dataChunkLength[z]) maxChunkLength = dataChunkLength[z];
            chunkEndWriteSize();
        }
        chunkEndWriteSize();
        if (isComposite || isHyperstack) imp.setPosition(channel, slice, frame);
        writeString("idx1");
        chunkSizeHere();
        for (int z = 0; z < zDim; z++) {
            writeInt(dataSignature);
            writeInt(0x10);
            writeInt(dataChunkOffset[z]);
            writeInt(dataChunkLength[z]);
        }
        chunkEndWriteSize();
        chunkEndWriteSize();
        raFile.close();
        IJ.showProgress(1.0);
    }
