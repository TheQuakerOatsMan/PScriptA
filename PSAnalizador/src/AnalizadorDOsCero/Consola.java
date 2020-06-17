package AnalizadorDOsCero;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class Consola extends JDialog{

	private static final long serialVersionUID = 1L;
	JButton in, ca;
	JLabel fondoGay;
	JPasswordField pass;
	JScrollPane sp2s, sp2;
	JTextPane consol, consolS;
	
	public Consola(JFrame padre, boolean modal, String consola, String consolaS) {

		super(padre, modal);
		setTitle("Consolas");
		setSize(1240, 720);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		consol = new JTextPane();
		consol.setEditable(false);
		consol.setForeground(Color.WHITE);
		consol.setBackground(Color.DARK_GRAY);
		consol.setFont(new Font("Tahoma", Font.PLAIN, 16));

		consolS = new JTextPane();
		consolS.setEditable(false);
		consolS.setForeground(Color.WHITE);
		consolS.setBackground(Color.GRAY);
		consolS.setFont(new Font("Tahoma", Font.PLAIN, 16));

		sp2s = new JScrollPane(consolS);
		sp2s.setBounds(553, 555, 552, 109);
		sp2s.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp2s.getVerticalScrollBar().setBackground(SystemColor.control);
		sp2s.setBackground(Color.white);
		sp2s.setOpaque(true);

		sp2 = new JScrollPane(consol);
		sp2.setBounds(0, 555, 552, 109);
		sp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp2.getVerticalScrollBar().setBackground(SystemColor.control);
		sp2.setBackground(Color.white);
		sp2.setOpaque(true);
		
		consol.setText(consola);
		consolS.setText(consolaS);
				
		JPanel consolas = new JPanel(new GridLayout(2,1));
		consolas.add(sp2);
		consolas.add(sp2s);
		
		add(consolas, BorderLayout.CENTER);
	}


}
