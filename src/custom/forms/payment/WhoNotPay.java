package custom.forms.payment;

import generic.MessagePanel;
import generic.form.printProcessors.GenericPrintProcessor;
import hibernate.entityBeans.StockDocument;
import hibernate.facades.MetadataFacade;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalExclusionType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import actions.ActionManager;
import app.Appliction;


import remotes.RemotesManager;
import util.ServerResponse;

import layouts.RiverLayout;
import localization.Local;
import model.metadata.EntityField;
import model.metadata.EntityMetadata;


public class WhoNotPay extends JPanel{
	private JPanel mainPanel = null;
	private WhoNotPayTable whoNotPayTable;
	private EntityMetadata entity;
	private JScrollPane scrollPane;
	private static WhoNotPay instance = null;
	private JToolBar toolBar;
	private List<Object> stockDocuments;
	private JLabel allDeptLabel;
	
	public WhoNotPay() {
		ServerResponse metaResponse = RemotesManager.getInstance()
				.getMetadataRemote().getEntityXml("StockDocument.xml");
		EntityMetadata em = new EntityMetadata((String) metaResponse.getData());
		this.entity = em;
		whoNotPayTable = new WhoNotPayTable();
		init();
		addComponents();
		
	}
	private void init() {
		
		scrollPane = new JScrollPane(whoNotPayTable);
		setLayout(new BorderLayout());
		toolBar = new JToolBar();
		toolBar.add(ActionManager.getInstance().getWhoNotPayPrintAction());
		toolBar.setEnabled(false);
		allDeptLabel = new JLabel("Ukupan dug van valute: ");
		
	}
	private void addComponents() {
		JPanel panel = new JPanel();
		panel.setLayout(new RiverLayout());
		panel.add("vfill hfill", scrollPane);
		add(panel, BorderLayout.CENTER);
		add(toolBar,BorderLayout.NORTH);
		add(allDeptLabel,BorderLayout.SOUTH);
		searchStockDocument();
		getAllDept();
	
	}
	public void getAllDept(){
		WhoNotPayTableModel model = (WhoNotPayTableModel) whoNotPayTable
				.getModel();
		allDeptLabel.setText("Ukupan dug van valute: "+model.getAllDept());
	}
	public JTable getWhoNotPayTable() {
		return whoNotPayTable;
	}
	public void searchStockDocument() {
			ServerResponse response = getEntity();
			MessagePanel messPanel = new MessagePanel(Local.getString(response
					.getResponseCode()), response.getResponseMessage());
			if (response.getSeverity() == ServerResponse.INFO) {
				WhoNotPayTableModel model = (WhoNotPayTableModel) whoNotPayTable
						.getModel();
				this.stockDocuments = (List<Object>) response.getData();
				model.setStockDocuments((List<Object>) response.getData());
				whoNotPayTable.updateUI();
			
			} else {
				JOptionPane.showMessageDialog(this, messPanel,
						Local.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
			}
		
		}
	public ServerResponse getEntity() {
		Map<String, Object> searchMap = new HashMap<String, Object>();
				Date date = new Date();
				date.setMonth(date.getMonth()-1);
				SimpleDateFormat simp = new SimpleDateFormat("dd/MM/yyyy");
				searchMap.put("CreationTime","<="+simp.format(date));
				EntityField f = entity.getFieldByName("Paid");
				searchMap.put("Paid", "false");
				searchMap.put("DocumentTypeID", "2");
				searchMap.put("Canceled", "false");
				ServerResponse response = RemotesManager.getInstance()
						.getGenericPersistenceRemote()
						.searchSelect(entity, searchMap, 10000, "id", null);
				return response;
		}
	public static WhoNotPay getInstance() {
		if (instance == null) {
			instance = new WhoNotPay();
			return instance;
		}
		instance.searchStockDocument();
		return instance;
	}
	public void print() {
			new Thread(){
				public void run() {
					Appliction.getInstance().getPopupProgressBar().setVisible(true);
					List<Object> stockdocList = new ArrayList<Object>();
					stockdocList.add(stockDocuments.get(0));
					GenericPrintProcessor process = new GenericPrintProcessor(stockdocList);
					GenericPrintProcessor itemsProcessor = new GenericPrintProcessor(new ArrayList<Object>(stockDocuments));
					try {
						JasperReport report = RemotesManager.getInstance().getReportingRemote().getJasperReport("WhoNotPayReport.jrxml");
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ItemsDataSource", itemsProcessor);
						map.put("MetadataURL", MetadataFacade.METADATA_URL);
						JasperPrint print = RemotesManager.getInstance().getReportingRemote().fillJasperReport(report, map, process);
						JasperViewer jrViewer = new JasperViewer(print, false);
						jrViewer.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
						
						jrViewer.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					} finally{
						Appliction.getInstance().getPopupProgressBar().setVisible(false);
					}
				}
			}.start();
	}
	
}
