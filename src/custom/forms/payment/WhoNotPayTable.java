package custom.forms.payment;


import hibernate.entityBeans.BusinessPartner;
import hibernate.entityBeans.StockDocument;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class WhoNotPayTable extends JTable implements MouseListener{
	public WhoNotPayTable() {
		super();
		WhoNotPayTableModel model = new WhoNotPayTableModel();
		setModel(model);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int i = getSelectedRow();
		if(e.getClickCount() == 2){
			BusinessPartner partner =((StockDocument)((WhoNotPayTableModel) getModel()).getStockDocuments().get(i)).getBusinessPartner();
			JOptionPane.showMessageDialog(null, partner.toString(), "Podaci o duzniku", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
