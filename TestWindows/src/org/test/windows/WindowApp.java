package org.test.windows;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class WindowApp {

	private JFrame frmSignIn;
	private JTextField mailField;
	private JPasswordField passwordField;
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
	public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,}$");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowApp window = new WindowApp();
					window.frmSignIn.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static boolean validateEmail(String email) {
		Matcher mch = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		
		return mch.find();
	}
	
	public static boolean validatePassword(String password) {
		Matcher mch = PASSWORD_PATTERN.matcher(password);
		
		return mch.find();
	}
	
	/**
	 * Create the application.
	 */
	public WindowApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSignIn = new JFrame("GUI");
		frmSignIn.setBackground(UIManager.getColor("Button.light"));
		frmSignIn.setTitle("Sign In");
		frmSignIn.getContentPane().setBackground(new Color(211, 211, 211));
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Metal".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Metal is not available, you can set the GUI to another look and feel.
			e.printStackTrace();
		}
		
		JLabel lblNewLabel = new JLabel("Login Credentials");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 19));
		
		JLabel lblUsername = new JLabel("Username : ");
		lblUsername.setToolTipText("");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		mailField = new JTextField();
		mailField.setToolTipText("Enter your username");
		lblUsername.setLabelFor(mailField);
		mailField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mailField.setColumns(20);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("Enter your password\r\n");
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField.setColumns(20);
		
		JLabel errLabel = new JLabel("* Your Data is not valid");
		errLabel.setForeground(Color.RED);
		errLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		errLabel.setVisible(false);
		
		JButton btnLogin = new JButton("Log in");
		btnLogin.setBackground(Color.LIGHT_GRAY);
		btnLogin.setToolTipText("Click to log in");
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isValidUsername = false, isValidPassword = false, userExists = false;
				SQLiteConn conn = new SQLiteConn();
				
				String mail = mailField.getText().trim();
				String pass = String.valueOf(passwordField.getPassword()).trim();
				
				if (!mail.isEmpty()) {
					isValidUsername = validateEmail(mail);
				}
				if (!pass.isEmpty()) {
					isValidPassword = validatePassword(pass);
				}
				
				if (isValidPassword && isValidUsername) {
					userExists = conn.findUser(mail, pass);
					
					if (userExists) {
						new ShowUsers();
						
						frmSignIn.hide();
						mailField.setText("");
						passwordField.setText("");
						
						if (errLabel.isVisible()) {
							errLabel.hide();
						}
						
					} else {
						errLabel.setText("* User doesn't exist");
						errLabel.setVisible(true);
					}
				} else {
					errLabel.setVisible(true);
					
					mailField.setText("");
					passwordField.setText("");
				}
			}
		});
		
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		GroupLayout groupLayout = new GroupLayout(frmSignIn.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(78)
							.addComponent(lblUsername))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(82)
							.addComponent(lblPassword)))
					.addGap(46)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(errLabel, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
						.addComponent(mailField, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
						.addComponent(passwordField, 0, 0, Short.MAX_VALUE))
					.addContainerGap(182, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(30)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsername)
						.addComponent(mailField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(41)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(33)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addComponent(errLabel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addGap(24))
		);
		frmSignIn.getContentPane().setLayout(groupLayout);
		frmSignIn.setBounds(100, 100, 594, 373);
		frmSignIn.setLocationRelativeTo(null);
		frmSignIn.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder());
		frmSignIn.setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		mnMenu.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mnMenu.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mnMenu);
		
		JMenuItem mntmExitalt = new JMenuItem("Exit");
		mntmExitalt.setHorizontalAlignment(SwingConstants.LEFT);
		
		mntmExitalt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSignIn.dispatchEvent(new WindowEvent(frmSignIn, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		mntmExitalt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		mntmExitalt.setHorizontalAlignment(SwingConstants.LEFT);
		mntmExitalt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnMenu.add(mntmExitalt);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmGetHelp = new JMenuItem("Get Help");
		mntmGetHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String info = "Enter your credentials and gain access to the Database in a secure way.\nAll your"+
						"sensitive information is encrypted via AES-128 bit encryption standards.\n\n Copyright \u00a9 " +
						Calendar.getInstance().get(Calendar.YEAR);
				JOptionPane.showMessageDialog(mntmGetHelp, info, "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		mntmGetHelp.setHorizontalAlignment(SwingConstants.LEFT);
		mnHelp.add(mntmGetHelp);
	}
}
