package Models;

import Controllers.ServerManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

	private ServerManager manager;
	static Map<String, Speler> Spelers = new HashMap<String, Speler>();

	public FileManager(ServerManager manager){
		this.manager = manager;
	}

	/**
	 * Maak JSON object aan van save game
	 * @param String fileName
	 * Geef het path van de safe file mee in de vorm van een String
	 */
	public File saveGame(String naam) {
		//JSON OB - save all data inside
		JSONObject object = new JSONObject();
		// Save all players information in the json object
		object.put("Spelers", saveAlleSpelersJSON());
		// Get alle BordGegevens (kaarten en horigen)
		object.put("Kaarten", saveAlleKaartenJSON());
		// Current stapel
		object.put("CurrentStapel", saveStapelKaartenJSON());
		return createFile(naam, object);
	}

	/**
	 *
	 * @return
	 */
	private JSONArray saveAlleSpelersJSON(){
		JSONArray spelerInJSON = new JSONArray();
		// Get alle Spelers
		ArrayList<Speler> spelerLijst = manager.bordController.bord.getAlleSpelers();
		for (int i = 0; i < spelerLijst.size() ; i++) {
			JSONObject speler = new JSONObject();
			speler.put("spelerNaam", spelerLijst.get(i).getNaam());
			speler.put("beurt", spelerLijst.get(i).getBeurt());
			speler.put("punten", spelerLijst.get(i).getPunten());
			speler.put("beschikbareHorige", spelerLijst.get(i).getBeschikbareHorigeInt());
			speler.put("gebruikteHorige", spelerLijst.get(i).getGebruikteHorigeInt());
			speler.put("horigeKleur", spelerLijst.get(i).getHorigeKleur());
			spelerInJSON.add(speler);
		}
		return spelerInJSON;
	}

	/**
	 * alle kaarten op het bord convert naar JSON array
	 * @return Json array met alle kaarten op het bord
	 */
	private JSONArray saveAlleKaartenJSON() {
		Tile[][] alleTiles = manager.bordController.bord.getAlleTiles();
		JSONArray alleKaartenBijElkaar = new JSONArray();

		for(int Y = 0; Y < 100; Y++){
			//y
			for(int X = 0; X < 100; X++){
				if( alleTiles[X][Y] != null) {
					JSONObject kaarten = new JSONObject();
					kaarten.put("isLeeg", false);

					System.out.println("IMage id: " + alleTiles[X][Y].getImageID());

					kaarten.put("imageId", alleTiles[X][Y].getImageID());
					// Noord Zijde
					kaarten.put("noordZijde", alleTiles[X][Y].getNoordZijde().getZijde().toString());
					kaarten.put("noordEinde", alleTiles[X][Y].getNoordZijde().isEinde());
					if(alleTiles[X][Y].getNoordZijde().getHorigeSpeler() != null) {
						kaarten.put("noordZijdeHorige", alleTiles[X][Y].getNoordZijde().getHorigeSpeler().getSpelerString());
						kaarten.put("noordZijdeHorigePos", alleTiles[X][Y].getNoordZijde().getHorigeSpeler().getPositieString());
					}
					// Oost Zijde
					kaarten.put("oostZijde", alleTiles[X][Y].getOostZijde().getZijde().toString());
					kaarten.put("oostEinde", alleTiles[X][Y].getOostZijde().isEinde());
					if(alleTiles[X][Y].getOostZijde().getHorigeSpeler() != null) {
						kaarten.put("oostZijdeHorige", alleTiles[X][Y].getOostZijde().getHorigeSpeler().getSpelerString());
						kaarten.put("oostZijdeHorigePos", alleTiles[X][Y].getOostZijde().getHorigeSpeler().getPositieString());
					}
					// Zuid Zijde
					kaarten.put("zuidZijde", alleTiles[X][Y].getZuidZijde().getZijde().toString());
					kaarten.put("zuidEinde", alleTiles[X][Y].getZuidZijde().isEinde());
					if(alleTiles[X][Y].getZuidZijde().getHorigeSpeler() != null) {
						kaarten.put("zuidZijdeHorige", alleTiles[X][Y].getZuidZijde().getHorigeSpeler().getSpelerString());
						kaarten.put("zuidZijdeHorigePos", alleTiles[X][Y].getZuidZijde().getHorigeSpeler().getPositieString());

					}
					// West zijde
					kaarten.put("westZijde", alleTiles[X][Y].getWestZijde().getZijde().toString());
					kaarten.put("westEinde", alleTiles[X][Y].getWestZijde().isEinde());
					if(alleTiles[X][Y].getWestZijde().getHorigeSpeler() != null) {
						kaarten.put("westZijdeHorige", alleTiles[X][Y].getWestZijde().getHorigeSpeler().getSpelerString());
						kaarten.put("westZijdeHorigePos", alleTiles[X][Y].getWestZijde().getHorigeSpeler().getPositieString());
					}

					// Midden zijde
					if(alleTiles[X][Y].getMiddenZijde() != null) {
						kaarten.put("middenZijde", alleTiles[X][Y].getMiddenZijde().getZijde().toString());
						kaarten.put("middenZijdeHorigePos", alleTiles[X][Y].getMiddenZijde().getHorigeSpeler().getPositieString());
					} else {
						kaarten.put("middenZijde", false);
						kaarten.put("middenZijdeHorige", false);
					}
					//x en y
					kaarten.put("x", alleTiles[X][Y].getX());
					kaarten.put("y", alleTiles[X][Y].getY());
					// Rotation
					kaarten.put("rotation", alleTiles[X][Y].getRotation());
					// Klooster
					kaarten.put("heeftKlooster", alleTiles[X][Y].getKlooster());
					// Heeft bonus
					kaarten.put("heeftBonus", alleTiles[X][Y].getHeeftBonus());

					// Horige Positie (enum to string)
					Horige.Posities[] horigePosities = alleTiles[X][Y].getHorigenZijdes();
					JSONArray JSONHorigeArr = new JSONArray();

					for(int i = 0; i < horigePosities.length; i++){
						JSONHorigeArr.add(horigePosities[i].toString());
					}

					kaarten.put("horigePosities", JSONHorigeArr);
					alleKaartenBijElkaar.add(kaarten);
				}
			}
		}
		return alleKaartenBijElkaar;
	}

	/**
	 * stapel kaarten naar json converten
	 * @return Return stapel in json vorm (JSONArray)
	 */
	private JSONArray saveStapelKaartenJSON(){
		JSONArray stapelKaartenJSON = new JSONArray();
		ArrayList<Tile> currentStapel = manager.bordController.kaartenStapel.kaartenOver;

		for(int i = 0; i < currentStapel.size(); i++){
			JSONObject kaart = new JSONObject();

			// Zijdes
			// Noord
			kaart.put("noordZijde", currentStapel.get(i).getNoordZijde().getZijde().toString());
			kaart.put("noordEinde", currentStapel.get(i).getNoordZijde().isEinde());
			// Oost
			kaart.put("oostZijde", currentStapel.get(i).getOostZijde().getZijde().toString());
			kaart.put("oostEinde", currentStapel.get(i).getOostZijde().isEinde());
			// Zuid
			kaart.put("zuidZuid", currentStapel.get(i).getZuidZijde().getZijde().toString());
			kaart.put("zuidZuid", currentStapel.get(i).getZuidZijde().isEinde());
			// West
			kaart.put("westZuid", currentStapel.get(i).getWestZijde().getZijde().toString());
			kaart.put("westZuid", currentStapel.get(i).getWestZijde().isEinde());
			// Heeft klooster
			kaart.put("heeftKlooster", currentStapel.get(i).getHeeftKlooster());
			// Heeft bonus
			kaart.put("heeftBonus", currentStapel.get(i).getHeeftBonus());
			// Image id
			kaart.put("image_id", currentStapel.get(i).getImageID());

			// Horige Positie (enum to string)
			Horige.Posities[] horigePosities = currentStapel.get(i).getHorigenZijdes();
			JSONArray JSONHorigeArr = new JSONArray();

			for(int h = 0; h < horigePosities.length; h++){
				JSONHorigeArr.add(horigePosities[h].toString());
			}

			kaart.put("horigePosities", JSONHorigeArr);
			stapelKaartenJSON.add(kaart);

		}
		return stapelKaartenJSON;
	}

	/**
	 * Maak file aan van met een JSONobject erin
	 * @param String file name, JSONObject object
	 * @return Return file met JSON erin
	 */
	public File createFile(String naam, JSONObject object){

		try {
			//new file
			File newTextFile = new File(naam);

			//create writer
			FileWriter fw = new FileWriter(newTextFile);

			//Write to json file
			fw.write(object.toJSONString());

			//close writer
			fw.close();
			return newTextFile;
		} catch (IOException iox) {
			//do stuff with exception
			iox.printStackTrace();
		}
		return null;
	}

	/**
	 * Laden file en return JSONObject
	 * @param File load json file
	 * @return Return JSONObject
	 */
	private static JSONObject loadGame(File load) {
		System.out.println("Load Game");


		Tile [][] tiles = new Tile[100][100];

		JSONParser parser = new JSONParser();
		Object obj = null;

		try{
			File reader = load;
			System.out.println(reader.getAbsolutePath());
			obj = parser.parse(new FileReader(reader));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("FileNotFound");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IO");
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			System.out.println("I have no clue what this is");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (JSONObject) obj;
	}

	/**
	 * lezen file en maak tile kaarten
	 * @param File json file
	 * @return return Tile[][]
	 */
	public static Tile[][] loadBordKaartenJSON(File load){
		JSONObject jsonObject = loadGame(load);

		JSONArray bordKaarten = (JSONArray) jsonObject.get("Kaarten");
		JSONArray JSONSpelers = (JSONArray) jsonObject.get("Spelers");

		ArrayList<Speler> alleSpelers = getAlleSpelers(JSONSpelers);



		Tile[][] allLoadedTiles = new Tile[100][100];

		for(Object number : bordKaarten){
			JSONObject jsonNumber = (JSONObject) number;
			// Zijde x en y
			int x = ((Number)jsonNumber.get("x")).intValue();
			System.out.println("x num: " + x);
			int y = ((Number)jsonNumber.get("y")).intValue();
			//Zijde einde
			boolean noordEinde = (boolean) jsonNumber.get("noordEinde");
			boolean oostEinde  = (boolean) jsonNumber.get("oostEinde");
			boolean zuidEinde  = (boolean) jsonNumber.get("zuidEinde");
			boolean westEinde  = (boolean) jsonNumber.get("westEinde");
			// Rotation
			int rotation = ((Number)jsonNumber.get("rotation")).intValue();
			// Imageid
			String imageId = (String) jsonNumber.get("imageId");
			// Heeft klooster
			boolean heeftKlooster = (boolean) jsonNumber.get("heeftKlooster");
			// Get JsonArray horigePosities
			JSONArray horigeJSON = (JSONArray) jsonNumber.get("horigePosities");

			//Zijde type - NOORD
			Zijde.ZijdeType noordZijdeType = Zijde.ZijdeType.valueOf((String) jsonNumber.get("noordZijde"));
			Zijde noordZijde = new Zijde(noordZijdeType, noordEinde);

			// OOST
			Zijde.ZijdeType oostZijdeType = Zijde.ZijdeType.valueOf((String) jsonNumber.get("oostZijde"));
			Zijde oostZijde  = new Zijde(oostZijdeType, oostEinde);

			//ZUID
			Zijde.ZijdeType zuidZijdeType = Zijde.ZijdeType.valueOf((String) jsonNumber.get("zuidZijde"));
			Zijde zuidZijde  = new Zijde(zuidZijdeType, zuidEinde );

			//WEST
			Zijde.ZijdeType westZijdeType = Zijde.ZijdeType.valueOf((String) jsonNumber.get("westZijde"));
			Zijde westZijde  = new Zijde(westZijdeType, westEinde);

			// Midden
//			Zijde.ZijdeType middenZijde = Zijde.ZijdeType.valueOf((String) jsonNumber.get("middenZijde"));


			String spelerNaam = null;

			for(Speler d : alleSpelers){
//				System.out.println("NOORD: " + (String) jsonNumber.get("noordZijdeHorige"));
//				System.out.println("OOST: " + (String) jsonNumber.get("oostZijdeHorige"));
//				System.out.println("ZUID: " + (String) jsonNumber.get("zuidZijdeHorige"));
//				System.out.println("WEST: " + (String) jsonNumber.get("westZijdeHorige"));

				if(jsonNumber.get("noordZijdeHorige") != null && d.getNaam().contains((String) jsonNumber.get("noordZijdeHorige"))) {
					spelerNaam = d.getNaam();
//					System.out.println("Speler Hor:" + speler.getNaam());
				}
				if(jsonNumber.get("oostZijdeHorige") != null && d.getNaam().contains((String) jsonNumber.get("oostZijdeHorige"))) {
					spelerNaam = d.getNaam();
//					System.out.println("Speler Hor:" + speler.getNaam());
				}
				if(jsonNumber.get("zuidZijdeHorige") != null && d.getNaam().contains((String) jsonNumber.get("zuidZijdeHorige"))) {
					spelerNaam = d.getNaam();
//					System.out.println("Speler Hor:" + speler.getNaam());
				}
				if(jsonNumber.get("westZijdeHorige") != null && d.getNaam().contains((String) jsonNumber.get("westZijdeHorige"))) {
					spelerNaam = d.getNaam();
					//					System.out.println("Speler Hor:" + speler.getNaam());
				}
			}

			// Alle horigen
			Horige horigen = new Horige();

			//get JsonArray horigePosities
			JSONArray horigenArray = (JSONArray) jsonNumber.get("horigePosities");

			//new array
			String[] positie = new String[horigenArray.size()];
			Horige.Posities[] horigenPos = new Horige.Posities[horigenArray.size()];

			//all data to array
			for (int i = 0; i < horigenArray.size() ; i++) {
				positie[i] = (String)horigenArray.get(i);

				horigenPos[i] = Horige.Posities.valueOf((String)horigenArray.get(i));
				System.out.println("Kaart ENUM: " + horigenPos[i]);
			}

			//horige positie
			if(jsonNumber.get("noordZijdeHorige") != null){
				horigen.setPositie(horigen.positie.valueOf((String)jsonNumber.get("noordZijdeHorigePos")));
				horigen.setSpeler(Spelers.get(spelerNaam));
				noordZijde = new Zijde(noordZijdeType, noordEinde, horigen);
				System.out.println("noordZijdeHorige: " + jsonNumber.get("noordZijdeHorige"));
			}
			if(jsonNumber.get("oostZijdeHorige") != null){
				horigen.setPositie(horigen.positie.valueOf((String)jsonNumber.get("oostZijdeHorigePos")));
				horigen.setSpeler(Spelers.get(spelerNaam));
				oostZijde = new Zijde(oostZijdeType, oostEinde, horigen);
				System.out.println("Oostzijde Horige: " + jsonNumber.get("oostZijdeHorige"));
			}
			if(jsonNumber.get("zuidZijdeHorige") != null){
				horigen.setPositie(horigen.positie.valueOf((String)jsonNumber.get("zuidZijdeHorigePos")));
				horigen.setSpeler(Spelers.get(spelerNaam));
				zuidZijde = new Zijde(zuidZijdeType, zuidEinde, horigen);
				System.out.println("zuidZijdeHorige: " + jsonNumber.get("zuidZijdeHorige"));
			}
			if(jsonNumber.get("westZijdeHorige") != null){
				horigen.setPositie(horigen.positie.valueOf((String)jsonNumber.get("westZijdeHorigePos")));
				horigen.setSpeler(Spelers.get(spelerNaam));
				westZijde = new Zijde(westZijdeType, westEinde, horigen);
				System.out.println("westZijdeHorige: " + jsonNumber.get("westZijdeHorige"));
			}

			boolean heeftBonus = (boolean)jsonNumber.get("heeftBonus");

			System.out.println("allLoadedTiles.length = " + allLoadedTiles.length);

			allLoadedTiles[x][y] = new Tile();

			//add to bord kaarten
			allLoadedTiles[x][y].setImageID(imageId);
			allLoadedTiles[x][y].setX(x);
			allLoadedTiles[x][y].setY(y);
			allLoadedTiles[x][y].setNoordZijde(noordZijde);
			allLoadedTiles[x][y].setOostZijde(oostZijde);
			allLoadedTiles[x][y].setZuidZijde(zuidZijde);
			allLoadedTiles[x][y].setWestZijde(westZijde);
			allLoadedTiles[x][y].setHeeftKlooster(heeftKlooster);
			allLoadedTiles[x][y].setHeeftBonus(heeftBonus);
			allLoadedTiles[x][y].setHorigenZijdes(horigenPos);
			allLoadedTiles[x][y].setRotation(rotation);
			if(heeftKlooster){
				allLoadedTiles[x][y].setMiddenZijde(new Zijde(Zijde.ZijdeType.KLOOSTER, true));
			}

		}
		System.out.println("Klaar met loaden kaarten");

		return allLoadedTiles;
	}

	/**
	 * JSONArray
	 * @param JSONArray JSONSpelers (spelers in JSON format)
	 * @return return ArrayList Speler
	 */
	public static ArrayList<Speler> getAlleSpelers(JSONArray JSONSpelers){
		ArrayList<Speler> alleSpelers = new ArrayList<>();
		Spelers = new HashMap<>();

		for(Object number : JSONSpelers) {
			JSONObject jsonNumber = (JSONObject) number;
			// Speler gegevens
			String naam = (String) jsonNumber.get("spelerNaam");
			int punten = ((Number)jsonNumber.get("punten")).intValue();
			boolean beurt = (boolean) jsonNumber.get("beurt");
			int beschHorige = ((Number)jsonNumber.get("beschikbareHorige")).intValue();
			int gebrHorige = ((Number)jsonNumber.get("gebruikteHorige")).intValue();
			String horigeKleur = (String) jsonNumber.get("horigeKleur");
			//add speler
			Speler speler = new Speler(naam, punten, beurt, beschHorige, gebrHorige, horigeKleur);
			alleSpelers.add(speler);
			Spelers.put(speler.getNaam(),speler);
			System.out.println("================================SPELER " + Spelers.get(speler.getNaam()).getNaam());
		}
		return alleSpelers;
	}

	/**
	 * JSONArray
	 * @param File JSONSpelers in een file (spelers in JSON format)
	 * @return return ArrayList Speler
	 */
	public static ArrayList<Speler> loadAlleSpelersJSON(File load){
		JSONObject jsonObject = loadGame(load);

		JSONArray JSONSpelers = (JSONArray) jsonObject.get("Spelers");

		ArrayList<Speler> alleSpelers = new ArrayList<>();
		for(Object number : JSONSpelers) {
			JSONObject jsonNumber = (JSONObject) number;
			// Speler gegevens
			String naam = (String) jsonNumber.get("spelerNaam");
			int punten = ((Number)jsonNumber.get("punten")).intValue();
			boolean beurt = (boolean) jsonNumber.get("beurt");
			int beschHorige = ((Number)jsonNumber.get("beschikbareHorige")).intValue();
			int gebrHorige = ((Number)jsonNumber.get("gebruikteHorige")).intValue();
			String horigeKleur = (String) jsonNumber.get("horigeKleur");

			//add speler
			alleSpelers.add(new Speler(naam,punten,beurt,beschHorige,gebrHorige,horigeKleur));
		}
		return alleSpelers;
	}



}
