import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.*;

public class Prodavaonica extends Process implements Election {

    String p="";
    Dictionary<String, Integer> proizvodi;
    int next;
    String name;
    Scanner reader = new Scanner(System.in);
    boolean awake = false;

    public Prodavaonica(Linker initComm,Dictionary<String, Integer> proi) {
        super(initComm);
        next = (myId + 1) % N;
	       this.name="Prodavaonica"+String.valueOf(myId);
	       this.proizvodi=proi;
    }
    public synchronized String nadiProizvod(){
	   while (p.equals("")) myWait();
	   return p;
    }

    /*
    public synchronized void handleMsg(Msg m, int src, String tag) {
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

	public String parsiraj(String str){
		String pomocni=str.split("\\?")[0];
		//pomocni.substring(0, pomocni.indexOf(' '));
		System.out.println("Parsirano: "+pomocni);
		return pomocni;
	}

	public synchronized void handleMsg(Msg m, int src, String tag) {
    String poruka = m.getMessage().substring(1); // problem parisranja...doraditi
		String poruka_proizvod=parsiraj(poruka);
		if (tag.equals(String.valueOf(myId))) {
			System.out.println("Poruka se vratila: "+poruka);
		     	p=poruka;
			notify();
		}
		else {
			System.out.println("Traži proizvod: "+poruka_proizvod);
			if(proizvodi.get(poruka_proizvod)!=null && proizvodi.get(poruka_proizvod)>0){
				System.out.println("Nađen: "+poruka_proizvod);
				poruka=poruka+name+" "+proizvodi.get(poruka_proizvod)+"?";
			}
			notify();
		   	sendMsg(next, tag, poruka);
		}
	}

  public synchronized void odradiKupnju(){  
    String proizvod="";
    System.out.print("Odaberi proizvod: ");
    while(proizvod.equals(""))
      proizvod = reader.nextLine();
    if(proizvodi.get(proizvod) != null){
      int brojPreostalih=proizvodi.get(proizvod);
      if(brojPreostalih == 0){
        System.out.print("Nema dovoljno artikala.");
        return;
      }
      System.out.print("Količina: ");
      int kolicina = reader.nextInt();
      if(kolicina<=brojPreostalih && kolicina>0){
        System.out.println("Prodano: "+proizvod+" "+kolicina);
        proizvodi.put(proizvod, proizvodi.get(proizvod) - kolicina);
        return;
      }
      else{
        System.out.println("Nema dovoljno artikala.");
        return;
      }
    }
  }

  public synchronized void dodajArtikl(){
    String proizvod="";
    System.out.print("Naziv artikla: ");
    while(proizvod.equals(""))
      proizvod = reader.nextLine();
    System.out.print("Količina: ");
    int kolicina = reader.nextInt();
    proizvodi.put(proizvod, kolicina);
  }

	public synchronized void ostaleProdavaonice(String upit){ //trazenje prozvoda
		String rezultat="";
		startElection(upit+"?");
		rezultat = nadiProizvod();
		String[] popis=rezultat.split("\\?");
		for(String s : popis)  //nacin ispisa proizvoda iz ostalih prodavaonica
			System.out.println(s);

	}

  public int kolicinaProizvoda(String proizvod){ //vraca kolicinu prozivoda , 0 ako nema proizvoda
    if(proizvodi.get(proizvod)==null)
      return 0;
    return proizvodi.get(proizvod);
  }

    public synchronized void startElection(String proizvod) {
      p="";
      sendMsg(next, String.valueOf(myId), proizvod);
    }
}
