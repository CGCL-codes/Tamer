package org.catapult.web.document.config;

import java.util.HashMap;
import java.util.Map;
import org.catapult.web.Element;
import org.catapult.web.core.Block;
import org.catapult.web.document.MergeElementPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;

/**
 * @author aku
 * @version $Rev: 149 $ $Date: 2008-11-18 13:08:05 -0500 (Tue, 18 Nov 2008) $
 */
public class AutoMergeElementPostProcessor implements BeanPostProcessor, InitializingBean {

    private class ElementFactory {

        private final String elementName;

        /**
         * @param elementName
         */
        public ElementFactory(String elementName) {
            super();
            this.elementName = elementName;
        }

        public Element create() {
            return (Element) AutoMergeElementPostProcessor.this.applicationContext.getBean(this.elementName);
        }
    }

    private class MergingElement extends Block {

        private final Element into;

        private final Element source;

        /**
         * @param clone
         * @param element
         */
        public MergingElement(Element into, Element source) {
            super();
            this.into = into;
            this.source = source;
            getChildNodes().add(source);
        }

        /**
         * @see org.catapult.web.support.AbstractNode#postProcessNode()
         */
        @Override
        protected void postProcessNode() throws Exception {
            getRequest().setAttribute(MergeElementPostProcessor.CONTENT, this.source.getChildNodes(), RequestAttributes.SCOPE_REQUEST);
            this.into.merge(this.source);
        }

        /**
         * @see org.catapult.web.support.AbstractNode#afterPostProcess()
         */
        @Override
        protected void afterPostProcess() {
            getRequest().removeAttribute(MergeElementPostProcessor.CONTENT, RequestAttributes.SCOPE_REQUEST);
        }
    }

    public static final String RENDERER_POSTFIX = "-renderer";

    public static final String DEFAULT_CONTENT_TYPE = "html";

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, ElementFactory> renderers = new HashMap<String, ElementFactory>();

    private String contentType = DEFAULT_CONTENT_TYPE;

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.applicationContext, Element.class);
        String rendererPostfix = RENDERER_POSTFIX + "." + this.contentType;
        for (String beanName : beanNames) {
            if (beanName.endsWith(rendererPostfix)) {
                String rendererName = beanName.substring(beanName.lastIndexOf('/') + 1, beanName.lastIndexOf(rendererPostfix));
                this.renderers.put(rendererName, new ElementFactory(beanName));
            }
        }
    }

    /**
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
     *      java.lang.String)
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        if (bean instanceof Element) {
            Element element = (Element) bean;
            if (this.contentType.equals(element.getContentType())) {
                ElementFactory renderer = this.renderers.get(element.getName());
                if (renderer != null) {
                    result = new MergingElement(renderer.create(), element);
                }
            }
        }
        return result;
    }

    /**
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
     *      java.lang.String)
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * @param contentType
     *            the contentType to set
     */
    public final void setContentType(String contentType) {
        Assert.hasText(contentType);
        this.contentType = contentType;
    }
}
