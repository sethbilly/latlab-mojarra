/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latlab.mojarra.jsf;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class JsfUtil {

    public static final String REDIRECT = "faces-redirect=true";
    public static final String INCLUDE_FACES_REDIRECT = "includeViewParams=true";
    public static final String PARAM_SEPARATOR = "&amp;";

    public static final String REQUEST_HELPER = REDIRECT + PARAM_SEPARATOR + INCLUDE_FACES_REDIRECT;

    public static final String FULL_HELPER = "index.html?faces-redirect=true&amp;includeViewParams=true";

    public static Class[] methodExpArgs = {ActionEvent.class};
    public static Class[] emptyMethodParameter = new Class[0];

    private static final Logger logger = Logger.getLogger(JsfUtil.class.getName());

    public static String getRequestBaseURL(HttpServletRequest request) {

//        System.out.println(request.getRequestURI());
//        System.out.println(request.getRequestURL());
//        System.out.println(request.getContextPath());
        String requestUrl = request.getRequestURL().toString();

        String domainUrl = requestUrl.substring(0, requestUrl.length() - request.getRequestURI().length());

//        System.out.println(request.getRequestURI());
//        String contextPath = request.getContextPath();
//        String requestUrl = request.getRequestURL().toString();
//        String url = requestUrl + contextPath;
//        
//        
        return domainUrl + request.getContextPath();

    }

    public static SelectItem[] createSelectItems(String[] listItems, boolean selectOne) {
        List entities = Arrays.asList(listItems);

        return createSelectItems(entities, selectOne);
    }

    public static SelectItem[] createSelectItems(Object[] listItems, boolean selectOne) {
        List entities = Arrays.asList(listItems);

        return createSelectItems(entities, selectOne);
    }

    public static SelectItem[] createSelectItems(boolean selectOne, String... listItems) {
        List entities = Arrays.asList(listItems);

        return createSelectItems(entities, selectOne);
    }

    /**
     * @param itemsToSelectCollection the item collections would be used to fill
     * the combobox when binded
     *
     * @param selectOne when true would preced the list items with --Select One-
     *
     *
     * @return select items to bind to <f:select items />
     */
    public static SelectItem[] createSelectItems(Collection<?> entities, boolean selectOne) {

        if (entities == null) {
            return null;
        }

        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem(null, "--Select One--");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static SelectItem[] createSelectItems(Object[] data, String header) {

        if (data == null) {
            return null;
        }

        int size = data.length + 1;
        SelectItem[] items = new SelectItem[size];
        int i = 0;

        items[0] = new SelectItem(null, "--" + header + "--");
        i++;

        for (Object x : data) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static SelectItem[] createSelectItems(Collection<?> entities, String header) {

        if (entities == null) {
            return null;
        }

        int size = entities.size() + 1;
        SelectItem[] items = new SelectItem[size];
        int i = 0;

        items[0] = new SelectItem(null, "--" + header + "--");
        i++;

        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static HtmlSelectOneMenu createUISelectItem(List list) {
        HtmlSelectOneMenu selectOneMenu = new HtmlSelectOneMenu();

        for (Object object : list) {
            UISelectItem selectOne = new UISelectItem();
            selectOne.setItemValue(object);
            selectOne.setItemLabel(object.toString());

            selectOneMenu.getChildren().add(selectOne);
        }

        selectOneMenu.getChildren().add(new UISelectItem());

        return selectOneMenu;
    }

    public static HtmlSelectOneMenu createUISelectItem(SelectItem[] list) {
        HtmlSelectOneMenu selectOneMenu = new HtmlSelectOneMenu();

        for (SelectItem selectItem : list) {
            UISelectItem selectOne = new UISelectItem();
            selectOne.setItemValue(selectItem.getValue());
            selectOne.setItemLabel(selectItem.getLabel());

            selectOneMenu.getChildren().add(selectOne);
        }

        selectOneMenu.getChildren().add(new UISelectItem());

        return selectOneMenu;
    }

    public static void addErrorMessageg(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);

        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void errorMsg(String msg) {
        addErrorMessage(msg);
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        addMessage(facesMsg);
    }

    public static void addErrorMessage(String msgKey, String msgLabel) {
        String errorMessage = FacesContext.getCurrentInstance().getApplication().
                getResourceBundle(FacesContext.getCurrentInstance(), msgKey)
                .getString(msgLabel);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage);
        addMessage(facesMsg);
    }

    public static void addErrorMessage(String msg, boolean renderResponse) {
        addErrorMessage(msg);
        if (renderResponse) {
            getFacesContext().renderResponse();
        }
    }

    public static void addMessage(FacesMessage facesMsg) 
    {
       FacesContext.getCurrentInstance().addMessage(null, facesMsg);
       FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void addErrorMessageAndRespond(String msg) {
        addErrorMessage(msg, true);
    }

    public static void addInformationMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
         addMessage(facesMsg);
        
        
    }

    public static void addInformationMessage(String msgKey, String msgLabel) {
        String infoMessage = FacesContext.getCurrentInstance().getApplication().
                getResourceBundle(FacesContext.getCurrentInstance(), msgKey)
                .getString(msgLabel);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, infoMessage, infoMessage);
         addMessage(facesMsg);
    }

    public static void infoMsg(String msg) {
        addInformationMessage(msg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static HttpSession getHTTPSession() {
        HttpSession httpSession = (HttpSession) getFacesContext().getExternalContext().getSession(true);

        return httpSession;
    }

    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
    }

    public static String getContextPath() {
        return getHttpServletRequest().getContextPath();
    }

    public static String getApplicationUri() {
        try {
            FacesContext ctxt = FacesContext.getCurrentInstance();
            ExternalContext ext = ctxt.getExternalContext();
            URI uri = new URI(ext.getRequestScheme(),
                    null, ext.getRequestServerName(), ext.getRequestServerPort(),
                    ext.getRequestContextPath(), null, null);
            return uri.toASCIIString();
        } catch (URISyntaxException e) {
            throw new FacesException(e);
        }
    }

    public static void resetViewRoot() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
        context.setViewRoot(viewRoot);
    }

    public static void setSessionAttribute(String key, Object value) {
        getHTTPSession().setAttribute(key, value);

    }

    public static <T extends Object> T getSessionAttribute(String key) {
        return (T) getHTTPSession().getAttribute(key);
    }

    public static String getSessionAttributeAsString(String key) {
        try {
            Object object = getHTTPSession().getAttribute(key);

            if (object != null) {
                return object.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return "";
    }

    public static Integer getSessionAttributeAsInteger(String key) {
        try {
            return Integer.parseInt(getSessionAttributeAsString(key));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void invalidateSession() {
        try {
            getHTTPSession().invalidate();
        } catch (Exception e) {
        }
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static ValueExpression createValueExp(String expresion, Class columnClass) {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        ExpressionFactory ef = facesContext.getApplication().getExpressionFactory();
        ValueExpression ve = ef.createValueExpression(facesContext.getELContext(), expresion, columnClass);

        return ve;
    }

    public static MethodExpression createMethodExp(String expresion, Class expectedReturnType, Class... methodArguments) {
        MethodExpression methodExpression = null;

        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            ExpressionFactory ef = facesContext.getApplication().getExpressionFactory();

//            FacesContext.getCurrentInstance().getApplication().get
            methodExpression = ef.createMethodExpression(facesContext.getELContext(),
                    expresion, expectedReturnType, methodArguments);
        } catch (Exception e) {
            System.out.println("==============================================");
            System.out.println("ERROR CREATING METHOD EXPRESSION");
            e.printStackTrace();
        }

        return methodExpression;
    }

    public static MethodExpression createNoArgsVoidMethodExp(String expresion) {
        MethodExpression methodExpression = null;

        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            ExpressionFactory ef = facesContext.getApplication().getExpressionFactory();

            methodExpression = ef.createMethodExpression(facesContext.getELContext(),
                    expresion, Void.class, emptyMethodParameter);
        } catch (Exception e) {
            System.out.println("==============================================");
            System.out.println("ERROR CREATING METHOD EXPRESSION");
            e.printStackTrace();
        }

        return methodExpression;
    }

    public static String getComponentValue(UIComponent component) {
        String value = null;

        try {
            value = (String) component.getValueExpression("value").getValue(JsfUtil.getELContext());
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }

        return value;
    }

    public static MethodExpression createActionExpression(String actionExpression, Class<?> returnType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createMethodExpression(
                facesContext.getELContext(), actionExpression, returnType, new Class[0]);
    }

    public static MethodExpression createVoidActionExpression() {
        MethodExpression me = null;

        return me;
    }

    public static void bindMethodToComponent(String actionExpression, UIComponent component) {
        try {
            if (component instanceof HtmlCommandLink) {
                HtmlCommandLink commandLink = (HtmlCommandLink) component;

                MethodExpression me = createNoArgsVoidMethodExp(actionExpression);
                commandLink.setActionExpression(me);
            } else if (component instanceof HtmlCommandButton) {
                HtmlCommandButton commandLink = (HtmlCommandButton) component;

                MethodExpression me = createNoArgsVoidMethodExp(actionExpression);
                commandLink.setActionExpression(me);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private static void bindMethodToRichAjaxComponent(String actionExpression,UIComponent component)
//    {
//        try
//        {
//            if(component instanceof HtmlAjaxCommandButton)
//            {
//                HtmlAjaxCommandButton commandButton = (HtmlAjaxCommandButton) component;
//
//                MethodExpression me = createNoArgsVoidMethodExp(actionExpression);
//                commandButton.setActionExpression(me);
//            }
//            else if(component instanceof HtmlAjaxCommandLink)
//            {
//                HtmlAjaxCommandLink commandLink = (HtmlAjaxCommandLink) component;
//
//                MethodExpression me = createNoArgsVoidMethodExp(actionExpression);
//                commandLink.setActionExpression(me);
//            }
//
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    public static void bindValueToComponent(String valueExpression, UIComponent component) {
        valueExpression = validateExp(valueExpression);
//        System.out.println("going to do value binding with expr  " + valueExpression);
        try {
            ValueExpression expression = createValueExp(valueExpression, Object.class);
            component.setValueExpression("value", expression);

//            System.out.println("........ binding sucessfull");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String validateExp(String exp) {
        if (exp.startsWith("#")) {
            return exp;
        } else {
            return "#{" + exp + "}";
        }
    }

    public static <T extends Object> T getManagedBean(String managedBeanName) {
        try {
            Object managedBean = FacesContext.getCurrentInstance()
                    .getApplication().getELResolver().
                    getValue(getFacesContext().getELContext(), null, managedBeanName);

            return (T) managedBean;
        } catch (Exception e) {
            logger.log(Level.INFO, e.toString());
        }
        return null;
    }

    public static FacesContext getFacesContext() {
        try {
            return FacesContext.getCurrentInstance();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting FacesContext.getCurrentInstance();", e);
        }
        return null;
    }

    public static Application getApplication() {
        try {
            return FacesContext.getCurrentInstance().getApplication();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting "
                    + "FacesContext.getCurrentInstance().getApplication()", e);
        }
        return null;
    }

    public static ELResolver getElResolver() {
        try {
            return FacesContext.getCurrentInstance().getApplication().getELResolver();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting "
                    + "FacesContext.getCurrentInstance().getApplication().getELResolver()", e);
        }
        return null;
    }

    public static void printRequestViewParameters() {
        HttpServletRequest request = JsfUtil.getHttpServletRequest();

        Enumeration<String> e = request.getParameterNames();

        while (e.hasMoreElements()) {
            String string = e.nextElement();

            System.out.println(string + " \t\t " + request.getParameter(string));

        }
    }

    // <editor-fold defaultstate="collapsed" desc="Get ELContext">
    public static ELContext getELContext() {
        try {
            return FacesContext.getCurrentInstance().getELContext();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting "
                    + "FacesContext.getCurrentInstance().getELContext()", e);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get randowm uuid">
    public static String getRandId() {
        return "ID_" + UUID.randomUUID().toString().replaceAll("-", "");
    }
// </editor-fold>
}
