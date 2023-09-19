package hangman.view

import hangman.model.Word
import hangman.MainApp
import scalafx.scene.control.{Alert, TextField, TextFormatter}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.event.ActionEvent

@sfxml
class WordEditController(
                      private val wordField: TextField
                      ) {

  var dialogStage: Stage = null
  private var _word: Word = null
  var okClicked = false

  def word = _word
  def word_=(x: Word) {
    _word = x
    wordField.text = _word.wordProperty.value.toString
  }


  def isInputValid(): Boolean = {
    var errorMessage = ""
    val text = wordField.text.value
    if (wordField.text.value == null || wordField.text.value.length == 0) {
      errorMessage += "No valid word!\n"
    }
    val nonAlphabeticCharacters = text.replaceAll("[a-zA-Z]", "")
    if (nonAlphabeticCharacters.nonEmpty) {
      errorMessage += s"Contains invalid characters: $nonAlphabeticCharacters\n"
    }
    if (errorMessage.length == 0) {
      return true
    } else {
      val alert = new Alert(Alert.AlertType.Error) {
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()
      return false
    }
  }

  def handleEnter(action :ActionEvent) {
    if (isInputValid()) {
      _word.wordProperty.value = wordField.text.value
      okClicked = true
      dialogStage.close()
    }
  }

  def handleCancel(action :ActionEvent) {
    dialogStage.close()
  }
}