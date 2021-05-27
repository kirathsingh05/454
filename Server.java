import java.util.HashMap;
import java.util.Arrays;
// import java.util.Random;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//ttmn
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


	// MAIN
	public static void main(String args[]) throws Exception {
		if (args.length != 1) {
			System.out.println("usage: java Server port");
			// open
			// --
			int port = Integer.parseInt(args[0]);
			ServerSocket ssock = new ServerSocket(port);
			System.out.println("listening on port " + port);
			// --
			Socket csock = ssock.accept();
			System.out.println("accepted connection " + csock);

			DataInputStream din = new DataInputStream(csock.getInputStream());
			int dataLength = din.readInt();
			System.out.println("received request header, data payload has length " + dataLength);
			byte[] bytes = new byte[dataLength];
			din.readFully(bytes);

			int i = 0;
			while (i < bytes.length) { 
				try {
					int node1;
					int node2;
					// #1
					node1 = 0;
					node2 = 0;
					while(bytes[i] != 32){
						node1 = 10*node1 + (bytes[i] - 48) ; 	//48 offs.
						i++;
					}
					i++;
					while(bytes[i] !=10 ){ 						//#2
						node2 = (bytes[i] - 48) + 10*node2 ;
						i++;
					}
					i++;
					updateValues(node1,node2);
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
			String result = new String();
			HashMap<Integer, Integer> temp = edges;
			StringBuilder sb = new StringBuilder();
			for (int key : temp.keySet()) {
	        	sb.append(key);
	        	sb.append(" ");
	        	sb.append(temp.get(key));   
	    	}
			result = sb.toString();

			// Write result to the client
			DataOutputStream dout = new DataOutputStream(csock.getOutputStream());
			bytes = result.getBytes("UTF-8");
			dout.writeInt(bytes.length);
			dout.write(bytes);
			dout.flush();
			System.out.println("#result data (in bytes): " + bytes.length);
		} 
	}
}
