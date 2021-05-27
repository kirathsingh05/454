import java.util.HashMap;
import java.util.Arrays;
// import java.util.Random;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Server {

	static HashMap<Integer, Integer> edges = new HashMap<Integer, Integer>();

	// Helper func
	static void updateValues(int val1, int val2){
		Integer node1 = Integer.valueOf(val1);
		Integer node2 = Integer.valueOf(val2);
		// in 1|2
		if(edges.containsKey(node1)) {
			edges.put(node1, edges.get(node1) + 1);
		}
		else {
			edges.put(node1, 1);
		}
		if(edges.containsKey(node2)) {
			edges.put(node2, edges.get(node2) + 1);
		}
		else {
			edges.put(node2, 1);
		}
	}

	public static void main(String args[]) throws Exception {
		if (args.length != 1) {
			System.out.println("usage: java Server port");
			System.exit(-1);
		}
		int port = Integer.parseInt(args[0]);

		ServerSocket ssock = new ServerSocket(port);
		System.out.println("listening on port " + port);
		while(true) {
			try {
				Socket csock = ssock.accept();
				System.out.println("Accepted connection: " + csock);
				
				// read data input acc.
				DataInputStream din = new DataInputStream(csock.getInputStream());
				int reqDataLen = din.readInt();
				System.out.println("received request header, data payload has length " + reqDataLen);
				byte[] bytes = new byte[reqDataLen];
				din.readFully(bytes);
				
				int i = 0;
				while (i < bytes.length) {
					// decl.
					int node1;
					int node2;
					// assn.
					node1 = 0;
					node2 = 0;
					// #1
					while(bytes[i] != 32){
						node1 = (node1*10) + (bytes[i]-48) ; //48 offs.
						i++;
					}
					i++;
					// #2
					while(bytes[i] !=10 ){
						node2 = (node2*10) + (bytes[i]-48) ;
						i++;
					}
					i++;
					updateValues(node1, node2);
				}

				// stringify result
				String result = new String();
				HashMap<Integer, Integer> temp = edges;
				StringBuilder sb = new StringBuilder();
				for (int key : temp.keySet()) {
		        	sb.append(key);
		        	sb.append(" ");
		        	sb.append(temp.get(key));   
		        	sb.append("\n");
		    	}
				result = sb.toString();
				
				// append result
				DataOutputStream dout = new DataOutputStream(csock.getOutputStream());
				bytes = result.getBytes("UTF-8");
				dout.writeInt(bytes.length);
				dout.write(bytes);
				dout.flush();
				System.out.println("#result data (in bytes): " + bytes.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
