        public int processRun(ProcessingThread context) throws IOException {
            final ClipboardTrackList tl = (ClipboardTrackList) context.getClientArg("tl");
            final long insertPos = ((Long) context.getClientArg("pos")).longValue();
            final int mode = ((Integer) context.getClientArg("mode")).intValue();
            final List tis = (List) context.getClientArg("tis");
            final AbstractCompoundEdit edit = (AbstractCompoundEdit) context.getClientArg("edit");
            final BlendContext bcPre = (BlendContext) context.getClientArg("bcPre");
            final BlendContext bcPost = (BlendContext) context.getClientArg("bcPost");
            final Span insertSpan = (Span) context.getClientArg("insertSpan");
            final Span copySpan = (Span) context.getClientArg("copySpan");
            final boolean cutTimeline = ((Boolean) context.getClientArg("cut")).booleanValue();
            final Span cutTimelineSpan = (Span) context.getClientArg("cutSpan");
            final long delta = insertPos - tl.getSpan().start;
            Track.Info ti;
            Trail srcTrail;
            AudioTrail audioTrail;
            boolean[] trackMap;
            boolean isAudio, pasteAudio;
            for (int i = 0; i < tis.size(); i++) {
                ti = (Track.Info) tis.get(i);
                if (ti.selected) {
                    try {
                        ti.trail.editBegin(edit);
                        isAudio = ti.trail instanceof AudioTrail;
                        srcTrail = tl.getSubTrail(ti.trail.getClass());
                        if (isAudio) {
                            pasteAudio = (srcTrail != null) && (((AudioTrail) srcTrail).getChannelNum() > 0);
                        } else {
                            pasteAudio = false;
                        }
                        if (mode == EDIT_INSERT) {
                            ti.trail.editInsert(this, insertSpan, edit);
                            if (cutTimeline) ti.trail.editRemove(this, cutTimelineSpan, edit);
                        } else if (pasteAudio || ((mode == EDIT_OVERWRITE) && !isAudio)) {
                            ti.trail.editClear(this, insertSpan, edit);
                        }
                        if (pasteAudio) {
                            audioTrail = (AudioTrail) ti.trail;
                            trackMap = tl.getTrackMap(ti.trail.getClass());
                            int[] trackMap2 = new int[audioTrail.getChannelNum()];
                            for (int j = 0, k = 0; j < trackMap2.length; j++) {
                                if (ti.trackMap[j]) {
                                    for (; (k < trackMap.length) && !trackMap[k]; k++) ;
                                    if (k < trackMap.length) {
                                        trackMap2[j] = k++;
                                    } else if (tl.getTrackNum(ti.trail.getClass()) > 0) {
                                        for (k = 0; !trackMap[k]; k++) ;
                                        trackMap2[j] = k++;
                                    } else {
                                        trackMap2[j] = -1;
                                    }
                                } else {
                                    trackMap2[j] = -1;
                                }
                            }
                            if (!audioTrail.copyRangeFrom((AudioTrail) srcTrail, copySpan, insertPos, mode, this, edit, trackMap2, bcPre, bcPost)) return CANCELLED;
                        } else if ((ti.numTracks == 1) && (tl.getTrackNum(ti.trail.getClass()) == 1)) {
                            ti.trail.editAddAll(this, srcTrail.getCuttedRange(copySpan, true, srcTrail.getDefaultTouchMode(), delta), edit);
                        }
                    } finally {
                        ti.trail.editEnd(edit);
                    }
                }
            }
            return DONE;
        }
