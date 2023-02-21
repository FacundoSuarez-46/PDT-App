package com.fannog.cliente.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BeanFactory {

    private final InitialContext ctx;
    private final String SERVER_NAME = "ProyectoServidor";
    private final String EJB_NAME = "ProyectoEJB-ejb";
    private final String PATTERN = "DAO";
    private final String CONVENTION = "Impl";
    private final String SERVER_PACKAGE = "com.fannog.servidor";

    private static final ThreadLocal<BeanFactory> BEANS = ThreadLocal.withInitial(BeanFactory::create);

    private BeanFactory(InitialContext initialContext) {
        this.ctx = initialContext;
    }

    public <T> T lookup(String entity) throws Exception {
        String jdniName = "ejb:"
                + SERVER_NAME
                + "/"
                + EJB_NAME
                + "/"
                + entity + PATTERN
                + CONVENTION
                + "!"
                + SERVER_PACKAGE
                + "."
                + PATTERN
                + "."
                + entity
                + PATTERN;

        try {
            return (T) ctx.lookup(jdniName);
        } catch (NamingException e) {
            throw new Exception("Could not find jndi: " + jdniName, e);
        }
    }

    public static BeanFactory local() {
        return BEANS.get();
    }

    public static BeanFactory create() {
        try {
            return new BeanFactory(new InitialContext());
        } catch (NamingException e) {
            throw new RuntimeException("Could not create a new context", e);
        }
    }
}
