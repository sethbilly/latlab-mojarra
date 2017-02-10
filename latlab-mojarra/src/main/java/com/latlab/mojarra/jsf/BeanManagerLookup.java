/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.latlab.mojarra.jsf;

import java.lang.reflect.Type;
import java.util.Iterator;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BeanManagerLookup {

    /**
     * holds a local beanManager if no jndi is available
     */
    public static BeanManager localInstance;

    /**
     * provide a custom jndi lookup name
     */
    public static String jndiName;

    public static BeanManager getBeanManager() {
        if (localInstance != null) {
            return localInstance;
        }
        return lookupBeanManagerInJndi();
    }

    public static BeanManager lookupBeanManagerInJndi() {

        if (jndiName != null) {
            try {
                return (BeanManager) InitialContext.doLookup(jndiName);
            } catch (NamingException e) {
                throw new IllegalArgumentException("Could not lookup beanmanager in jndi using name: '" + jndiName + "'.", e);
            }
        }

        try {
            // in an application server
            return (BeanManager) InitialContext.doLookup("java:comp/BeanManager");
        } catch (NamingException e) {
            // silently ignore
        }

        try {
            // in a servlet container
            return (BeanManager) InitialContext.doLookup("java:comp/env/BeanManager");
        } catch (NamingException e) {
            // silently ignore
        }

        throw new IllegalArgumentException(
                "Could not lookup beanmanager in jndi. If no jndi is avalable, set the beanmanger to the 'localInstance' property of this class.");
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookup(Class<T> clazz, BeanManager bm) {
        Iterator<Bean< ?>> iter = bm.getBeans(clazz).iterator();
        if (!iter.hasNext()) {
            throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type " + clazz.getName());
        }
        Bean<T> bean = (Bean<T>) iter.next();
        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        T dao = (T) bm.getReference(bean, clazz, ctx);
        return dao;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object lookup(String name, BeanManager bm) {
        Iterator<Bean< ?>> iter = bm.getBeans(name).iterator();
        if (!iter.hasNext()) {
            throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type '" + name + "'");
        }
        Bean bean = iter.next();
        CreationalContext ctx = bm.createCreationalContext(bean);
        // select one beantype randomly. A bean has a non-empty set of beantypes.
        Type type = (Type) bean.getTypes().iterator().next();
        return bm.getReference(bean, type, ctx);
    }

    public static <T> T lookup(Class<T> clazz) {
        BeanManager bm = BeanManagerLookup.getBeanManager();
        return lookup(clazz, bm);
    }

    public static Object lookup(String name) {
        BeanManager bm = BeanManagerLookup.getBeanManager();
        return lookup(name, bm);
    }

//   public BeanManager getBeanManager()
//    {
//        return (BeanManager) 
//              ((ServletContext) facesContext.getExternalContext().getContext())
//                   .getAttribute("javax.enterprise.inject.spi.BeanManager"); 
//    }
//  Set<Bean<?>> beans = beanManager.getBeans(Object.class,new AnnotationLiteral<Any>() {}));
//        for (Bean<?> bean : beans) {
//            System.out.println(bean.getBeanClass().getName());
//        }
}
