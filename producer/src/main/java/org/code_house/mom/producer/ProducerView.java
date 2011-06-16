package org.code_house.mom.producer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public interface ProducerView {

	JLabel getStatusBar();
	JComboBox getClientComboBox();
	JComboBox getCurrencyComboBox();

	JTextArea getMessageArea();
	JButton getSubmitButton();

	JTextField getAmountField();

}
