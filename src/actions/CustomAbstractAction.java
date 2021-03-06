package actions;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class CustomAbstractAction extends AbstractAction implements Serializable{

	public Icon loadIcon(String fileName) {
		URL imageURL = getClass().getResource("/icons/" + fileName);
		Icon icon = null;

		if (imageURL != null) {
			icon = new ImageIcon(imageURL);
		}
		else {
			System.err.println("Resource not found: " + fileName);
		}

		return icon;
	}
	@Override
	public abstract void actionPerformed(ActionEvent e);
}
