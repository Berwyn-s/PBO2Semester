package uas;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

public class LoginClass extends JFrame  {
	private static final long serialVersionUID = 1L;
	
	MainClass sqlClass;
	JLabel user_label, password_label, message;
	JTextField userName_text;
	JPasswordField password_text;
	JButton submit, register;
   LoginClass() {
	   
	   try {
		   sqlClass = new MainClass();
	   } catch (SQLException e) {
		e.printStackTrace();
	   }
	   
	  Container panel = getContentPane();
     
      user_label = new JLabel("User Name :",SwingConstants.CENTER);
      user_label.setSize(200, 20);
      user_label.setLocation(5, 50);
      
      password_label = new JLabel("Password :", SwingConstants.CENTER);
      password_label.setSize(200, 20);
      password_label.setLocation(5, 140);
      
      userName_text = new JTextField();
      userName_text.setSize(200, 40);
      userName_text.setLocation(150, 40);
          
      password_text = new JPasswordField();
      password_text.setSize(200, 40);
      password_text.setLocation(150, 130);
   
      // Submit
      submit = new JButton("Login");
      submit.setAlignmentX(SwingConstants.CENTER);
      submit.setLocation(210,200);
      submit.setSize(120,40);
     
      
      register = new JButton("Register");
      register.setAlignmentX(SwingConstants.CENTER);
      register.setLocation(70, 200);
      register.setSize(120, 40);
      
      panel.setLayout(null);
      
      panel.add(user_label);
      panel.add(userName_text);
      panel.add(password_label);
      panel.add(password_text);
      panel.add(register);
      panel.add(submit);
      
      CreateHandler rgistHndlr = new CreateHandler();
      register.addActionListener(rgistHndlr);
      
      LoginHandler lginHndlr = new LoginHandler();
      submit.addActionListener(lginHndlr);
      
      setTitle("Login");
      setSize(400,300);
      setMinimumSize(new Dimension(400,300));
      setVisible(true);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
   
   class CreateHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String userName = userName_text.getText();
		    char[] pass = password_text.getPassword();
		    String passWord = String.valueOf(pass);
		    boolean success = false;
		    
			try {
				success = sqlClass.Register(userName, passWord);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if (success)
				JOptionPane.showMessageDialog(null, "Succesfully register");
			else
				JOptionPane.showMessageDialog(null, "Username is already used!");
			 userName_text.setText("");
			 password_text.setText("");
		}
	}
	
	class LoginHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String userName = userName_text.getText();
		    char[] pass = password_text.getPassword();
		    String passWord = String.valueOf(pass);
		    boolean success = false;
		    try {
				success = sqlClass.login(userName, passWord);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    
		    if (success) {
		    	JOptionPane.showMessageDialog(null, "Login Success!");
		    	onAnotherFrame();
		    }	
		    else
		    	JOptionPane.showMessageDialog(null, "Username or password is invalid!");
		    userName_text.setText("");
		    password_text.setText("");
		}
	}
	
	void onAnotherFrame(){
		new Restoran(userName_text.getText());
		this.dispose();
	}
   
   public static void main(String[] args){
      new LoginClass();
   }
   
}

