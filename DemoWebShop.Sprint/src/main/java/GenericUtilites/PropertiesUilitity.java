package GenericUtilites;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUilitity {
	
		public String readofdataPropertiesfile(String key) throws IOException {

	        FileInputStream fis =new FileInputStream("./src/main/resources/commonData.Properties");

	        Properties p = new Properties();

	        p.load(fis);

	        return p.getProperty(key);
	    }
	}
	