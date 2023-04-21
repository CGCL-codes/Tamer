package org.spantus.segment.online.rule;

import java.text.MessageFormat;
import org.spantus.core.marker.Marker;
import org.spantus.segment.online.OnlineDecisionSegmentatorParam;

public class DecisionCtx {

    private Marker marker;

    private Long time;

    private Boolean state;

    private Long sample;

    private RuleBaseEnum.state segmentState;

    private RuleBaseEnum.state previousState;

    private OnlineDecisionSegmentatorParam param;

    public boolean isSegmentInit() {
        return getSegmentState() == null || getSegmentState().equals(RuleBaseEnum.state.start);
    }

    public boolean isSegmentStartState() {
        return getSegmentState() != null && getSegmentState().equals(RuleBaseEnum.state.start);
    }

    public boolean isSegmentEndState() {
        return getSegmentState().equals(RuleBaseEnum.state.end);
    }

    public boolean isSegmentState() {
        return getSegmentState().equals(RuleBaseEnum.state.segment);
    }

    public boolean isNoiseState() {
        return getSegmentState() == null;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean curentState) {
        this.state = curentState;
    }

    public Long getSample() {
        return sample;
    }

    public void setSample(Long sample) {
        this.sample = sample;
    }

    public RuleBaseEnum.state getSegmentState() {
        return segmentState;
    }

    public void setSegmentState(RuleBaseEnum.state segmentState) {
        this.previousState = this.segmentState;
        this.segmentState = segmentState;
    }

    public RuleBaseEnum.state getPreviousState() {
        return previousState;
    }

    public OnlineDecisionSegmentatorParam getParam() {
        if (param == null) {
            param = new OnlineDecisionSegmentatorParam();
        }
        return param;
    }

    public void setParam(OnlineDecisionSegmentatorParam param) {
        this.param = param;
    }

    public Long getSegmentLength() {
        Long substr = null;
        if (getMarker() != null && getMarker().getStart() != null) {
            substr = getMarker().getStart();
        }
        substr = substr == null ? getTime() : substr;
        return getTime() - substr;
    }

    public Long getNoiseLength() {
        Long substr = getTime();
        if (getMarker() != null) {
            substr = getMarker().getStart() + getMarker().getLength();
        }
        return getTime() - substr;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}:[{1} ms] state:{2};{3};marker:{4}; length:{5}; noise:{6};", getClass().getSimpleName(), getTime(), getSegmentState(), (Boolean.TRUE.equals(getState()) ? "signal" : "noise"), getMarker(), getSegmentLength(), getNoiseLength());
    }
}
