package Controllers;

import Models.Stapel;
import Models.Bord;
/**
 * Created by Marti on 8-6-2017.
 */
public class BordController {

	Stapel kaartenStapel;
	Bord bord;

	public BordController() {
		System.out.println("HALLLLLLLLLLLLLO");
		kaartenStapel = new Stapel();
		bord = new Bord();
		bord.plaatsKaartCheat(10,10,kaartenStapel.getBeginTile());
	}

	public String pakKaartvanStapel(String spelerNaam) {
		if (!bord.isSpelerBeurt(spelerNaam)) {
			kaartenStapel.pakKaart();

			return kaartenStapel.getTurnTile().getImageID();
		}
		return null;
	}

	public boolean checkKaartFit(int x, int y) {
		return bord.checkKaartFit(x, y, kaartenStapel.getTurnTile());
	}

	public boolean plaatsKaart(int x, int y) {
	if(bord.checkKaartFit(x,y,kaartenStapel.getTurnTile())){
		kaartenStapel.getTurnTile().plaats(x,y);
		bord.plaatsKaart(x,y,kaartenStapel.getTurnTile());
		return true;
	} else {
		System.out.println("Kaart niet succesvol geplaatst (BORDCONTROLLER)");
		return false;
	}
	}

	public void draaiKaart(){
		kaartenStapel.getTurnTile().draaiKaart();
	}
}
