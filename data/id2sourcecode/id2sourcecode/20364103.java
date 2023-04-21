        @Override
        public void handle(DecodeResponse event) {
            logger.debug("(SI={}): got decoded segment#{} from the decoder", storageIndex, common.getCurrentSegmentNum());
            int k = event.getK();
            int pad = common.getPaddingValue();
            SHA256d crypttextSegmentHasher = Hasher.getCrypttextSegmenthasher();
            List<byte[]> segment = new ArrayList<byte[]>();
            for (int i = 0; i < k; i++) {
                byte[] chunk = event.getBuffer(i);
                if (common.isTailSegmentReached() && i == k - 1 && pad > 0) {
                    int newlen = chunk.length - pad;
                    chunk = Arrays.copyOf(chunk, newlen);
                }
                crypttextSegmentHasher.update(chunk);
                segment.add(chunk);
            }
            byte[] segmentHash = crypttextSegmentHasher.digest();
            try {
                common.getCiphertextHashTree().setLeafHash(common.getCurrentSegmentNum(), segmentHash);
                int index = common.getCurrentSegmentNum() + 1;
                logger.debug("(SI={}): send validated segment to our parent downloader", storageIndex);
                trigger(new GotValidatedSegment(DataUtils.join(segment), index, common.getNumSegments()), downloadnode);
                if (common.nextSegment()) {
                    logger.debug("(SI={}): we still have segments to ask for, send getSegment {}", storageIndex, common.getCurrentSegmentNum());
                    trigger(new GetSegment(common.getCurrentSegmentNum()), segmentfetcher.provided(SegmentFetcher.class));
                } else {
                    end = System.currentTimeMillis();
                    long elapsed = end - start;
                    logger.debug("(SI={}): we finished downloading in {} msec", storageIndex, elapsed);
                    if (System.getProperty("debug.writetimespath") != null) {
                        debugWriteTimeStats(elapsed, storageIndex);
                    }
                    destroyAll();
                    trigger(new DownloadingDone(new StatusMsg(Status.Succeeded, "downloading completed"), storageIndex), downloadnode);
                }
            } catch (BadHashError e) {
                e.printStackTrace();
            } catch (NotEnoughHashesError e) {
                e.printStackTrace();
            }
        }
