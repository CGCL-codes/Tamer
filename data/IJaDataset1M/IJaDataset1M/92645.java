package jaxilstudio.business.dataaccess;

import java.util.List;
import javax.persistence.Query;
import jaxilstudio.business.db.Module;
import easybusinesslayer.LayerType;
import easybusinesslayer.annotation.Call;
import easybusinesslayer.annotation.Layer;

@Layer(LayerType.DataAccess)
public class Modules {

    @Call(id = "data", method = "createModuleQuery")
    private Query query;

    public Module getModule(String type) {
        query.setParameter(1, type);
        List<?> modulelist = query.getResultList();
        if (modulelist.size() == 0) return null;
        return (Module) modulelist.get(0);
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }
}
