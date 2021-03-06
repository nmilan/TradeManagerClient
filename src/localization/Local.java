package localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class Local {
	private static final String bundleFile = "localization/Jezik";
	private static String locale = ResourceBundle.getBundle("properties/settings").getString("locale");
	private static ResourceBundle rb = ResourceBundle.getBundle(bundleFile, new Locale(locale));
	
	public static void setLocale (String loc) {
		locale = loc;
		rb = ResourceBundle.getBundle(bundleFile, new Locale(locale));
	}
	
	public static String getLocale() {
		return locale;
	}
	
	public static String getString (String key){
		try{
			return rb.getString(key);
		}catch (Exception e) {
			return key;
		}
	}
}
