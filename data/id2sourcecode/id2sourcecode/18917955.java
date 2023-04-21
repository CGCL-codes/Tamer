    public static Map read(BitInputStream bis) throws IOException {
        Map result = new LinkedHashMap();
        byte[] head = new byte[4];
        InputStream is = bis.getInputStream();
        is.read(head);
        boolean zipContent = head[0] == 67;
        result.put("zipContent", head[0] == 67);
        result.put("version", head[3]);
        head[0] = 0x46;
        byte[] lengthBytes = new byte[4];
        is.read(lengthBytes);
        long fileLength = _parseUI32(lengthBytes);
        result.put("fileLength", fileLength);
        InputStream swfUnzippedInputStream = null;
        if (zipContent) {
            swfUnzippedInputStream = new InflaterInputStream(is);
        } else {
            swfUnzippedInputStream = is;
        }
        BitInputStream bitInputStream = new BitInputStream(swfUnzippedInputStream);
        result.put("sizes", BasicTypesUtils.rect(bitInputStream));
        byte[] frameDelay = new byte[] { bitInputStream.read(), bitInputStream.read() };
        result.put("frameRate", BitUtils._parseFixed88(frameDelay));
        byte[] frameCount = new byte[] { bitInputStream.read(), bitInputStream.read() };
        result.put("frameCount", BitUtils._parseUI16(frameCount));
        boolean finished = false;
        List tags = new ArrayList();
        while (!finished) {
            try {
                Map tagContents = new LinkedHashMap();
                Map tagHeader = BasicTypesUtils.tagHeader(bitInputStream);
                Long tagLength = (Long) tagHeader.get("tagLength");
                Long tagCode = (Long) tagHeader.get("tagCode");
                String tagName = TagUtils.getTagNameForCode(tagCode.intValue());
                tagContents.put("tagCode", tagCode);
                tagContents.put("tagName", tagName);
                tagContents.put("tagLength", tagLength);
                if (TagUtils.getImpl(tagCode.intValue()) != null) {
                    byte[] buf = new byte[tagLength.intValue()];
                    bitInputStream.read(buf);
                    ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                    tagContents.putAll(TagUtils.getImpl(tagCode.intValue()).read(new BitInputStream(bais)));
                } else {
                    ByteArrayOutputStream tagContentBaos = new ByteArrayOutputStream();
                    for (int i = 0; i < tagLength; i++) {
                        tagContentBaos.write(bitInputStream.read());
                    }
                    tagContents.put("__data__", _hex(tagContentBaos.toByteArray()));
                }
                if ((tags.size() > 2)) {
                    String prevTagName = (String) ((Map) tags.get(tags.size() - 1)).get("tagName");
                    String prevPrevTagName = (String) ((Map) tags.get(tags.size() - 2)).get("tagName");
                    if (prevTagName != null && prevPrevTagName != null && prevTagName.equals("End") && prevPrevTagName.equals("End")) {
                        finished = true;
                    }
                }
                tags.add(tagContents);
            } catch (IOException e) {
                finished = true;
            } catch (Throwable e2) {
                finished = true;
                e2.printStackTrace();
            }
        }
        result.put("tags", tags);
        return result;
    }
