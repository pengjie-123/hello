package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatServer {
   ArrayList<Object> clientOutputStreams;
	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket socket;
		public ClientHandler(Socket socket) {
			this.socket=socket;
			try {
				InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());//可加gbk
				reader=new BufferedReader(streamReader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// TODO Auto-generated constructor stub
		}

		@Override
		public void run() {
			String msg;
			try {
				while ((msg=reader.readLine())!=null) {
					msg=socket.getInetAddress().getHostName()+":"+"\n"+msg;
					System.out.println("服务器收到了read "+msg);
					tellEveryone(msg);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		
		
	}//关闭内部类
	public static void main(String[] args) {
		new ChatServer().go();
	}
	private void go() {
		clientOutputStreams=new ArrayList<>();
		try {
			ServerSocket serverSocket=new ServerSocket(5000);
			while(true) {
				System.out.println("服务器准备监听");
				Socket clientSocket=serverSocket.accept();
				System.out.println("客户端的ip： "+clientSocket.getInetAddress().getHostAddress());
				PrintWriter writer=new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				
				Thread t=new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("get a connection");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	private void tellEveryone(String msg) {
		Iterator<Object> iterator=clientOutputStreams.iterator();
		while (iterator.hasNext()) {
			PrintWriter writer = (PrintWriter) iterator.next();
			writer.println(msg);
			writer.flush();
			
		}
		
	}
}
