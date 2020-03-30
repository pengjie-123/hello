package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ChatClient {
   JTextField outgoing;
   JTextArea incoming;
   BufferedReader reader;
   PrintWriter writer;
   Socket socket;
   
   public void go() {
	JFrame frame=new JFrame("Chat Client");
	JPanel mainPanel=new JPanel();
	incoming=new JTextArea(25,30);
	incoming.setLineWrap(true);
	incoming.setWrapStyleWord(true);
	incoming.setEditable(false);
	JScrollPane scrollPane=new JScrollPane(incoming);
	scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	outgoing=new JTextField(20);
	JButton button=new JButton("send");
	button.addActionListener(new SendButtonListener());
	mainPanel.add(scrollPane);
	mainPanel.add(outgoing);
	mainPanel.add(button);
	setUpNetwork();
	
	//开启多线程
	Thread thread=new Thread(new IncomeMessage());
	thread.start();
	
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	System.out.println("多线程之后的代码");
	frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
	frame.setSize(400, 500);
	frame.setVisible(true);
	
	
	
}

private void setUpNetwork() {
	try {
		socket=new Socket("192.168.0.107", 5000);
		InputStreamReader streamReader=new InputStreamReader(socket.getInputStream());
		reader=new BufferedReader(streamReader);
		
		writer=new PrintWriter(socket.getOutputStream());
		System.out.println("networking established!");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
public class SendButtonListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		writer.println(outgoing.getText());
		writer.flush();
		outgoing.setText("");
		outgoing.requestFocus();
	}
	
}
public class IncomeMessage implements Runnable {

	@Override
	public void run() {
		
		String msg;
		try {
			
			while((msg=reader.readLine())!=null) {
				System.out.println("客户端收到message: "+msg);
				incoming.append(msg+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
public static void main(String[] args) {
	new ChatClient().go();
}

}
