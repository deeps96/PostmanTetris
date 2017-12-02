package de.deeps.postman.app.model;

import de.deeps.postman.Main;
import de.deeps.postman.app.controller.Controller;
import de.deeps.postman.game.controller.GameController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * This class is responsible for loading the fxml files.
 * For each layout the controller gets stored and a scene is generated.
 */
public class FXMLDictionary {

	public enum Layout {
		MAIN, SETTINGS, GAME, GAME_PAUSED, ABOUT, GAME_OVER
	}

	@Getter(AccessLevel.PRIVATE)
	private static final String FXML_DIR = "fxml";
	@Getter(AccessLevel.PRIVATE)
	private static final EnumMap<Layout, String> FXML_MAP = createFXMLMap();

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private HashMap<Layout, FXMLLayout> layouts;

	// initialization
	public FXMLDictionary(Main main) throws IOException {
		setLayouts(new HashMap<>());
		loadAllLayouts(main);
	}

	private void loadAllLayouts(Main main) throws IOException {
		for (Layout layout : Layout.values()) {
			loadLayout(layout, main);
		}
	}

	private void loadLayout(Layout layout, Main main) throws IOException {
		FXMLLoader fxmlLoader = generateNewFXMLLoader();
		Parent rootNode = fxmlLoader.load(
			getClass().getClassLoader()
					.getResourceAsStream(getFilePathForLayout(layout)));
		Controller controller = fxmlLoader.getController();
		controller.setMain(main);
		getLayouts().put(layout, new FXMLLayout(rootNode, controller));
		registerKeyListenersAtLayout(layout, controller);
	}

	private void registerKeyListenersAtLayout(Layout layout,
			EventHandler<KeyEvent> keyEventEventHandler) {
		getLayouts().get(layout).getSceneOfRootNode()
				.setOnKeyPressed(keyEventEventHandler);
		getLayouts().get(layout).getSceneOfRootNode()
				.setOnKeyReleased(keyEventEventHandler);
	}

	private String getFilePathForLayout(Layout layout) {
		return getFXML_DIR() + "/" +  getFXML_MAP().get(layout);
	}

	private FXMLLoader generateNewFXMLLoader() {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setResources(getLocale());
		return fxmlLoader;
	}

	private static EnumMap<Layout, String> createFXMLMap() {
		EnumMap<Layout, String> fxmlMap = new EnumMap<>(Layout.class);
		for (Layout layout : Layout.values()) {
			fxmlMap.put(layout, layout.toString().toLowerCase() + ".fxml");
		}
		return fxmlMap;
	}

	// convencience
	public Scene getSceneForLayout(Layout layout) {
		return getLayouts().get(layout).getSceneOfRootNode();
	}

	public Node getRootNodeOfLayout(Layout layout) {
		return getLayouts().get(layout).getRootNode();
	}

	public GameController getGameController() {
		return (GameController) getLayouts().get(Layout.GAME).getController();
	}

	// accessing
	private ResourceBundle getLocale() {
		return SingleLocale.get();
	}

}
