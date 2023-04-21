package org.t2framework.confeito.spi;

import org.t2framework.confeito.action.ErrorInfo;
import org.t2framework.confeito.annotation.Form;
import org.t2framework.confeito.contexts.WebContext;
import org.t2framework.confeito.model.Component;

/**
 * <#if locale="en">
 * <p>
 * {@code FormResolver} interface is to resolve mapping between request and
 * object.
 * 
 * Since 0.6.3, API change for some reason from parameter Object to
 * BeanDesc<Object>.
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 */
public interface FormResolver {

    /**
	 * <#if locale="en">
	 * <p>
	 * Resolve mapping from request to object.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param form
	 *            annotation
	 * @param context
	 *            object with request
	 * @param target
	 *            object instance
	 * @param errorInfo
	 */
    void resolve(Form form, WebContext context, Component<?> target, ErrorInfo errorInfo);
}
