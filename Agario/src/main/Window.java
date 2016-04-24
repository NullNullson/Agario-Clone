package main;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import agario.GameMode;

public class Window extends JFrame implements ActionListener{
	
	private JPanel menuPanel;
	
	private JButton hostServer;
	
	private JButton joinServer;
	
	public static final Font buttonFont = new Font("Consolas", Font.BOLD, 36);
	
	public Window(){
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Main.WIDTH, Main.HEIGHT);
		this.setTitle("Agario");
		
		menuPanel = new JPanel();
		
		hostServer = new JButton("Host Server");
		hostServer.setFont(buttonFont);
		hostServer.addActionListener(this);
		
		joinServer = new JButton("Join Server");
		joinServer.setFont(buttonFont);
		joinServer.addActionListener(this);
		
		menuPanel.setLayout(new GridLayout(5, 0));
		
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.setLayout(new GridLayout(0, 2));
		
		buttonPanel.add(hostServer);
		
		buttonPanel.add(joinServer);
		
		menuPanel.add(buttonPanel);
		
		Container contentPane = this.getContentPane();
		
		contentPane.add(menuPanel);
		
	}
	
	public static void main(String[] args){
		new Window().setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == hostServer){
			
			Container contentPane = this.getContentPane();
			
			contentPane.removeAll();
			
			Main main = new Main(GameMode.SERVER);
			
			contentPane.add(main);
			
			contentPane.repaint();
			contentPane.revalidate();
			
		}
		else if(e.getSource() == joinServer){
			
			Container contentPane = this.getContentPane();
			
			contentPane.removeAll();
			
			Main main = new Main(GameMode.CLIENT);
			
			contentPane.add(main);
			
			contentPane.repaint();
			contentPane.revalidate();
			
		}
		
	}
	
}
