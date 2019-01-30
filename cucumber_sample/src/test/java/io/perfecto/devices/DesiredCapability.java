package io.perfecto.devices;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.perfecto.utils.Logger;
import io.perfecto.utils.Logger.STATUS;
import io.perfecto.utils.PerfectoCloudInformation;
import io.perfecto.utils.PerfectoCloudInformation.INFORMATION;

public class DesiredCapability {

	public static DesiredCapabilities getDesiredCapabilities(Device deviceInformation) {
		
		PerfectoCloudInformation perfectoCloudInformation = PerfectoCloudInformation.getCloudInformation();
		
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

		if(perfectoCloudInformation.getInformation(INFORMATION.SECURITY_TOKEN).isEmpty()) {
			if(!perfectoCloudInformation.getInformation(INFORMATION.USER_NAME).isEmpty()) {
				desiredCapabilities.setCapability("user", perfectoCloudInformation.getInformation(INFORMATION.USER_NAME));
			}
			
			if(!perfectoCloudInformation.getInformation(INFORMATION.PASSWORD).isEmpty()) {
				desiredCapabilities.setCapability("password", perfectoCloudInformation.getInformation(INFORMATION.PASSWORD));
			}
		}else {
			desiredCapabilities.setCapability("securityToken", perfectoCloudInformation.getInformation(INFORMATION.SECURITY_TOKEN));
		}
		
		Map<String, String> capabilities = deviceInformation.getDeviceAttributes();

		Set<Entry<String, String>> entries = capabilities.entrySet();

		Iterator<Entry<String, String>> entryIterator = entries.iterator();

		Entry<String, String> currentEntry;

		while (entryIterator.hasNext()) {
			currentEntry = entryIterator.next();
			desiredCapabilities.setCapability(currentEntry.getKey(), currentEntry.getValue());
		}

		if (capabilities.containsKey("platformName")) {
			switch (capabilities.get("platformName").toUpperCase()) {
			case "ANDROID":
//				AndroidAppConstant androidAppConst = AndroidAppConstant.getAppConstant();
//				androidAppConst.setAppConstants(desiredCapabilities);
				break;
			case "IOS":
//				IOSAppConstant iOSAppConst = IOSAppConstant.getAppConstant();
//				iOSAppConst.setAppConstants(desiredCapabilities);
				break;
			}

		} else {
			Logger.log(STATUS.FATAL,
					"Platform Name is Mandatory. Please provide platformName parameter in device_selection.properties");

		}
		
//		desiredCapabilities.setCapability("ignoreUnimportantViews", true);

		return desiredCapabilities;

	}

}
