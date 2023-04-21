package it.dangelo.domjson.impl.path.rules;

import it.dangelo.domjson.JSONArray;
import it.dangelo.domjson.JSONElement;
import it.dangelo.domjson.JSONObject;
import it.dangelo.domjson.JSONPrincipalElement;
import it.dangelo.domjson.impl.JSONArrayImpl;
import it.dangelo.domjson.path.JsonPathException;

public class ChildRule extends AbstractRule {

    private String childName;

    public ChildRule(String childName) {
        this.childName = childName;
    }

    private JSONElement getChild(JSONObject object) {
        JSONElement result = null;
        if (this.childName.equals("*")) {
            JSONArray array = new JSONArrayImpl();
            String[] names = object.gatAttributeNames();
            for (String string : names) {
                array.add(object.get(string));
            }
            result = array;
        } else if (object.exist(this.childName)) {
            result = object.get(this.childName);
        }
        return result;
    }

    @Override
    protected PathContext execute(PathContext context) throws JsonPathException {
        Object results = context.getResults();
        if (results instanceof JSONObject) {
            JSONObject object = (JSONObject) results;
            results = this.getChild(object);
        } else if (results instanceof JSONArray) {
            JSONArray array = (JSONArray) results;
            JSONArray newResult = new JSONArrayImpl();
            for (JSONElement element : array) {
                if (element instanceof JSONObject) {
                    JSONObject object = (JSONObject) element;
                    newResult.add(this.getChild(object));
                }
            }
            results = newResult;
        }
        results = this.filterObject(results);
        context.setResults(results);
        return context;
    }
}
