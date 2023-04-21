    private boolean SVNFoldersSync(final SVNWCClient wcc, final SVNLocation srcFile, final SVNLocation dstFile) throws IOException, SVNException {
        _lastSrcFile = srcFile;
        _lastDstFile = dstFile;
        final String srcPath = srcFile.toString(), dstPath = dstFile.toString();
        if (0 == StringUtil.compareDataStrings(srcPath, dstPath, false)) {
            publishEvent(new SVNSyncEvent(srcFile, dstFile, SVNEventActionEnum.FAILED_EXTERNAL, new UnsupportedOperationException("Cannot sync file with itself")));
            return false;
        }
        final boolean srcIsFile = srcFile.isFile(wcc), dstIsFile = dstFile.isFile(wcc);
        if (srcIsFile != dstIsFile) {
            publishEvent(new SVNSyncEvent(srcFile, dstFile, SVNEventActionEnum.FAILED_EXTERNAL, new UnsupportedOperationException("Cannot sync folder with file")));
            return false;
        }
        if (!CoreUtils.isSVNFile(wcc, srcFile)) {
            if (isShowSkippedTargetsEnabled()) publishEvent(new SVNSyncEvent(srcFile, dstFile, SVNEventActionEnum.SKIP));
            return !isCancelled();
        }
        if (isSyncConfirmationRequired(srcPath) && (!isSyncConfirmed(srcPath))) {
            if (isShowSkippedTargetsEnabled()) publishEvent(new SVNSyncEvent(srcFile, dstFile, SVNEventActionEnum.SKIP));
            return !isCancelled();
        }
        if (srcIsFile) {
            _numProcessedFiles++;
            final Triplet<Long, Byte, Byte> diff = findDifference(wcc, srcFile, dstFile);
            if (null == diff) {
                if (isPropertiesSyncAllowed() && (!SVNPropertiesSync(wcc, srcFile, dstFile)) && isShowSkippedTargetsEnabled()) publishEvent(new SVNSyncEvent(srcFile, dstFile, SVNEventActionEnum.SKIP));
                return !isCancelled();
            }
            final SVNEventActionEnum action;
            if (isUseMergeForUpdate()) {
                if (!doMerge(wcc, srcFile, dstFile)) return false;
                action = SVNEventActionEnum.MERGE_COMPLETE;
            } else {
                srcFile.copyTo(wcc, dstFile);
                action = SVNEventActionEnum.UPDATE_EXISTS;
            }
            if (isPropertiesSyncAllowed()) SVNPropertiesSync(wcc, srcFile, dstFile);
            _numUpdatedNodes++;
            publishEvent(new SVNSyncEvent(srcFile, dstFile, action));
            return !isCancelled();
        }
        boolean sendHeartbeat = true;
        _numProcessedFolders++;
        if (isPropertiesSyncAllowed() && SVNPropertiesSync(wcc, srcFile, dstFile)) {
            _numUpdatedNodes++;
            publishEvent(new SVNSyncEvent(srcFile, dstFile, SVNEventActionEnum.UPDATE_EXISTS));
        }
        final Collection<? extends SVNLocation> srcFiles = srcFile.listFiles(wcc, SVNSyncFilesFilter.DEFAULT);
        final Set<String> srcProcd = new TreeSet<String>(), dstProcd = new TreeSet<String>();
        final int numSources = (null == srcFiles) ? 0 : srcFiles.size();
        if (numSources > 0) {
            for (final SVNLocation f : srcFiles) {
                if (isCancelled()) return false;
                final String n = f.getName();
                srcProcd.add(n);
                final SVNLocation d = dstFile.appendSubPath(n);
                _lastSrcFile = f;
                _lastDstFile = d;
                if (!CoreUtils.isSVNFile(wcc, f)) {
                    if (isShowSkippedTargetsEnabled()) publishEvent(new SVNSyncEvent(f, d, SVNEventActionEnum.SKIP));
                    continue;
                }
                if (d.exists(wcc)) {
                    if (!SVNFoldersSync(wcc, f, d)) return false;
                } else if (f.isFile(wcc)) {
                    f.copyTo(wcc, d);
                    doAdd(wcc, d, false, false, true, SVNDepth.EMPTY, false, false, true);
                    publishEvent(new SVNSyncEvent(f, d, SVNEventActionEnum.ADD));
                    _numAddedNodes++;
                    sendHeartbeat = false;
                } else if (f.isDirectory(wcc)) {
                    doAdd(wcc, d, true, true, true, SVNDepth.INFINITY, false, false, true);
                    publishEvent(new SVNSyncEvent(f, d, SVNEventActionEnum.ADD));
                    _numAddedNodes++;
                    if (!SVNFoldersSync(wcc, f, d)) return false;
                    sendHeartbeat = false;
                } else throw new StreamCorruptedException("Unknwn location type: " + f);
                dstProcd.add(n);
            }
        }
        final Collection<? extends SVNLocation> dstFiles = dstFile.listFiles(wcc, SVNSyncFilesFilter.DEFAULT);
        final int numDests = (null == dstFiles) ? 0 : dstFiles.size();
        if (numDests <= 0) {
            if (isCancelled()) return false;
            if (sendHeartbeat) postHeartbeatEvent();
            return true;
        }
        for (final SVNLocation d : dstFiles) {
            if (isCancelled()) return false;
            final String n = d.getName();
            if (dstProcd.contains(n)) continue;
            final SVNLocation sFile = srcFile.appendSubPath(n);
            _lastSrcFile = sFile;
            _lastDstFile = d;
            if (!CoreUtils.isSVNFile(wcc, d)) {
                if (isShowSkippedTargetsEnabled()) publishEvent(new SVNSyncEvent(sFile, d, SVNEventActionEnum.SKIP));
                continue;
            }
            doDelete(wcc, d, true, true, false);
            publishEvent(new SVNSyncEvent(sFile, d, SVNEventActionEnum.DELETE));
            _numDeletedNodes++;
            sendHeartbeat = false;
        }
        if (isCancelled()) return false;
        if (sendHeartbeat) postHeartbeatEvent();
        return true;
    }
