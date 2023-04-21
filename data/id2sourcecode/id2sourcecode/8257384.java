        void convertFromABGRToRGBA() {
            int i;
            if (imageDataType == ImageComponentRetained.ImageDataType.TYPE_BYTE_ARRAY) {
                byte[] srcBuffer, dstBuffer;
                srcBuffer = getAsByteArray();
                if (dataIsByRef) {
                    dstBuffer = new byte[length];
                    for (i = 0; i < length; i += 4) {
                        dstBuffer[i] = srcBuffer[i + 3];
                        dstBuffer[i + 1] = srcBuffer[i + 2];
                        dstBuffer[i + 2] = srcBuffer[i + 1];
                        dstBuffer[i + 3] = srcBuffer[i];
                    }
                    data = dstBuffer;
                    dataIsByRef = false;
                } else {
                    byte a, b;
                    for (i = 0; i < length; i += 4) {
                        a = srcBuffer[i];
                        b = srcBuffer[i + 1];
                        srcBuffer[i] = srcBuffer[i + 3];
                        srcBuffer[i + 1] = srcBuffer[i + 2];
                        srcBuffer[i + 2] = b;
                        srcBuffer[i + 3] = a;
                    }
                }
            } else if (imageDataType == ImageComponentRetained.ImageDataType.TYPE_BYTE_BUFFER) {
                assert dataIsByRef;
                ByteBuffer srcBuffer, dstBuffer;
                srcBuffer = getAsByteBuffer();
                srcBuffer.rewind();
                ByteOrder order = ByteOrder.nativeOrder();
                dstBuffer = ByteBuffer.allocateDirect(length).order(order);
                dstBuffer.rewind();
                for (i = 0; i < length; i += 4) {
                    dstBuffer.put(i, srcBuffer.get(i + 3));
                    dstBuffer.put(i + 1, srcBuffer.get(i + 2));
                    dstBuffer.put(i + 2, srcBuffer.get(i + 1));
                    dstBuffer.put(i + 3, srcBuffer.get(i));
                }
                dataIsByRef = false;
            }
        }
