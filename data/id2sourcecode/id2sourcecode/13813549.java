        public CLMem createImage3D(final CLContext context, final long flags, final CLImageFormat image_format, final long image_width, final long image_height, final long image_depth, final long image_row_pitch, final long image_slice_pitch, final Buffer host_ptr, IntBuffer errcode_ret) {
            final ByteBuffer formatBuffer = APIUtil.getBufferByte(2 * 4);
            formatBuffer.putInt(0, image_format.getChannelOrder());
            formatBuffer.putInt(4, image_format.getChannelType());
            final long function_pointer = CLCapabilities.clCreateImage3D;
            BufferChecks.checkFunctionAddress(function_pointer);
            if (errcode_ret != null) BufferChecks.checkBuffer(errcode_ret, 1); else if (LWJGLUtil.DEBUG) errcode_ret = APIUtil.getBufferInt();
            CLMem __result = new CLMem(nclCreateImage3D(context.getPointer(), flags, MemoryUtil.getAddress(formatBuffer, 0), image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, MemoryUtil.getAddress0Safe(host_ptr) + (host_ptr != null ? BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage3DSize(formatBuffer, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch)) : 0), MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
            if (LWJGLUtil.DEBUG) Util.checkCLError(errcode_ret.get(0));
            return __result;
        }
