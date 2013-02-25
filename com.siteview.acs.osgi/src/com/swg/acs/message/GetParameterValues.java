/**
 * 
 */
package com.swg.acs.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.swg.acs.Argument;
import com.swg.acs.MessageArgument;
import com.swg.acs.MessageBody;
import com.swg.acs.message.cwmp.ArgumentFactory;
import com.swg.acs.message.cwmp.CwmpMessage;

/**
 * @author satriaprayoga
 *
 */
public class GetParameterValues extends CwmpMessage{

	private static final long serialVersionUID = -909174078440244591L;
	
	private List<String> parameterNames;

	public GetParameterValues() {
		super("GetParameterValues");
		parameterNames=new ArrayList<String>();
		parameterNames.add("Device.Services.UserAgentPort");
		parameterNames.add("Device.Services.RegistrarServer");//添加获取数据列表
		parameterNames.add("Device.Services.RegistrarServerPort");
		parameterNames.add("Device.Services.OutboundProxy");
		parameterNames.add("Device.Services.OutboundProxyPort");
		parameterNames.add("Device.Services.UserAgentDomain");
		parameterNames.add("Device.Services.RegistrationPeriod");
	}

	@Override
	protected void configureBody(MessageBody bodyPart,
			ArgumentFactory argumentFactory) {
		MessageArgument paramArg=bodyPart.addMessageArgument(argumentFactory.createMessageArgument("ParameterNames"));
		paramArg.setAttribute(SOAP_ARRAY_TYPE, "xsd:string["+parameterNames.size()+"]");//+parameterNames.size()+
		
		for(String s:parameterNames){
			Argument param=paramArg.addMessageArgument(argumentFactory.createMessageArgument("string"));
			param.setValue(s);
		}
	}

	@Override
	protected void configureParse(MessageBody messageBody) {
		MessageArgument paramArg=messageBody.getChild("ParameterNames");
		Iterator<MessageArgument> params=paramArg.childIterator();
		parameterNames=new ArrayList<String>();
		while(params.hasNext()){
			MessageArgument arg=params.next();
			parameterNames.add(arg.getValue());
		}
	}
	
	public List<String> getParameterNames() {
		return parameterNames;
	}
	
	public void addParameterNames(String parameterName){
		parameterNames.add(parameterName);
	}

}
