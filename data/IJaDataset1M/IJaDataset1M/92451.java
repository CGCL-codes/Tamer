package gnu.beanfactory.taglib;

import gnu.beanfactory.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import gnu.beanfactory.servlet.*;
import org.apache.log4j.*;
import java.util.*;

public class SelectTag extends BaseTag {

    static Category log = Category.getInstance(SelectTag.class.getName());

    String optionsDict;

    public String getOptionsUrl() {
        return optionsDict;
    }

    public void setOptionsUrl(String url) {
        this.optionsDict = expand(url);
    }

    public java.util.Map getValueMap() {
        try {
            if (getOptionsUrl() != null && getOptionsUrl().trim().length() > 0) {
                BeanContext ctx = Container.getBeanContext();
                Object d = ctx.resolve(getOptionsUrl());
                if (d != null) {
                    if (d instanceof Dictionary) {
                        Dictionary dict = (Dictionary) d;
                        Enumeration e = dict.keys();
                        TreeMap map = new TreeMap();
                        while (e.hasMoreElements()) {
                            Object key = e.nextElement();
                            Object val = dict.get(key);
                            map.put(key.toString(), val.toString());
                        }
                        return map;
                    } else {
                        return new HashMap();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("optionsUrl " + getOptionsUrl() + " could not be resolved");
        }
        return new java.util.HashMap();
    }

    DigestSecurity ds = null;

    public int doStartTag() throws JspException {
        try {
            ds = new DigestSecurity();
            JspWriter out = pageContext.getOut();
            out.print("<select ");
            writeAttribute("name", getMangledPropertyString());
            writeDescriptiveAttributes();
            ds.update(getMangledPropertyString());
            out.print(">");
            return EVAL_BODY_INCLUDE;
        } catch (java.io.IOException e) {
            throw new EncapsulatedJspException(e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new EncapsulatedJspException(e);
        } catch (BeanFactoryException e) {
            throw new EncapsulatedJspException(e);
        }
    }

    public int doEndTag() throws JspException {
        JspWriter output = pageContext.getOut();
        try {
            BeanContext ctx = Container.getBeanContext();
            Object targetVal = ctx.resolve(getUrl());
            Map dict = getValueMap();
            Iterator e = dict.keySet().iterator();
            while (e.hasNext()) {
                String key = e.next().toString();
                String val = dict.get(key).toString();
                output.print("<option ");
                writeAttribute("value", val, true);
                if (OptionTag.isSelected(targetVal, val)) {
                    output.print(" selected ");
                }
                output.print(">");
                output.print(key);
                output.print("</option>");
            }
            output.print("</select>");
            writeFingerprint(ds);
            return EVAL_PAGE;
        } catch (java.io.IOException e) {
            throw new gnu.beanfactory.servlet.EncapsulatedJspException(e);
        } catch (BeanFactoryException e) {
            throw new gnu.beanfactory.servlet.EncapsulatedJspException(e);
        }
    }
}
