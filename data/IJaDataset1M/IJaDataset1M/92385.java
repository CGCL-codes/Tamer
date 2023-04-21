package net.sf.elbe.core.events;

import net.sf.elbe.core.ELBECoreMessages;
import net.sf.elbe.core.model.IAttribute;
import net.sf.elbe.core.model.IConnection;
import net.sf.elbe.core.model.IEntry;
import net.sf.elbe.core.model.IValue;

public class ValueModifiedEvent extends EntryModificationEvent {

    private IAttribute modifiedAttribute;

    private IValue oldValue;

    private IValue newValue;

    public ValueModifiedEvent(IConnection connection, IEntry modifiedEntry, IAttribute modifiedAttribute, IValue oldValue, IValue newValue, ModelModifier source) {
        super(connection, modifiedEntry, source);
        this.modifiedAttribute = modifiedAttribute;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public IAttribute getModifiedAttribute() {
        return this.modifiedAttribute;
    }

    public IValue getOldValue() {
        return this.oldValue;
    }

    public IValue getNewValue() {
        return this.newValue;
    }

    public String toString() {
        return ELBECoreMessages.bind(ELBECoreMessages.event__replaced_oldval_by_newval_at_att_at_dn, new String[] { getOldValue().getStringValue(), getNewValue().getStringValue(), getModifiedAttribute().getDescription(), getModifiedEntry().getDn().toString() });
    }
}
