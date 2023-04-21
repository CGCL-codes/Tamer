    boolean nextWrite() {
        log.debug("start nextWrite");
        while ((varList.size() >= 0) && (varListIndex < varList.size())) {
            int varNum = varList.get(varListIndex).intValue();
            int vState = _varModel.getState(varNum);
            VariableValue var = _varModel.getVariable(varNum);
            if (log.isDebugEnabled()) log.debug("nextWrite var index " + varNum + " state " + VariableValue.stateNameFromValue(vState) + " isToWrite: " + var.isToWrite() + " label:" + var.label());
            varListIndex++;
            if (var.isToWrite() || vState == VariableValue.UNKNOWN) {
                log.debug("start write of variable " + _varModel.getLabel(varNum));
                executeWrite(var);
                if (log.isDebugEnabled()) log.debug("return from starting var write");
                return true;
            }
        }
        while ((cvList.size() >= 0) && (cvListIndex < cvList.size())) {
            int cvNum = cvList.get(cvListIndex).intValue();
            CvValue cv = _cvModel.getCvByRow(cvNum);
            if (log.isDebugEnabled()) log.debug("nextWrite cv index " + cvNum + " state " + cv.getState());
            cvListIndex++;
            if (cv.isToWrite() || cv.getState() == CvValue.UNKNOWN) {
                if (log.isDebugEnabled()) log.debug("start write of cv index " + cvNum);
                setBusy(true);
                if (_programmingCV != null) log.error("listener already set at write start");
                _programmingCV = _cvModel.getCvByRow(cvNum);
                _read = false;
                _programmingCV.addPropertyChangeListener(this);
                _programmingCV.write(_cvModel.getStatusLabel());
                if (log.isDebugEnabled()) log.debug("return from starting cv write");
                return true;
            }
        }
        while ((indexedCvList.size() >= 0) && (indexedCvListIndex < indexedCvList.size())) {
            int indxVarNum = indexedCvList.get(indexedCvListIndex).intValue();
            int indxState = _varModel.getState(indxVarNum);
            if (log.isDebugEnabled()) log.debug("nextWrite indexed cv @ row index " + indexedCvListIndex + " state " + indxState);
            VariableValue iCv = _varModel.getVariable(indxVarNum);
            indexedCvListIndex++;
            if (iCv.isToWrite() || indxState == VariableValue.UNKNOWN) {
                String sz = "start write of indexed cv " + (_indexedCvModel.getCvByRow(indexedCvListIndex - 1)).cvName();
                if (log.isDebugEnabled()) log.debug(sz);
                setBusy(true);
                if (_programmingIndexedCV != null) log.error("listener already set at read start");
                _programmingIndexedCV = _varModel.getVariable(indxVarNum);
                _read = true;
                _programmingIndexedCV.addPropertyChangeListener(this);
                _programmingIndexedCV.writeAll();
                if (log.isDebugEnabled()) log.debug("return from starting indexed CV read");
                return true;
            }
        }
        if (log.isDebugEnabled()) log.debug("nextWrite found nothing to do");
        writeChangesButton.setSelected(false);
        writeAllButton.setSelected(false);
        setBusy(false);
        container.paneFinished();
        log.debug("return from nextWrite with nothing to do");
        return false;
    }
