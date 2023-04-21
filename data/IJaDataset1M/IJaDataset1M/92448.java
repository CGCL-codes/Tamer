package net.sf.joafip.store.service.objectfortest;

import java.io.IOException;
import java.io.Serializable;
import net.sf.joafip.StorableClass;

@StorableClass
public class BobSerializable implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2467347031289624020L;

    public static boolean defaultWrite;

    private Object object;

    private int value;

    private Boolean boolean1;

    public BobSerializable() {
        super();
    }

    public Object getObject() {
        return object;
    }

    public void setObject(final Object object) {
        this.object = object;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        this.value = value;
    }

    public Boolean getBoolean1() {
        return boolean1;
    }

    public void setBoolean1(final Boolean boolean1) {
        this.boolean1 = boolean1;
    }

    private void writeObject(final java.io.ObjectOutputStream out) throws IOException {
        if (defaultWrite) {
            out.defaultWriteObject();
        } else {
            out.writeObject(object);
            out.writeInt(value);
            out.writeBoolean(boolean1);
        }
    }

    private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        if (defaultWrite) {
            in.defaultReadObject();
        } else {
            object = in.readObject();
            value = in.readInt();
            boolean1 = in.readBoolean();
        }
    }
}
