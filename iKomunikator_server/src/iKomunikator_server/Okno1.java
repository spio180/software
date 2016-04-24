package iKomunikator_server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Okno1 {

	private JFrame frame;
	private JTextField textIp;
	private JTextField textPort;
	private JTextField textLimit;
	private static String[] slowa_zakazane = {"motyla noga", "ale urwa³", "kurde bele"};
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Okno1 window = new Okno1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Okno1() {
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 642, 507);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JLabel lblIp = new JLabel("Adres IP");
		lblIp.setBounds(10, 11, 110, 30);
		frame.getContentPane().add(lblIp);
		
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(10, 52, 110, 30);
		frame.getContentPane().add(lblPort);
		
		
		JLabel lblLimit = new JLabel("Limit Po\u0142\u0105cze\u0144");
		lblLimit.setBounds(10, 93, 110, 30);
		frame.getContentPane().add(lblLimit);
		
		
		textIp = new JTextField();
		textIp.setText("127.0.0.1");
		textIp.setBounds(130, 21, 114, 20);
		frame.getContentPane().add(textIp);
		textIp.setColumns(10);
		
		
		
		textPort = new JTextField();
		textPort.setText("65535");
		textPort.setBounds(130, 62, 114, 20);
		frame.getContentPane().add(textPort);
		textPort.setColumns(10);
		
		
		textLimit = new JTextField();
		textLimit.setText("999");
		textLimit.setBounds(130, 103, 114, 20);
		frame.getContentPane().add(textLimit);
		textLimit.setColumns(10);
		
		
		
		JButton btnZmien = new JButton("Zmie\u0144");
		btnZmien.setBounds(117, 368, 89, 23);
		frame.getContentPane().add(btnZmien);
		
		
		JButton btnUsun = new JButton("Usu\u0144");
		btnUsun.setBounds(216, 368, 89, 23);
		frame.getContentPane().add(btnUsun);
		
		
		JButton btnOdswiez = new JButton("Od\u015Bwie\u017C");
		btnOdswiez.setBounds(315, 368, 89, 23);
		frame.getContentPane().add(btnOdswiez);
		
		
		JButton btnDodaj = new JButton("Dodaj");
		btnDodaj.setBounds(10, 368, 89, 23);
		frame.getContentPane().add(btnDodaj);
		
		
		JButton btnZapisz = new JButton("Zapisz");
		btnZapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (Integer.parseInt(textPort.getText())>65535) {
					JOptionPane.showMessageDialog(null, "Wpisz poprawny port w zakresie 1-65535");
				}
				else if (Integer.parseInt(textLimit.getText())>999) {
					JOptionPane.showMessageDialog(null, "Wpisz poprawny limit po³¹czeñ w zakresie 1-999");
				}
				else {
						String ip= textIp.getText();
						int port= Integer.parseInt(textPort.getText());
						int limit= Integer.parseInt(textLimit.getText());
						XmlWriter1 write= new XmlWriter1(ip, port, limit);
						try {
							write.SerializeServerConfig();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				}
				
			}
		});
		btnZapisz.setBounds(428, 435, 89, 23);
		frame.getContentPane().add(btnZapisz);
		
		
		JButton btnAnuluj = new JButton("Anuluj");
		btnAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});		
		btnAnuluj.setBounds(527, 435, 89, 23);
		frame.getContentPane().add(btnAnuluj);
		
		
		JList list = new JList(slowa_zakazane);
		list.setBounds(25, 149, 327, 166);
		frame.getContentPane().add(list);
	}
}

