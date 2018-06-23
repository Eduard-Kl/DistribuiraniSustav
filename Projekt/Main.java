import java.util.*;
public class Main{
	public static void main(String[] args){

		Dictionary<String, Integer> proizvodi = new Hashtable<String, Integer>();
		proizvodi.put("cokolada", 5);
		proizvodi.put("pivo", 7);

		int myId = Integer.parseInt(args[0]);
		int numProc = Integer.parseInt(args[1]);
		String trazeniProizvod = args[2];

		if(proizvodi.get(trazeniProizvod) != null){
			System.out.println("Traženi proizvod dostupan je na skladištu, broj artikala: " + proizvodi.get(trazeniProizvod) + ".");
			System.out.println("Prodaja izvršena.");
			proizvodi.put(trazeniProizvod, proizvodi.get(trazeniProizvod) - 1);
			System.exit(0);
		}
		
    }
}