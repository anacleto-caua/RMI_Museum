package org.video_player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.io.File;

/**
 * A simple JavaFX application to play a video with basic controls.
 */
public class VideoPlayerApp extends Application {

    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private Button playButton;
    private Button stopButton;
    private Button restartButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Video Player");

        // --- 1. Specify the video file ---
        // IMPORTANT: Replace "sample_video.mp4" with the actual path to your video file.
        // It's recommended to place your video file in the same directory as your compiled .class files
        // or provide an absolute path. Supported formats typically include MP4 (H.264), FLV, etc.
        String videoFilePath = "src/videos/sample_video.mp4"; // Example: assuming it's in your project root
        File videoFile = new File(videoFilePath);

        // Check if the file exists
        if (!videoFile.exists()) {
            System.err.println("Error: Video file not found at " + videoFile.getAbsolutePath());
            // You might want to display an alert to the user here
            return;
        }

        String videoUri = videoFile.toURI().toString();
        Media media = new Media(videoUri); // Represents the media source
        mediaPlayer = new MediaPlayer(media); // Controls the playback of the media
        mediaView = new MediaView(mediaPlayer); // Displays the visual component of the media

        // --- 2. Create playback controls (buttons) ---
        playButton = new Button("Play");
        stopButton = new Button("Stop");
        restartButton = new Button("Restart");

        // Play button action: Plays the video
        playButton.setOnAction(e -> {
            mediaPlayer.play();
        });

        // Stop button action: Stops the video and resets its position to the beginning
        stopButton.setOnAction(e -> {
            mediaPlayer.stop();
        });

        // Restart button action: Seeks to the beginning and plays
        restartButton.setOnAction(e -> {
            mediaPlayer.seek(mediaPlayer.getStartTime()); // Go to the start of the media
            mediaPlayer.play(); // Play from the start
        });

        // --- 3. Arrange layout ---
        HBox controls = new HBox(10, playButton, stopButton, restartButton); // Buttons with 10px spacing
        controls.setAlignment(Pos.CENTER); // Center the buttons horizontally

        BorderPane root = new BorderPane();
        root.setCenter(mediaView); // Place the video display in the center
        root.setBottom(controls); // Place the controls at the bottom

        // Bind mediaView size to BorderPane size to make it responsive
        mediaView.fitWidthProperty().bind(root.widthProperty());
        // Subtract controls height from total height to prevent video from overlapping controls
        mediaView.fitHeightProperty().bind(root.heightProperty().subtract(controls.heightProperty()));
        mediaView.setPreserveRatio(true); // Maintain aspect ratio of the video

        // --- 4. Create scene and set stage ---
        Scene scene = new Scene(root, 800, 600); // Initial window size (width, height)
        primaryStage.setScene(scene);
        primaryStage.show(); // Display the window

        // --- 5. Handle application closing ---
        // This ensures media resources are released when the window is closed
        primaryStage.setOnCloseRequest(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop(); // Stop playback
                mediaPlayer.dispose(); // Release media player resources
            }
        });
    }

    public static void main(String[] args) {
        launch(args); // This is the standard way to launch a JavaFX application
    }
}
