package org.millerM907.hw06_generation_adapters_by_interface;

import org.millerM907.hw02_stable_abstractions.space_battle.ConfigurableObject;
import org.millerM907.hw05_ioc_container.base.Command;
import org.millerM907.hw05_ioc_container.impl.ioc.IoC;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;

public class AdapterFactory {

    @SuppressWarnings("unchecked")
    public static <T> T createAdapter(Class<T> interfaceClass, ConfigurableObject obj) {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("Only interfaces are supported");
        }

        InvocationHandler handler = new UniversalAdapterHandler(interfaceClass, obj);

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                handler
        );
    }

    private static class UniversalAdapterHandler implements InvocationHandler {
        private static final String GET_KEY_TEMPLATE = "%s:%s.get";
        private static final String SET_KEY_TEMPLATE = "%s:%s.set";

        private final Class<?> interfaceClass;
        private final ConfigurableObject obj;

        UniversalAdapterHandler(Class<?> interfaceClass, ConfigurableObject obj) {
            this.interfaceClass = interfaceClass;
            this.obj = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            String methodName = method.getName();
            String interfaceName = interfaceClass.getName();

            if (methodName.startsWith("get")) {
                String property = decapitalize(methodName.substring(3));
                String key = String.format(GET_KEY_TEMPLATE, interfaceName, property);
                return IoC.resolve(key, obj);
            } else if (methodName.startsWith("set")) {
                String property = decapitalize(methodName.substring(3));
                String key = String.format(SET_KEY_TEMPLATE, interfaceName, property);
                IoC.<Command>resolve(key, obj, args[0]).execute();
                return null;
            } else {
                throw new UnsupportedOperationException("Unsupported method: " + methodName);
            }
        }

        private String decapitalize(String s) {
            if (s == null || s.isEmpty()) return s;
            return s.substring(0, 1).toLowerCase(Locale.ROOT) + s.substring(1);
        }
    }
}
