import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.text.DecimalFormat;

public class POSFrame extends JFrame implements ActionListener{
	private boolean newOrder = false;
	private JTextField tenderedAmountTF;
	private JTable table_1;
	private JButton payBtn,sm1btn,sm2btn,sm3btn,sm4btn,med1btn,med2btn,med3btn,med4btn,lg1btn,lg2btn,lg3btn,lg4btn;
	private JLabel lblTax,lblItemCount,lblSubtotal,lblTotal,lblTenderedAmount,lblChange;
	//Cart item count
	//CartTotal- subtotal with no tax included
	private int CartItemCount = 0;
	private double CartTotal = 0;
	//Enter Local Tax (NYC ~ 8875%)
	private double TaxAmount = 0.08875;
	private JButton DeleteBtn;
	
	
	public POSFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("POS");
		setSize(1400,800);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel ItemsPanel = new JPanel();
		ItemsPanel.setBounds(864, 11, 512, 752);
		getContentPane().add(ItemsPanel);
		ItemsPanel.setLayout(null);
		
		tenderedAmountTF = new JTextField();
		tenderedAmountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		tenderedAmountTF.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tenderedAmountTF.setBounds(205, 662, 147, 24);
		ItemsPanel.add(tenderedAmountTF);
		tenderedAmountTF.setColumns(10);
		
		lblSubtotal = new JLabel("Subtotal: $0");
		lblSubtotal.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSubtotal.setBounds(30, 561, 472, 33);
		ItemsPanel.add(lblSubtotal);
		
		lblTotal = new JLabel("Total: $0");
		lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTotal.setBounds(30, 620, 472, 33);
		ItemsPanel.add(lblTotal);
		
		lblTenderedAmount = new JLabel("Tendered Amount: ");
		lblTenderedAmount.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTenderedAmount.setBounds(30, 655, 165, 33);
		ItemsPanel.add(lblTenderedAmount);
		
		lblItemCount = new JLabel("Item Count: 0");
		lblItemCount.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblItemCount.setBounds(30, 529, 262, 33);
		ItemsPanel.add(lblItemCount);
		
		payBtn = new JButton("Pay");
		payBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double tempTendered= 0;
//				reads in tendered amount and makes sure its a digit and make sure the digits do not overflow
				try {
					tempTendered = Double.parseDouble(tenderedAmountTF.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "DIGITS ONLY");
				}
				if(tempTendered<(CartTotal*(1+TaxAmount))) {
					JOptionPane.showMessageDialog(null, "TENDERED AMOUNT LESS THAN TOTAL");
				}
				else if (tempTendered>1000000) {
					JOptionPane.showMessageDialog(null, "PLEASE ENTER AN AMOUNT UNDER 999999 ");
				}
				else {
//					if requirements are met change will be displayed and POS will reset on next item
					lblChange.setText("Change: $" + new DecimalFormat("0.00").format(tempTendered - (TaxAmount+1)*CartTotal));
					newOrder = true;
				}
			}
		});
		payBtn.setBounds(380, 662, 89, 23);
		ItemsPanel.add(payBtn);
		
		lblChange = new JLabel("Change: $0");
		lblChange.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblChange.setBounds(30, 697, 472, 33);
		ItemsPanel.add(lblChange);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 21, 452, 497);
		ItemsPanel.add(scrollPane);
		
		table_1 = new JTable();
		scrollPane.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Item", "Unit Price"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		lblTax = new JLabel("Tax: $0");
		lblTax.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTax.setBounds(30, 590, 472, 33);
		ItemsPanel.add(lblTax);
		
		DeleteBtn = new JButton("DELETE ITEM");
//		Deltes an item and updates the cart total
		DeleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow =  table_1.getSelectedRow();
				if(selectedRow>=0) {
					DefaultTableModel model = (DefaultTableModel) table_1.getModel();
//					mode.getValueAt() returns an object.
//					Needs to ve converted to string the double to update cart total
					String TempItemPrice = model.getValueAt(selectedRow, 1).toString();
					CartTotal -= Double.parseDouble(TempItemPrice);
					lblItemCount.setText("Item Count: "+ Integer.toString(--CartItemCount));
					lblSubtotal.setText("Subtotal: $" + new DecimalFormat("0.00").format(CartTotal));
					lblTax.setText("Tax: $" + new DecimalFormat("0.00").format(CartTotal*TaxAmount));
					lblTotal.setText("Total: $" + new DecimalFormat("0.00").format((TaxAmount+1)*CartTotal));
//					removes the selected row. Done last to extract deleted item price for recalculation.
					model.removeRow(selectedRow);
					
				}
				else {
					JOptionPane.showMessageDialog(null, "SELECT A ROW");
				}
			}
		});
		DeleteBtn.setBounds(356, 527, 113, 23);
		ItemsPanel.add(DeleteBtn);
		table_1.getColumnModel().getColumn(0).setResizable(false);
		table_1.getColumnModel().getColumn(1).setResizable(false);
		JPanel CartPanel = new JPanel();
		CartPanel.setBounds(10, 11, 848, 752);
		getContentPane().add(CartPanel);
		CartPanel.setLayout(null);

//		Buttons with names of items
		sm1btn = new JButton("SM COFFEE");
		sm1btn.setBackground(new Color(255, 128, 128));
		sm1btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		sm1btn.setBounds(20, 28, 136, 120);
		sm1btn.addActionListener(this);
		CartPanel.add(sm1btn);
		
		sm2btn = new JButton("SM LATTE");
		sm2btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		sm2btn.setBackground(new Color(255, 128, 128));
		sm2btn.setBounds(20, 215, 136, 120);
		sm2btn.addActionListener(this);
		CartPanel.add(sm2btn);
		
		sm3btn = new JButton("SM CHOCO\r\n");
		sm3btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		sm3btn.setBackground(new Color(255, 128, 128));
		sm3btn.setBounds(20, 400, 136, 120);
		sm3btn.addActionListener(this);
		CartPanel.add(sm3btn);
		
		sm4btn = new JButton("SM TEA");
		sm4btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		sm4btn.setBackground(new Color(255, 128, 128));
		sm4btn.setBounds(20, 594, 136, 120);
		sm4btn.addActionListener(this);
		CartPanel.add(sm4btn);
		
		med1btn = new JButton("MED COFFEE");
		med1btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		med1btn.setBackground(new Color(128, 255, 255));
		med1btn.setBounds(342, 28, 136, 120);
		med1btn.addActionListener(this);
		CartPanel.add(med1btn);
		
		med2btn = new JButton("MED LATTE");
		med2btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		med2btn.setBackground(new Color(128, 255, 255));
		med2btn.setBounds(342, 215, 136, 120);
		med2btn.addActionListener(this);
		CartPanel.add(med2btn);
		
		med3btn = new JButton("MED\t CHOCO");
		med3btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		med3btn.setBackground(new Color(128, 255, 255));
		med3btn.setBounds(342, 400, 136, 120);
		med3btn.addActionListener(this);
		CartPanel.add(med3btn);
		
		med4btn = new JButton("MED TEA");
		med4btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		med4btn.setBackground(new Color(128, 255, 255));
		med4btn.setBounds(342, 594, 136, 120);
		med4btn.addActionListener(this);
		CartPanel.add(med4btn);
		
		lg1btn = new JButton("LG\r\n COFFEE");
		lg1btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		lg1btn.setBackground(new Color(255, 128, 192));
		lg1btn.setBounds(658, 28, 136, 120);
		lg1btn.addActionListener(this);
		CartPanel.add(lg1btn);
		
		lg2btn = new JButton("LG\r\n COFFEE");
		lg2btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		lg2btn.setBackground(new Color(255, 128, 192));
		lg2btn.setBounds(658, 215, 136, 120);
		lg2btn.addActionListener(this);
		CartPanel.add(lg2btn);
		
		lg3btn = new JButton("LG CHOCO");
		lg3btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		lg3btn.setBackground(new Color(255, 128, 192));
		lg3btn.setBounds(658, 400, 136, 120);
		lg3btn.addActionListener(this);
		CartPanel.add(lg3btn);
		
		lg4btn = new JButton("LG\r\n TEA");
		lg4btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		lg4btn.setBackground(new Color(255, 128, 192));
		lg4btn.setBounds(658, 594, 136, 120);
		lg4btn.addActionListener(this);
		CartPanel.add(lg4btn);
		
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		DefaultTableModel model = (DefaultTableModel) table_1.getModel();
//		resets POs if newOrder is set to true(after pay)
		if(newOrder == true) {
			CartTotal = 0;
			CartItemCount = 0;
			model.setRowCount(0);
			lblChange.setText("Change: $0");
			tenderedAmountTF.setText("");
			newOrder = false;
		}
//		Changes item price and item name based on what buttons are pressed
		double itemPrice = 0;
		String itemName = "NULL";
		if(arg0.getSource()== sm1btn) {
			itemPrice = 3.25;
			itemName = "SMALL COFFEE";
		}
		else if(arg0.getSource()==med1btn) {
			itemPrice = 3.45;
			itemName = "MEDIUM COFFEE";
		}
		else if(arg0.getSource()==lg1btn) {
			itemPrice = 3.75;
			itemName = "LARGE COFFEE";
		}
		else if(arg0.getSource()==sm2btn) {
			itemPrice = 4.95;
			itemName = "SMALL LATTE";
		}
		else if(arg0.getSource()==med2btn) {
			itemPrice = 5.45;
			itemName = "MEDIUM LATTE";
		}
		else if(arg0.getSource()==lg2btn) {
			itemPrice = 5.65;
			itemName = "LARGE LATTE";
		}
		else if(arg0.getSource()==sm3btn) {
			itemPrice = 4.25;
			itemName = "SMALL CHOCOLATE";
		}
		else if(arg0.getSource()==med3btn) {
			itemPrice = 4.65;
			itemName = "MEDIUM CHOCOLATE";
		}
		else if(arg0.getSource()==lg3btn) {
			itemPrice = 4.95;
			itemName = "LARGE CHOCOLATE";
		}
		else if(arg0.getSource()==sm4btn) {
			itemPrice = 3.45;
			itemName = "SMALL TEA";
		}
		else if(arg0.getSource()==med4btn) {
			itemPrice = 3.75;
			itemName = "MEDIUM TEA";
		}
		else if(arg0.getSource()==lg4btn) {
			itemPrice = 3.95;
			itemName = "LARGE TEA";
		}
		
		model.addRow(new Object[] {itemName,itemPrice});
//		subtotal, item count, tax and total is recalculated based on cart with decimal format 0.00.
		CartTotal += itemPrice;
		lblItemCount.setText("Item Count: "+ Integer.toString(++CartItemCount));
		lblSubtotal.setText("Subtotal: $" + new DecimalFormat("0.00").format(CartTotal));
		lblTax.setText("Tax: $" + new DecimalFormat("0.00").format(CartTotal*TaxAmount));
		lblTotal.setText("Total: $" + new DecimalFormat("0.00").format((TaxAmount+1)*CartTotal));

	}
}
