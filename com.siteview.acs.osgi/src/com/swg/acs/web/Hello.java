package com.swg.acs.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.osgi.framework.BundleContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.swg.acs.Message;
import com.swg.acs.MessageEnvelope;
import com.swg.acs.MessageHeader;
import com.swg.acs.message.AddObject;
import com.swg.acs.message.DeleteObject;
import com.swg.acs.message.Download;
import com.swg.acs.message.Fault;
import com.swg.acs.message.GetParameterAttributes;
import com.swg.acs.message.GetParameterNames;
import com.swg.acs.message.GetParameterValues;
import com.swg.acs.message.GetParameterValuesResponse;
import com.swg.acs.message.GetRPCMethods;
import com.swg.acs.message.GetRPCMethodsResponse;
import com.swg.acs.message.Inform;
import com.swg.acs.message.InformResponse;
import com.swg.acs.message.Reboot;
import com.swg.acs.message.ScheduleInform;
import com.swg.acs.message.SetParameterAttributes;
import com.swg.acs.message.SetParameterValues;
import com.swg.acs.message.SetParameterValuesResponse;
import com.swg.acs.message.Upload;
import com.swg.acs.message.UploadResponse;
import com.swg.acs.message.soap.SoapMessageBuilder;

//@WebServlet("/")
public class Hello extends ACSServlet {
	 @SuppressWarnings("unused")
    private BundleContext context;
	public Hello(BundleContext context)
    {
        this.context = context;
    }

//	protected static Logger logger = Logger.getLogger(ACSServlet.class);

	private static final long serialVersionUID = 1L;

	private static final String Element = null;

	static String PAGE_HEADER = "<html><head><title>helloworld</title><body>";

	static String PAGE_FOOTER = "</body></html>";
	static MessageFactory mf = null;
	static {
		try {
			mf = MessageFactory.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	/**
	 * 直接接受SOAPMessage
	 * @param req
	 * @param resp
	 */
	public void GetSOAPMessage(HttpServletRequest req,HttpServletResponse resp,Message message){
	
		   SOAPMessage requestMessage;
		try {
			MessageFactory factory = MessageFactory.newInstance();
			requestMessage = factory.createMessage(getMimeHeaders(req), req.getInputStream());
			try {
				DOMSOAPMessage(requestMessage,message);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
			System.out.println("-------接收-------");
			requestMessage.writeTo(System.out);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}
	}
	 private MimeHeaders getMimeHeaders(HttpServletRequest req){
		  Enumeration httpHeaders = req.getHeaderNames();
		  MimeHeaders headers = new MimeHeaders();
		  
		  while(httpHeaders.hasMoreElements()){
		   String httpHeaderName = (String)httpHeaders.nextElement();
		   String httpHeaderValue = req.getHeader(httpHeaderName);
		   StringTokenizer httpHeaderValues =
		    new StringTokenizer(httpHeaderValue, ",");
		   while(httpHeaderValues.hasMoreElements()){
		    String headerValue = httpHeaderValues.nextToken().trim();
		    headers.addHeader(httpHeaderName, headerValue);
		   }
		  }
		  return headers;
		 }
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
       
		// ConnectionRequest();
		// System.out.println( resp.getContentType());

		// System.out.println("-------3----");
		// PrintWriter out = resp.getWriter();
//		 Get Authorization header
//		 Authenticator authenticator =getCwmpSoapContext().getAuthenticator();//设置权限
//		 MimeHeaders headers = this.getHeaders(req);
		// InputStream is = req.getInputStream();
		// SOAPMessage msg = mf.createMessage(headers, is);
		// SOAPMessage reply = this.onMessage(msg);
//		 if (!authenticator.authenticate(req, resp)) {
//		 resp.setHeader("WWW-Authenticate", "BASIC realm=\"ACS\"");
//		 resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//		 resp.getOutputStream().write("error !!!".getBytes());
//		 } else {
//		// Allowed, so show him the secret stuff
		// out.println("Top-secret stuff");
		// resp.getOutputStream().write("hello world".getBytes());
//		 }
		// CwmpSoapContext cwmpSoapContext = getCwmpSoapContext();
		// System.out.println("Get Request: " + req.getMethod());
//		InputStream in = req.getInputStream();
//		InputStreamReader ir = new InputStreamReader(in);
//		BufferedReader input = new BufferedReader(ir);
//		String line;
//		String str = "";
//		while ((line = input.readLine()) != null) {
//			if (line.trim().length() <= 0)
//				continue;
//			str += line;
//		}
//		i = i + 1;
//		// System.out.println(i +"===="+str);
//		StringXML(str, i);// 写入xml文件
		SOAPMResponse(req ,resp);
		

	}
	/**
	 * 请求CPE的SOAP
	 * @throws IOException 
	 */
	public  void SOAPMResponse(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		Message message = null;
//		 message = new GetRPCMethodsResponse();
		message = new GetRPCMethods();
//		// message = new Inform();
//		 message = new InformResponse();
//		 GetSOAPMessage(req,resp,message);
		
//		// message = new UploadResponse();
//		// message = new SetParameterValuesResponse();
////		 message = new SetParameterValues();
//		 message = new GetParameterValues();
////		message = new SetParameterValues();
////		message= new Download();
		 GetSOAPMessage(req,resp,message);
		CwmpSoapContext cwmpSoapContext = getCwmpSoapContext();
		SoapMessageBuilder builder = cwmpSoapContext.getSoapMessageBuilder();
		SOAPMessage soapMessage = null;
		try {
			soapMessage = builder.build(message);
			resp.setContentType("text/xml");
			OutputStream outputStream = resp.getOutputStream();
			soapMessage.saveChanges();
			soapMessage.writeTo(outputStream);
//			soapMessage.writeTo(System.out);
//			
		} catch (SOAPException e) {
			e.printStackTrace();
		}
//	
	}
	public static void main(String[] args) {
		BundleContext context=null;
		Hello h = new Hello(context);
		
          h.ACSCallCPE();
		
	
	}
	/**
	 * ACS呼叫cpe
	 */
	public void ACSCallCPE(){
		CwmpSoapContext cwmpSoapContext = getCwmpSoapContext();
		SoapMessageBuilder builder = cwmpSoapContext.getSoapMessageBuilder();
		SOAPMessage reqMsg = null;
//		Message message= new GetRPCMethods();//pass
//		Message message= new SetParameterValues();//errot(Client fault)
//		Message message = new GetParameterValues();//error(验证违反约束：数据类型的元素<ParameterNames>不匹配)
		Message message = new  GetParameterNames("",true);//pass
//		Message message = new SetParameterAttributes();//没有返回值， 一直在等待
//		Message message = new GetParameterAttributes();//error(验证违反约束：数据类型的元素<ParameterNames>不匹配)
//		Message message= new AddObject("TestKey","TestValue");/?
//		Message message = new  DeleteObject("TestKey","TestValue");/?
//		Message message = new Reboot();//pass
//		Message message= new Download();//pass


		
//		Message message = new Fault("SOAP-ENV:Client","GetParameterValues");
		try 
        { 
            SOAPConnectionFactory conntools=SOAPConnectionFactory.newInstance();        
             SOAPConnection conn=conntools.createConnection();           
             reqMsg = builder.build(message);
            URL endPoint =new URL("http://192.168.9.38:7547"); 
            System.out.println("\n====================发送的消息:"); 
           
            reqMsg.writeTo(System.out);
//            int i=0;
//            WritSOAPMessage(reqMsg,i);
//            SOAPMessage s=   conn.get("http://192.168.9.38:7547");
//            s.writeTo(System.out);
            SOAPMessage respMsg = conn.call(reqMsg, endPoint);     
           // System.out.println("\n服务端返回的信息- : " + getResult(respMsg));                     
             System.out.println("\n\n====================接收的消息"); 
//             FileOutputStream  out= new FileOutputStream(new File("GetPRMethods.xml"));
             respMsg.writeTo(System.out); 
//            i=i+1;
//            WritSOAPMessage(reqMsg,i);    
        }catch(Exception e){
            	e.printStackTrace();
            }
	}
	/**
	 * 解析SOAPMessage 获取ID
	 * 
	 * @param reply
	 * @throws SOAPException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static SOAPMessage DOMSOAPMessage(SOAPMessage reply,Message message)
			throws SOAPException, ParserConfigurationException, SAXException,
			IOException {
		SOAPPart soapPart = reply.getSOAPPart();

		Source source = soapPart.getContent();
	String	 str=reply.getSOAPBody().getChildNodes().item(0).getChildNodes().item(2).getFirstChild().getNodeValue();
	String ID=reply.getSOAPHeader().getChildNodes().item(0).getFirstChild().getNodeValue();
	message.setId(ID);
	
	String maxE=reply.getSOAPBody().getChildNodes().item(0).getChildNodes().item(2).getFirstChild().getNodeValue();
//	InformResponse  inform= new InformResponse();
//	message.setMaxEnvelopes(Integer.parseInt(maxE));
//	System.out.println(ID+"-------"+maxE);
 reply.saveChanges();
		return reply;

	}

}
