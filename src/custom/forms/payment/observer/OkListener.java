package custom.forms.payment.observer;

import java.util.EventListener;


public interface OkListener extends EventListener{
	void okPerformed(OkEvent event);
}
