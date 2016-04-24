package main;

import java.awt.Container;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Window extends JFrame{
	
	public static final Font buttonFont = new Font("Consolas", Font.BOLD, 36);
	
	public Window(){
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Main.WIDTH, Main.HEIGHT);
		this.setTitle("Agario");
		this.setResizable(false);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		Main main = new Main();
		
		Container contentPane = this.getContentPane();
		
		contentPane.add(main);
		
	}
	
	public static void main(String[] args){
		new Window().setVisible(true);
	}
	
}
