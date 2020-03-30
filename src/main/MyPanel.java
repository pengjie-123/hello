package main;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MyPanel {
	void setPanel() {
		JFrame frame=new JFrame("First Ui");
		JButton button=new JButton("click me");
		frame.getContentPane().add(button);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}
public static void main(String[] args) {
	new MyPanel().setPanel();//yyyyy
}
}
