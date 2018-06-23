import java.util.Scanner;


public class ProdavaonicaTester {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		int myId = Integer.parseInt(args[1]);
		 int numProc = Integer.parseInt(args[2]);
		 System.out.println("prvi "+args[0]+" myId: " + args[1]+ ", numProc: "+args[2]);
		 Linker comm = new Linker(args[0], myId, numProc);
		 Prodavaonica p = new Prodavaonica(comm);
		 for (int i = 0; i < numProc; i++){
			 if (i != myId);
			 	(new ListenerThread(i, p)).start();
		 }
		String leader=""; 
		while(true){ //bekonacna petlja za upite
			Scanner reader = new Scanner(System.in);   
			String proizvod=reader.nextLine();
			System.out.println("Proizvod: " + proizvod);
			if(proizvod.equals("break")){ // ovje ubacit provjeru proizvoda u ducanu 				break;
			}
		 		
			else{ //ako nema trazi dalje
				p.startElection(proizvod);
				leader = p.nadiProizvod();
				System.out.println("Proizvodi: " + leader);
			} 
						
		}
		 
		// System.out.println("The leader is: " + leader);

	}

}
