package org.code_house.mom.producer;

import java.awt.Component;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.code_house.mom.domain.Client;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Form control.
 * 
 * @author ldywicki
 */
@SuppressWarnings("all")
public class ProducerWindow extends JFrame implements ProducerView, InitializingBean, DisposableBean {

	private static final long serialVersionUID = -6089606546968564636L;

	private JComboBox clientCombo;
	private JComboBox currencyCombo;
	private JTextField amountField;
	private JLabel statusBar;
	private JTextArea messageArea;
	private JButton submitButton;

	public ProducerWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);

		setLayout(new MigLayout("wrap 2", "[right][grow]", "[][][][grow][]"));
		
		setMenuBar(new MenuBar());
	}

	private void createTop() {
		add(new JLabel("Client"));

		clientCombo = new JComboBox();
		clientCombo.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
				if (value != null) {
					return new JLabel(((Client) value).getName());
				}
				return new JLabel("Select client");
			}
		});
		add(clientCombo, "grow");

		add(new JLabel("Money"));
		amountField = new JTextField();
		add(amountField, "grow");

		add(new JLabel("Currency"));
		currencyCombo = new JComboBox();
		add(currencyCombo, "grow");
	}

	private void createCenter() {
		add(new JLabel("Message"), "top");
		messageArea = new JTextArea();
		messageArea.setEnabled(false);
		messageArea.setText("{test}");
		add(messageArea, "grow");

		submitButton = new JButton("Send");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		add(submitButton, "grow, span 2, wrap");
	}

	private void createBottom() {
		statusBar = new JLabel("Status bar");
		statusBar.setBorder(new EtchedBorder());
		getContentPane().add(statusBar, "dock south");
	}

	public static void main(String[] args) {
		ProducerWindow window = new ProducerWindow();
		window.setVisible(true);
	}

	public void destroy() throws Exception {
		destroy();
	}

	public void afterPropertiesSet() throws Exception {
		createTop();
		createCenter();
		createBottom();

		setVisible(true);
	}

	public JLabel getStatusBar() {
		return statusBar;
	}

	public JComboBox getClientComboBox() {
		return clientCombo;
	}

	public JComboBox getCurrencyComboBox() {
		return currencyCombo;
	}

	public JTextField getAmountField() {
		return amountField;
	}

	public JTextArea getMessageArea() {
		return messageArea;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}
}
