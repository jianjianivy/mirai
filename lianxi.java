package lianxi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LianXi {

    // private constructor
    public Object getInstance(Class<?> tee) throws Exception {
        Constructor<?>[] constructors = tee.getConstructors();
        constructors[0].setAccessible(true);

        return constructors[0].newInstance();
    }

    // "BeanClassName",["childBean1:childBean2:childBean3:property1:value","childBean1:childBean2:property2:value"]
    public Object getBeanInstance(String className, String[] beanStr) throws Exception {
        Object beanInstance = null;

        Class<?> parentClass = Class.forName(className);
        beanInstance = parentClass.newInstance();

        for (String beanInfo : beanStr) {
            if (beanInfo == null || "".equals(beanInfo)) {
                continue;
            }

            List<String> listBean = Arrays.stream(beanInfo.split(":")).collect(Collectors.toList());
            setBeanInstance(listBean, parentClass, beanInstance);
        }

        return beanInstance;
    }

    // recursion
    public void setBeanInstance(List<String> listBean, Class<?> parentClass, Object beanInstacne) throws Exception {
        String propertyName = listBean.get(0);
        Method getMethod = parentClass.getMethod("get" + propertyName);
        Class<?> childClass = getMethod.getReturnType();
        Method setMethod = parentClass.getMethod("set" + propertyName, childClass);

        if (listBean.size() == 2) {
            // setVal
            setMethod.invoke(beanInstacne, listBean.get(1));
        } else {
            // get childBeanInstance
            Object childBeanInstance = getMethod.invoke(beanInstacne);
            if (childBeanInstance == null) {
                childBeanInstance = childClass.newInstance();
            }

            // set childBeanInstacnce into parentBeanInstance
            setMethod.invoke(beanInstacne, childBeanInstance);
            // FIFO
            listBean.remove(0);
            setBeanInstance(listBean, childClass, childBeanInstance);
        }
    }

    // loop
    public void setBeanInstanceLoop(List<String> listBean, Class<?> parentClass, Object beanInstacne) throws Exception {

        Object parentBeanInstance = beanInstacne;

        for (int i = 0; i < listBean.size(); i++) {
            String propertyName = listBean.get(i);
            Method getMethod = parentClass.getMethod("get" + propertyName);
            Class<?> childClass = getMethod.getReturnType();
            Method setMethod = parentClass.getMethod("set" + propertyName, childClass);

            if (i == listBean.size() - 2) {
                // setVal
                setMethod.invoke(beanInstacne, listBean.get(i + 1));
                break;
            } else {
                // get childBeanInstance
                Object childBeanInstance = getMethod.invoke(parentBeanInstance);
                if (childBeanInstance == null) {
                    childBeanInstance = childClass.newInstance();
                }

                // set childBeanInstacnce into parentBeanInstance
                setMethod.invoke(beanInstacne, childBeanInstance);

                // next bean
                parentClass = childBeanInstance.getClass();
                parentBeanInstance = childBeanInstance;
            }
        }
    }
}
