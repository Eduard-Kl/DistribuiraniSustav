import java.util.Scanner;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ProdavaonicaTester {

	public static void main(String[] args) throws Exception{

		int myId = Integer.parseInt(args[1]);
		int numProc = Integer.parseInt(args[2]);
		System.out.println("Prvi: "+args[0]+", myId: " + args[1]+ ", numProc: "+args[2]+".");

		Dictionary<String, Integer> proizvodi = new Hashtable<String, Integer>();
		//proizvodi.put("cokolada", 5);
		//proizvodi.put("pivo", 2);

		BufferedReader citac = null;
		String linija = "";

		// Proces čita samo svoju datoteku
		String datoteka = "./Dostupni proizvodi/ListaDostupnihProizvoda" + myId + ".csv";


		// Učitavanje dostupnih proizvoda
		try{
            citac = new BufferedReader(new FileReader(datoteka));
            while ((linija = citac.readLine()) != null){
                String[] redak = linija.split(",");
                //System.out.println(redak[0] + redak[1]);
                proizvodi.put(redak[0], Integer.parseInt(redak[1]));
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
		
		
		Linker comm = new Linker(args[0], myId, numProc);
		Prodavaonica p = new Prodavaonica(comm,proizvodi);
		for (int i = 0; i < numProc; i++){
			if (i != myId);
				(new ListenerThread(i, p)).start();
		}


		while(true){ //beskonacna petlja za upite

			System.out.println("Izbornik (Kupi/Ubaci)");
			Scanner reader = new Scanner(System.in);
			System.out.print("");
			String trazeniProizvod = reader.nextLine();
			//System.out.println("Proizvod: " + trazeniProizvod);

			if(trazeniProizvod.equals("Kupi"))	//kupovina proizvoda
				p.odradiKupnju();

			else if(trazeniProizvod.equals("Ubaci"))	//dodavanje proizvoda
				p.dodajArtikl();

			// Sadrži li dućan proizvod
			else if(p.kolicinaProizvoda(trazeniProizvod)>0){
				System.out.println("Traženi proizvod dostupan je na skladištu, broj artikala: " + p.kolicinaProizvoda(trazeniProizvod) + ".");
			}

			else{ //ako nema trazi dalje
				p.ostaleProdavaonice(trazeniProizvod);
			}
		}

		// System.out.println("The leader is: " + leader);
	}
}
