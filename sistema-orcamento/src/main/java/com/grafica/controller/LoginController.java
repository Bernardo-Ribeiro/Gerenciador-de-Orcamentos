package com.grafica.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class LoginController {
	private static final double BACKGROUND_BLUR_RADIUS = 24;
	private Image background;

	@FXML
	private StackPane rootPane;

	@FXML
	private ImageView backgroundImage;

	@FXML
	private ImageView frostLayer;

	@FXML
	private VBox glassCard;

	private final Rectangle frostClip = new Rectangle();

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Label messageLabel;

	@FXML
	private void initialize() {
		String imagePath = Objects.requireNonNull(
				getClass().getResource("/com/grafica/img/Background-Login.jpg"),
				"Imagem de background não encontrada"
		).toExternalForm();

		background = new Image(imagePath);
		backgroundImage.setImage(background);
		frostLayer.setImage(background);

		backgroundImage.setPreserveRatio(false);
		frostLayer.setPreserveRatio(false);
		frostLayer.setEffect(new GaussianBlur(BACKGROUND_BLUR_RADIUS));

		frostClip.setArcWidth(36);
		frostClip.setArcHeight(36);
		frostLayer.setClip(frostClip);

		rootPane.layoutBoundsProperty().addListener((obs, oldValue, newValue) -> {
			updateBackgroundCover();
			updateFrostClip();
		});
		glassCard.layoutBoundsProperty().addListener((obs, oldValue, newValue) -> updateFrostClip());
		glassCard.localToSceneTransformProperty().addListener((obs, oldValue, newValue) -> updateFrostClip());

		updateBackgroundCover();
		updateFrostClip();
	}

	private void updateBackgroundCover() {
		double targetWidth = Math.max(1, rootPane.getWidth());
		double targetHeight = Math.max(1, rootPane.getHeight());

		double imageWidth = background.getWidth();
		double imageHeight = background.getHeight();

		if (imageWidth <= 0 || imageHeight <= 0) {
			return;
		}

		double scale = Math.max(targetWidth / imageWidth, targetHeight / imageHeight);
		double viewportWidth = targetWidth / scale;
		double viewportHeight = targetHeight / scale;
		double viewportX = (imageWidth - viewportWidth) / 2;
		double viewportY = (imageHeight - viewportHeight) / 2;

		Rectangle2D viewport = new Rectangle2D(viewportX, viewportY, viewportWidth, viewportHeight);
		backgroundImage.setViewport(viewport);
		frostLayer.setViewport(viewport);

		backgroundImage.setFitWidth(targetWidth);
		backgroundImage.setFitHeight(targetHeight);
		frostLayer.setFitWidth(targetWidth);
		frostLayer.setFitHeight(targetHeight);
	}

	private void updateFrostClip() {
		if (rootPane.getScene() == null) {
			return;
		}

		var boundsInRoot = rootPane.sceneToLocal(glassCard.localToScene(glassCard.getBoundsInLocal()));
		frostClip.setX(boundsInRoot.getMinX());
		frostClip.setY(boundsInRoot.getMinY());
		frostClip.setWidth(Math.max(1, boundsInRoot.getWidth()));
		frostClip.setHeight(Math.max(1, boundsInRoot.getHeight()));
	}

	@FXML
	private void handleLogin() {
		String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
		String password = passwordField.getText() == null ? "" : passwordField.getText().trim();

		if (username.isEmpty() || password.isEmpty()) {
			messageLabel.setText("Informe usuário e senha.");
			messageLabel.getStyleClass().setAll("feedback", "feedback-error");
			return;
		}

		messageLabel.setText("Login realizado com sucesso.");
		messageLabel.getStyleClass().setAll("feedback", "feedback-success");
	}
}
