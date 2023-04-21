    private void applyResult(File src, File dst, File edit, EditResult result) throws IOException {
        assert result != null;
        boolean diffPrinted = false;
        if (result.operation == EditResult.SAVE) {
            assert result.changed == null;
            if (edit == null) throw new IllegalUsageError("can\'t save: not a batch editor");
            if (!diff3 && !diffWithDest) {
                result.changed = diff(src, edit);
                diffPrinted = true;
            } else result.changed = FileDiff.equals(src, edit);
            if (result.changed) result.operation = EditResult.SAVE_DIFF; else result.operation = EditResult.SAVE_SAME;
        }
        switch(result.operation) {
            case EditResult.NONE:
                return;
            case EditResult.DELETE:
                if (psh.delete(dst)) result.setDone();
                return;
            case EditResult.RENAME:
                if (psh.renameTo(src, dst)) result.setDone();
                return;
            case EditResult.MOVE:
                if (psh.move(src, dst, force)) result.setDone();
                return;
            case EditResult.COPY:
                if (psh.copy(src, dst)) result.setDone();
                return;
            case EditResult.SAVE_DIFF:
                assert result.changed;
            case EditResult.SAVE_SAME:
                boolean saveLocal = dst.equals(src);
                if (!result.changed && saveLocal) return;
                boolean canOverwrite = saveLocal || force;
                if (dst.exists() && !canOverwrite) throw new IllegalStateException(String.format("File %s is already existed. ", dst));
                File dstdir = dst.getParentFile();
                if (dstdir != null) psh.mkdirs(dstdir);
                if (!diffPrinted) if (diff3) {
                    diff(src, edit);
                    diff(dst, edit);
                } else if (diffWithDest) diff(dst, edit); else diff(src, edit);
                psh.copy(edit, dst);
                result.setDone();
                return;
            case EditResult.SAVE:
            default:
                throw new UnexpectedException("invalid operation: " + result.operation);
        }
    }
