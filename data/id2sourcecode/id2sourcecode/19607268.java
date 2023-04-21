    private final void sendToDataTurbine(SourceStream streamI, int idxI, Buffer bufferI) throws Exception {
        synchronized (synchLock) {
            if (bufferI.isDiscard()) {
                return;
            }
            Format format = bufferI.getFormat();
            if (format.getDataType() != Format.byteArray) {
                return;
            } else if ((formats[idxI] != null) && !format.equals(formats[idxI])) {
                throw new Exception("Unsupported format change on stream " + idxI + " from " + formats[idxI] + " to " + format);
            }
            if (absoluteStartTime == Long.MIN_VALUE) {
                absoluteStartTime = System.currentTimeMillis();
            }
            if (channels[idxI] == null) {
                buffersWaiting[idxI] = new Vector(1);
                formats[idxI] = format;
                userData[idxI] = "encoding=" + format.getEncoding();
                if (format instanceof AudioFormat) {
                    AudioFormat aFormat = (AudioFormat) format;
                    if (++audioChannels == 1) {
                        channels[idxI] = "Audio";
                    } else {
                        channels[idxI] = "Audio" + audioChannels;
                    }
                    userData[idxI] += ",content=audio" + ",channels=" + aFormat.getChannels() + ",framerate=" + aFormat.getFrameRate() + ",framesize=" + aFormat.getFrameSizeInBits() + ",samplerate=" + aFormat.getSampleRate() + ",samplesize=" + aFormat.getSampleSizeInBits() + ",endian=" + aFormat.getEndian() + ",signed=" + aFormat.getSigned() + ",startAt=" + absoluteStartTime;
                } else {
                    VideoFormat vFormat = (VideoFormat) format;
                    String cName;
                    if (++videoChannels == 1) {
                        cName = "Video";
                    } else {
                        cName = "Video" + videoChannels;
                    }
                    if (getUseEncoding()) {
                        if (format.getEncoding().equalsIgnoreCase("jpeg")) {
                            cName += ".jpg";
                        }
                    }
                    channels[idxI] = cName;
                    Dimension size = vFormat.getSize();
                    userData[idxI] += ",content=video" + ",framerate=" + vFormat.getFrameRate() + ",maxlength=" + vFormat.getMaxDataLength() + ",height=" + ((int) size.getHeight()) + ",width=" + ((int) size.getWidth());
                    if (vFormat instanceof H261Format) {
                        H261Format h261Format = (H261Format) vFormat;
                        userData[idxI] += ",stillimage=" + h261Format.getStillImageTransmission();
                    } else if (vFormat instanceof H263Format) {
                        H263Format h263Format = (H263Format) vFormat;
                        userData[idxI] += ",advancedprediction=" + h263Format.getAdvancedPrediction() + ",arithmeticcoding=" + h263Format.getArithmeticCoding() + ",errorcompensation=" + h263Format.getErrorCompensation() + ",hrdb=" + h263Format.getHrDB() + ",pbframes=" + h263Format.getPBFrames() + ",unrestrictedvector=" + h263Format.getUnrestrictedVector();
                    } else if (vFormat instanceof JPEGFormat) {
                        JPEGFormat jpegFormat = (JPEGFormat) vFormat;
                        userData[idxI] += ",decimation=" + jpegFormat.getDecimation() + ",qfactor=" + jpegFormat.getQFactor();
                    } else if (vFormat instanceof RGBFormat) {
                        RGBFormat rgbFormat = (RGBFormat) vFormat;
                        userData[idxI] += ",bpp=" + rgbFormat.getBitsPerPixel() + ",blue=" + rgbFormat.getBlueMask() + ",endian=" + rgbFormat.getEndian() + ",flipped=" + rgbFormat.getFlipped() + ",green=" + rgbFormat.getGreenMask() + ",line=" + rgbFormat.getLineStride() + ",pixel=" + rgbFormat.getPixelStride() + ",red=" + rgbFormat.getRedMask();
                    }
                    userData[idxI] += ",startAt=" + absoluteStartTime;
                }
                int count = 0;
                for (int idx1 = 0; idx1 < channels.length; ++idx1) {
                    count += (channels[idx1] != null) ? 1 : 0;
                }
                if (count == channels.length) {
                    ChannelMap channelMap = new ChannelMap();
                    channelMap.PutTime(0., 0.);
                    for (int idx1 = 0; idx1 < channels.length; ++idx1) {
                        channelMap.Add(channels[idx1]);
                        channelMap.PutDataAsString(idx1, userData[idx1]);
                    }
                    rbnbSource.Register(channelMap);
                }
                lastKeyFrame = new int[channels.length];
            }
            if (bufferI.getLength() > 0) {
                Buffer waitBuffer = new Buffer();
                waitBuffer.copy(bufferI);
                byte[] original = (byte[]) waitBuffer.getData(), copied = new byte[original.length];
                System.arraycopy(original, 0, copied, 0, original.length);
                waitBuffer.setData(copied);
                buffersWaiting[idxI].addElement(waitBuffer);
                sendReadyToDataTurbine();
            }
        }
    }
