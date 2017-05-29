package Views;

import javafx.scene.Scene;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import Controllers.MenuController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MenuViewScene extends Scene{


	VBox mainPane;
	private MenuController controller;


	public MenuViewScene(MenuController controller){
		super(new VBox(), 400,400);
		mainPane = (VBox) this.getRoot();
		this.controller = controller;

		init();
	}

		public void init() {
			/*
			 * 0 = New game 1 = Laden game 2 = Gebruiksaanwijzing 3 = About 4 =
			 * Instellingen 5 = Spel verlaten
			 */
			Button[] knoppen = new Button[6];

			for (int i = 0; i < knoppen.length; i++) {
				knoppen[i] = new Button();
				mainPane.getChildren().add(knoppen[i]);
			}

			mainPane.setAlignment(Pos.CENTER);

			knoppen[0].setText("Nieuw spel");
			knoppen[0].setOnAction(e -> controller.setPreLobbyScene());

			// Maakt een variabele aan die naar het handleiding document verwijst,
			// wanneer je op de handleiding knop drukt wordt het html doc geopend
			// in het default programma voor het openen van .html

			File handleidingDoc = new File("Handleiding.html");
			knoppen[2].setText("Gebruiksaanwijzing");
			knoppen[2].setOnAction(e -> {
				try {
					Desktop.getDesktop().browse(handleidingDoc.toURI());
				} catch (IOException e1) {
					System.out.println(e1);
				}
			});

			knoppen[5].setText("Spel afsluiten");
			knoppen[5].setOnAction(e -> System.exit(0));

		}
	}
