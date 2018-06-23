import java.util.Scanner;
import java.util.*;

public class ProdavaonicaTester {

	public static void main(String[] args) throws Exception{
		
		// Lista proizvoda
		Dictionary<String, Integer> proizvodi = new Hashtable<String, Integer>();
		proizvodi.put("cokolada", 5);
		proizvodi.put("pivo", 2);
		
		int myId = Integer.parseInt(args[1]);
		int numProc = Integer.parseInt(args[2]);
		System.out.println("prvi "+args[0]+" myId: " + args[1]+ ", numProc: "+args[2]);
		Linker comm = new Linker(args[0], myId, numProc);
		Prodavaonica p = new Prodavaonica(comm,proizvodi);
		for (int i = 0; i < numProc; i++){
			if (i != myId);
				(new ListenerThread(i, p)).start();
		}
		String leader="";
		while(true){ //bekonacna petlja za upite
			Scanner reader = new Scanner(System.in);
			String trazeniProizvod = reader.nextLine();
			System.out.println("Proizvod: " + trazeniProizvod);

			// Sadrži li dućan proizvod
			if(proizvodi.get(trazeniProizvod) != null && proizvodi.get(trazeniProizvod)>0){
				System.out.println("Traženi proizvod dostupan je na skladištu, broj artikala: " + proizvodi.get(trazeniProizvod) + ".");
				System.out.println("Prodaja izvršena.");
				proizvodi.put(trazeniProizvod, proizvodi.get(trazeniProizvod) - 1);
			}
		 		
			else{ //ako nema trazi dalje
				p.startElection(trazeniProizvod);
				leader = p.nadiProizvod();
				System.out.println("Proizvodi: " + leader);
			}
		}

		// System.out.println("The leader is: " + leader);
	}
}
