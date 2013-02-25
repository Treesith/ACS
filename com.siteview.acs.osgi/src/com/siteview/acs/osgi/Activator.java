package com.siteview.acs.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

import com.swg.acs.web.Hello;


public class Activator implements BundleActivator {

	 private BundleContext ctx;
	    private ServiceReference<HttpService> ref;
	    
	    public void start(BundleContext context) throws Exception 
	    {
	        ctx = context;
//	        context.addServiceListener((ServiceListener) this, "(objectClass=" + HttpService.class.getName() + ")");
	        registerServlet();
	    }

	    public void stop(BundleContext context) throws Exception 
	    {
	        context.removeServiceListener((ServiceListener) this);
	    }

	    public void serviceChanged(ServiceEvent event) 
	    {
	        switch (event.getType()){
	            case ServiceEvent.REGISTERED:
	                registerServlet();
	                break;
	    
	            case ServiceEvent.UNREGISTERING:
	                unregisterServlet();
	                break;
	        }
	    }
	    
	    private void registerServlet()
	    {
	        if (ref == null) {
	            ref = ctx.getServiceReference(HttpService.class);
	            
	            if (ref != null) {
	                try {
	                    HttpService http = ctx.getService(ref);
	                    
	                    http.registerServlet("/acs", new Hello(ctx), null, null);
	                    System.out.println("/acs已经被注册");
	                    
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            } 
	        }
	    }
	    
	    private void unregisterServlet() 
	    {
	        if (ref != null) {
	            try {
	                HttpService http = ctx.getService(ref);
	                
	                http.unregister("/demo/hello");
	                System.out.println("/demo/hello已经被卸载");
	                
	                ref = null;
	            }
	            catch(Exception e){
	                e.printStackTrace();
	            }
	        }
	    }

}
