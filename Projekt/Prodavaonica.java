import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Prodavaonica extends Process implements Election {

    String p="";
    Dictionary<String, Integer> proizvodi;
    int next;
    String name;
    Scanner reader = new Scanner(System.in);
    boolean awake = false;
    String datoteka = "";

    public Prodavaonica(Linker initComm, Dictionary<String, Integer> proi) {
      super(initComm);
      next = (myId + 1) % N;
      this.name = "Prodavaonica" + String.valueOf(myId);
      this.proizvodi = proi;
      this.datoteka = "./Dostupni proizvodi/ListaDostupnihProizvoda" + String.valueOf(myId) + ".csv";
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

  public void odradiKupnju(){
    String proizvod="";
    System.out.print("Odaberi proizvod: ");
    while(proizvod.equals(""))
      proizvod = reader.nextLine();
    if(proizvodi.get(proizvod) != null){
      int brojPreostalih=proizvodi.get(proizvod);
      if(brojPreostalih == 0){
        System.out.println("Nema dovoljno artikala.");
        return;
      }
      System.out.print("Količina: ");
      int kolicina = reader.nextInt();
      if(kolicina<=brojPreostalih && kolicina>0){
        smanjiKolicinu(proizvod,kolicina);
        return;
      }
      else{
        System.out.println("Nema dovoljno artikala.");
        return;
      }
    }
  }

  public void dodajArtikl(){
    String proizvod="";
    System.out.print("Naziv artikla: ");
    while(proizvod.equals(""))
      proizvod = reader.nextLine();
    System.out.print("Količina: ");
    int kolicina = reader.nextInt();
    povecajKolicinu(proizvod,kolicina);
  }

  public synchronized  void smanjiKolicinu(String proizvod,int kolicina){
    update("ukloni", proizvod, kolicina);
    proizvodi.put(proizvod, kolicinaProizvoda(proizvod) - kolicina);
  }

  public synchronized  void povecajKolicinu(String proizvod,int kolicina){
    proizvodi.put(proizvod, kolicinaProizvoda(proizvod)+kolicina);
    update("dodaj", proizvod, kolicina);
    System.out.println("Prodano: "+proizvod+" "+kolicina);
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

  public synchronized void update(String izbor, String proizvod, int promjena){

    // Dodaj novi proizvod u listu dostupnih
    if(izbor == "dodaj"){
      try{
        // Otvori datoteku u append modu
        FileWriter pisac = new FileWriter(datoteka, true);
        pisac.write(proizvod + "," + promjena + "\n");
        pisac.close();
      }
      catch(FileNotFoundException e){
        e.printStackTrace();
      }
      catch(IOException e){
        e.printStackTrace();
      }
    }
    // Ažuriraj postojeći proizvod
    else{ // izbor == "ukloni"
      try{
        File ulaz = new File(datoteka);
        File tempDatoteka = new File("./Dostupni proizvodi/ListaDostupnihProizvoda" + String.valueOf(myId) + "TEMP.csv");

        BufferedReader citac = new BufferedReader(new FileReader(ulaz));
        BufferedWriter pisac = new BufferedWriter(new FileWriter(tempDatoteka));

        String linija = "", linijaIzbaci = proizvod + "," + String.valueOf(kolicinaProizvoda(proizvod)) + "\n";

        // U TEMP datoteku ubaci sve linije osim one koja sadrži 'proizvod'
        while((linija = citac.readLine()) != null){
          String[] redak = linija.split(",");
          if(!redak[0].equals(proizvod)){
            pisac.write(linija + "\n");
          }
        }
        pisac.close();

        FileWriter pisac2 = new FileWriter(tempDatoteka, true);

        // 'promjena' je broj kupljenih artikala
        int novaKolicina = kolicinaProizvoda(proizvod) - promjena;

        pisac2.write(proizvod + "," + novaKolicina + "\n");
        pisac2.close();

        citac.close();

        // Izbriši originalnu datoteku, i preimenuj TEMP
        tempDatoteka.renameTo(ulaz);
      }
      catch(FileNotFoundException e){
        e.printStackTrace();
      }
      catch(IOException e){
        e.printStackTrace();
      }
    }
  }

    public synchronized void startElection(String proizvod) {
      p="";
      sendMsg(next, String.valueOf(myId), proizvod);
    }
}
