package hangman

import hangman.model.Word
import hangman.util.Database
import hangman.view.{ManageController, WordEditController}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import javafx.{scene => jfxs}
import javafx.scene.layout.AnchorPane
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp {
  Database.setupDB()
  val wordData = new ObservableBuffer[Word]()
  wordData ++= Word.getAllWords

  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Hangman Game"
    resizable = false
    scene = new Scene {
      root = roots: jfxs.Parent
    }
  }

  def showInstructions(): Unit = {
    val resource = getClass.getResource("view/Instructions.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    val instructionsRoot: AnchorPane = loader.load()
    roots.setCenter(instructionsRoot)
  }

  def showHome(): Unit = {
    val resource = getClass.getResource("view/Home.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    val roots: AnchorPane = loader.load()
    this.roots.setCenter(roots)
  }

  def showGame(): Unit = {
    val resource = getClass.getResource("view/Game.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots: AnchorPane = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  var manageController: Option[ManageController#Controller] = None
  def showManage() = {
    val resource = getClass.getResource("view/Manage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    val control = loader.getController[ManageController#Controller]()
    manageController = Option(control)
    this.roots.setCenter(roots)
  }

  showHome()

  def showWordEdit(word: Word): Boolean = {
    val resource = getClass.getResourceAsStream("view/WordEdit.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[WordEditController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.word = word
    dialog.showAndWait()
    control.okClicked
  }


}
