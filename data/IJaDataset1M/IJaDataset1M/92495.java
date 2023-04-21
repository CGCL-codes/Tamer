package pso.intelligentproperties;

import java.util.HashMap;

public class IntelligentProperties extends HashMap<String, IntelligentProperty<?>> implements IIntelligentProperties {

    private static final long serialVersionUID = 1L;

    public IntelligentProperties() {
    }

    public final IntelligentProperty<?> getProperty(String key) {
        return get(key);
    }

    public final void setProperty(String key, IntelligentProperty<?> value) {
        if (this.get(key) != null) if (this.get(key).isReadOnly()) return;
        this.put(key, value);
    }

    public final String[] getKeys() {
        return this.keySet().toArray(new String[] {});
    }
}
