package custom.forms.payment.observer;

public interface OkObservable {
	void addlistener(OkListener listener);
	void removeListener(OkListener listener);
	void fireOkPerformed(OkEvent event);
	
}
