package hangman.view

import hangman.MainApp
import hangman.model.Word
import scalafx.scene.control.{Alert, TableColumn, TableView, TextField}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType

@sfxml
class ManageController(
                        private val wordTable: TableView[Word],
                        private val wordColumn: TableColumn[Word,String]
                        ) {
  wordTable.items = MainApp.wordData

  wordColumn.cellValueFactory = a => a.value.wordProperty

  private def showWordDetails (word: Option[Word]) = {
    word match {
      case Some(x) =>
        // Fill the labels with info from the person object.
        wordColumn.text <== x.wordProperty

      case None =>
        // Person is null, remove all the text.
        wordColumn.text.unbind()
        wordColumn.text = ""
    }
  }

  showWordDetails(None)
  wordTable.selectionModel.value.selectedItem.onChange(
    (_, _, newValue) => showWordDetails(Option(newValue))
  )

  def handleAdd(action: ActionEvent) = {
    val word = new Word("")
    val okClicked = MainApp.showWordEdit(word)
    if (okClicked) {
      MainApp.wordData += word
      word.save()
    }
  }

  def handleDelete(action: ActionEvent) = {
    val selectedIndex = wordTable.selectionModel.value.selectedIndex.value
    if (selectedIndex >= 0) {
      val word = wordTable.items.value.remove(selectedIndex)
      word.delete()
    } else {
      val alert = new Alert(AlertType.Warning) {
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Word Selected"
        contentText = "Please select a word in the table."
      }.showAndWait()
    }
  }

  def goHome(): Unit = {
    MainApp.showHome()
  }


}
