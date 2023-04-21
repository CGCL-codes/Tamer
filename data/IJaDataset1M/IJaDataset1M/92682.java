package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ALtIdShRelationalExpression extends PRelationalExpression {

    private TIdentifier _identifier_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers_ = new LinkedList<PAdditionalIdentifier>();

    private TLt _lt_;

    private PShiftExpression _shiftExpression_;

    public ALtIdShRelationalExpression() {
    }

    public ALtIdShRelationalExpression(@SuppressWarnings("hiding") TIdentifier _identifier_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers_, @SuppressWarnings("hiding") TLt _lt_, @SuppressWarnings("hiding") PShiftExpression _shiftExpression_) {
        setIdentifier(_identifier_);
        setAdditionalIdentifiers(_additionalIdentifiers_);
        setLt(_lt_);
        setShiftExpression(_shiftExpression_);
    }

    @Override
    public Object clone() {
        return new ALtIdShRelationalExpression(cloneNode(this._identifier_), cloneList(this._additionalIdentifiers_), cloneNode(this._lt_), cloneNode(this._shiftExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseALtIdShRelationalExpression(this);
    }

    public TIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node) {
        if (this._identifier_ != null) {
            this._identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier_ = node;
    }

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers() {
        return this._additionalIdentifiers_;
    }

    public void setAdditionalIdentifiers(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers_.clear();
        this._additionalIdentifiers_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public TLt getLt() {
        return this._lt_;
    }

    public void setLt(TLt node) {
        if (this._lt_ != null) {
            this._lt_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._lt_ = node;
    }

    public PShiftExpression getShiftExpression() {
        return this._shiftExpression_;
    }

    public void setShiftExpression(PShiftExpression node) {
        if (this._shiftExpression_ != null) {
            this._shiftExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._shiftExpression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._identifier_) + toString(this._additionalIdentifiers_) + toString(this._lt_) + toString(this._shiftExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        if (this._additionalIdentifiers_.remove(child)) {
            return;
        }
        if (this._lt_ == child) {
            this._lt_ = null;
            return;
        }
        if (this._shiftExpression_ == child) {
            this._shiftExpression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._identifier_ == oldChild) {
            setIdentifier((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PAdditionalIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._lt_ == oldChild) {
            setLt((TLt) newChild);
            return;
        }
        if (this._shiftExpression_ == oldChild) {
            setShiftExpression((PShiftExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}
