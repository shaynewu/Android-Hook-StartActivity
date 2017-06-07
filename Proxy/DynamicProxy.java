　　interface ProxyInterFace  {  
　　	public void proxyMethod();  
　　}  
　　
　　class TargetObject implements ProxyInterFace  {  
　　  
　　	public void proxyMethod() {  
　　		System.out.println("我被代理了，哈哈！");  
　　    }  
　　}  
　　
　　class ProxyObject implements InvocationHandler {  
　　	//代码的对象  
　　	public Object targetObject;  
　　   
　　	public void setTargetObject(Object targetObject) {  
　　		this.targetObject = targetObject;  
　　 }  
　　  
　　 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {  
　　	 //调用，传入一个目标对象，和对应的对象参数  
　　	 System.out.println("代理前 你可以做的事情");  
　　	 Object object = method.invoke(targetObject, args);
　　	 System.out.println("代理后 你可以做的事情"); 
　　	 return object;
　　 }  
　　}  
　　  
　　public class DynamicProxy {  
　　 public static void main(String[] args) { 
　　	//代理的目标对象  
　　	ProxyInterFace  target = new TargetObject();
　　	//代理器  
　　	ProxyObject proxy =  new ProxyObject();
　　	proxy.setTargetObject(target);
　　	//需要传进函数的handler
　　	InvocationHandler handler = proxy;
　　	//生存新的代理对象
　　    Object newProxyObject = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), handler);
　　    //新的代理对象执行方法
　　	((ProxyInterFace)newProxyObject).proxyMethod();
　　 }  
　　}
//　　运行结果：
//　　代理前 你可以做的事情
//　　我被代理了，哈哈！
//　　代理后 你可以做的事情