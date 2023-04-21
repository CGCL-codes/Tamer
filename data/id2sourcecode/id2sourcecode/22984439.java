    boolean wrapContent(String tag, String tagAttrs, Writer writer, char[] content, RendererContext context) throws IOException {
        ivTouchId = context.getId();
        ivFreezed = false;
        if (content == null) content = EMPTY_CHARS;
        beginTag(tag, tagAttrs, writer, context);
        int oldDigestLenght = ivDigestLength;
        byte[] oldDigest = ivDigest;
        {
            int wrtContentMark = 0;
            int nested = 0;
            int digestSize = 0;
            byte[] bdigest = new byte[content.length * 2];
            for (int i = 0; i < content.length; i++) {
                char[] theTag = checkSpecialTag(content, i);
                if (theTag != null) {
                    if (LOGGER.isTraceEnabled()) {
                    } else {
                        writer.write(content, wrtContentMark, i - wrtContentMark);
                        wrtContentMark = i + TAGLEN;
                    }
                    i += TAGLEN - 1;
                }
                if (!ivInvalidateAll && BEGTAG == theTag) {
                    ++nested;
                    continue;
                }
                if (!ivInvalidateAll && ENDTAG == theTag) {
                    --nested;
                    continue;
                }
                if (nested > 0) continue;
                short sh = (short) content[i];
                bdigest[digestSize++] = (byte) (sh & 0x00FF);
                bdigest[digestSize++] = (byte) ((sh >> 8) & 0x00FF);
            }
            ivDigestLength = digestSize;
            if (LOGGER.isTraceEnabled() || digestSize <= SWITCH2DIGEST) {
                if (digestSize != bdigest.length) {
                    ivDigest = new byte[digestSize];
                    System.arraycopy(bdigest, 0, ivDigest, 0, digestSize);
                } else {
                    ivDigest = bdigest;
                }
                ivDigestContent = ivDigest;
            } else {
                ivDigestContent = null;
                ivDigest = context.digest(bdigest, 0, digestSize);
            }
            if (wrtContentMark < content.length) writer.write(content, wrtContentMark, content.length - wrtContentMark);
        }
        endTag(tag, tagAttrs, writer, context);
        ivChanged = !(oldDigest != null && oldDigestLenght == ivDigestLength && MessageDigest.isEqual(oldDigest, ivDigest));
        if ((context.getKeepChangedContentIndicator() && ivChanged)) {
            releaseChildContent();
            ivChangedContent = content;
        } else ivChangedContent = null;
        if (LOGGER.isDebugEnabled() && ivChangedContent != null) {
            if (!LOGGER.isTraceEnabled() && ivDigestContent == null) {
                LOGGER.debug("changedContent:" + ivUniqueId + " invalidateAll:" + ivInvalidateAll);
            } else {
                byte[] oDigest = (oldDigest == null) ? new byte[0] : oldDigest;
                LOGGER.trace("changedContent:" + ivUniqueId + " invalidateAll:" + ivInvalidateAll + " old-lenght:" + oDigest.length + " new-lenght:" + ivDigest.length);
                StringWriter sw = new StringWriter();
                PrintWriter out = new PrintWriter(sw);
                out.println();
                for (int i = 0; i < Math.min(ivDigest.length, oDigest.length); i += 2) {
                    char nc = (char) (ivDigest[i] | (ivDigest[i + 1] << 8));
                    char oc = (char) (oDigest[i] | (oDigest[i + 1] << 8));
                    if (nc != oc) {
                        StringBuilder nb = new StringBuilder();
                        StringBuilder ob = new StringBuilder();
                        for (int j = i; j < Math.min(i + 20, Math.min(ivDigest.length, oDigest.length)); j += 2) {
                            nb.append((char) (ivDigest[j] | (ivDigest[j + 1] << 8)));
                            ob.append((char) (oDigest[j] | (oDigest[j + 1] << 8)));
                        }
                        out.println("<<--DIFF:[old=" + ob + "][new=" + nb + "]");
                        break;
                    } else out.print(nc);
                }
                LOGGER.trace(sw.getBuffer().toString());
            }
        }
        return ivChanged;
    }
