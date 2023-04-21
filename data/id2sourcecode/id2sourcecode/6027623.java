    @Primitive
    public static Value caml_ba_map_file(final CodeRunner ctxt, final Value vfd, final Value vkind, final Value vlayout, final Value vshared, final Value vdim, final Value vstart) throws Fail.Exception, Fatal.Exception, FalseExit {
        try {
            final int fd = vfd.asLong();
            final int flags = vkind.asLong() | vlayout.asLong();
            final long startPos = vstart.asBlock().asInt64();
            final Block dimBlock = vdim.asBlock();
            final int numDims = dimBlock.getWoSize();
            final int majorDim = (flags & CAML_BA_FORTRAN_LAYOUT) != 0 ? numDims - 1 : 0;
            if ((numDims < 1) || (numDims > MAX_NUM_DIMS)) {
                Fail.invalidArgument("Bigarray.mmap: bad number of dimensions");
            }
            final int[] dim = new int[numDims];
            for (int i = 0; i < numDims; i++) {
                final int d = dimBlock.get(i).asLong();
                dim[i] = d;
                if ((d == -1) && (i == majorDim)) {
                    continue;
                }
                if (d < 0) {
                    Fail.invalidArgument("Bigarray.create: negative dimension");
                }
            }
            final Channel ch = ctxt.getContext().getChannel(fd);
            if (ch == null) {
                Fail.raiseSysError("invalid file descriptor");
            }
            final RandomAccessFile file = ch.asStream();
            if (file == null) {
                Fail.raiseSysError("invalid file descriptor");
            }
            final long currPos = file.getFilePointer();
            final long fileSize = file.length();
            long arraySize = CAML_BA_ELEMENT_SIZE[flags & CAML_BA_KIND_MASK];
            for (int i = 0; i < numDims; i++) {
                final int d = dim[i];
                if (d != -1) {
                    arraySize *= d;
                }
            }
            if (dim[majorDim] == -1) {
                if (fileSize < startPos) {
                    Fail.failWith("Bigarray.mmap: file position exceeds file size");
                }
                final long dataSize = fileSize - startPos;
                dim[majorDim] = Misc.ensure32s(dataSize / arraySize);
                arraySize = dim[majorDim] * arraySize;
                if (arraySize != dataSize) {
                    Fail.failWith("Bigarray.mmap: file size doesn't match array dimensions");
                }
            } else {
                if (fileSize < startPos + arraySize) {
                    file.setLength(startPos + arraySize);
                }
            }
            final FileChannel.MapMode mode = vshared == Value.TRUE ? FileChannel.MapMode.READ_WRITE : FileChannel.MapMode.PRIVATE;
            final MappedByteBuffer data = file.getChannel().map(mode, currPos + startPos, arraySize);
            final MemArray array = new MemArray(flags, dim, data);
            final Block res = Block.createCustom(STRUCT_SIZE + (dim.length - 1) * 4, CustomBigArray.OPS);
            res.setCustom(array);
            return Value.createFromBlock(res);
        } catch (final InterruptedIOException iioe) {
            final FalseExit fe = FalseExit.createFromContext(ctxt.getContext());
            fe.fillInStackTrace();
            throw fe;
        } catch (final IOException ioe) {
            final String msg = ioe.getMessage();
            Fail.raiseSysError(msg != null ? msg : "error in file mapping");
            return Value.UNIT;
        }
    }
