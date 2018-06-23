import java.util.concurrent.TimeUnit;
public class Prodavaonica extends Process implements Election {   
    String p="";
    int next;   
    boolean awake = false;
    public Prodavaonica(Linker initComm) {
        super(initComm);
        next = (myId + 1) % N;
    }
    public synchronized String nadiProizvod(){
	   while (p.equals("")) myWait();
	   return p;
    }
    /*public synchronized void handleMsg(Msg m, int src, String tag) {
        int j = m.getMessageInt(); // get the number
        if (tag.equals("election")) {
            if (j > number)
                sendMsg(next, "election", j); // forward the message
            else if (j == number) // I won!
                sendMsg(next, "leader", myId);
            else if ((j < number) && !awake) startElection();
        } else if (tag.equals("leader")) {
            leaderId = j;
	    notify();
            if (j != myId) sendMsg(next, "leader", j);
        }
    }*/

	public synchronized void handleMsg(Msg m, int src, String tag) {
		String str = m.getMessage(); 
		if (tag.equals(String.valueOf(myId))) {
			System.out.println("Poruka se vratila: "+ str);		
		     	p=str;
			notify();
		} 
		else {	
			notify();		
		   	sendMsg(next, tag, str);				
		}
	}

    public synchronized void startElection(String proizvod) {   
        sendMsg(next, String.valueOf(myId), proizvod);
    }
}
