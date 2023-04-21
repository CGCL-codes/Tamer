    public static Buffer mergeEventSegment(EventSegment segment, CacheService cache) {
        ArrayList<EventSegment> segmentList = sessionBufferTable.get(segment.getHash());
        if (null == segmentList) {
            segmentList = new ArrayList<EventSegment>();
            sessionBufferTable.put(segment.getHash(), segmentList);
        }
        segmentList.add(segment);
        if (null != cache) {
            if (segmentList.size() < segment.total) {
                Set<Integer> totalKeys = new HashSet<Integer>();
                for (int i = 0; i < segment.total; i++) {
                    totalKeys.add(i);
                }
                for (EventSegment seg : segmentList) {
                    totalKeys.remove(seg.sequence);
                }
                for (Integer key : totalKeys) {
                    byte[] content = (byte[]) cache.get(key);
                    Buffer buf = Buffer.wrapReadableContent(content);
                    EventSegment seg;
                    try {
                        seg = (EventSegment) EventDispatcher.getSingletonInstance().parse(buf);
                        segmentList.add(seg);
                    } catch (Exception e) {
                        sessionBufferTable.clear();
                        return null;
                    }
                }
            }
        }
        if (segmentList.size() == segment.total) {
            Collections.sort(segmentList);
            Buffer content = new Buffer(4096);
            for (EventSegment seg : segmentList) {
                content.write(seg.content, seg.content.readableBytes());
            }
            sessionBufferTable.remove(segment.getHash());
            return content;
        }
        return null;
    }
