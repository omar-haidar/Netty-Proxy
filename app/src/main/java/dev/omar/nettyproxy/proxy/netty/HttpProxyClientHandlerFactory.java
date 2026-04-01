package dev.omar.nettyproxy.proxy.netty;

/**
 * Factory لإنشاء HttpProxyClientHandler
 * (بديل لـ Spring ApplicationContext.getBean)
 */
public interface HttpProxyClientHandlerFactory {

    /**
     * إنشاء handler جديد لكل اتصال
     * @param taskId رقم المهمة
     * @return كائن HttpProxyClientHandler جديد
     */
    HttpProxyClientHandler createHandler(long taskId);
}
